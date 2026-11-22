package com.example

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.StoreApp
import com.example.model.StoreOverlay
import com.example.model.StoreTab
import com.example.ui.StoreViewModel
import com.example.ui.components.CleanSectionTopBar
import com.example.ui.components.StoreDashboardHeader
import com.example.ui.screens.AppDetailScreen
import com.example.ui.screens.DeveloperAppsScreen
import com.example.ui.screens.RecentlyViewedScreen
import com.example.ui.screens.StoreHomeScreen
import com.example.ui.theme.AsPlusStoreTheme
import kotlinx.coroutines.launch

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

private const val PREFS_NAME = "as_plus_store_prefs"
private const val KEY_FAVORITE_IDS = "favorite_app_ids"
private const val KEY_RECENTLY_VIEWED_IDS = "recently_viewed_app_ids"

internal fun parseDeepLinkAppId(intent: Intent?): Int? {
  val uri = intent?.data ?: return null
  uri.getQueryParameter("app")?.toIntOrNull()?.let { return it }
  uri.getQueryParameter("id")?.toIntOrNull()?.let { return it }
  if (uri.scheme.equals("asplusstore", ignoreCase = true) &&
    (uri.host.equals("app", ignoreCase = true) || uri.host.equals("apps", ignoreCase = true))
  ) {
    uri.pathSegments.firstOrNull()?.toIntOrNull()?.let { return it }
  }
  val segments = uri.pathSegments
  if (segments.size >= 2 && segments[0].equals("app", ignoreCase = true)) {
    segments[1].toIntOrNull()?.let { return it }
  }
  val fragment = uri.fragment.orEmpty()
  val fragmentMatch = Regex("^(?:app-|app=|/app/)(\\d+)$", RegexOption.IGNORE_CASE).find(fragment)
  return fragmentMatch?.groupValues?.getOrNull(1)?.toIntOrNull()
}

class MainActivity : ComponentActivity() {
  private var pendingDeepLinkAppId by mutableStateOf<Int?>(null)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    pendingDeepLinkAppId = parseDeepLinkAppId(intent)
    enableEdgeToEdge()
    setContent {
      AsPlusStoreTheme {
        AsPlusStoreApp(
          deepLinkAppId = pendingDeepLinkAppId,
          onDeepLinkConsumed = { pendingDeepLinkAppId = null },
        )
      }
    }
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    setIntent(intent)
    parseDeepLinkAppId(intent)?.let { appId ->
      pendingDeepLinkAppId = appId
    }
  }
}

@Composable
fun AsPlusStoreApp(
  deepLinkAppId: Int? = null,
  onDeepLinkConsumed: () -> Unit = {},
) {
  val context = LocalContext.current
  val prefs = remember(context) { context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }
  val factory =
    remember(prefs) {
      object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
          val initialIds =
            prefs.getStringSet(KEY_FAVORITE_IDS, emptySet())
              ?.mapNotNull { it.toIntOrNull() }
              ?.toSet()
              ?: emptySet()
          val initialRecentIds =
            prefs.getString(KEY_RECENTLY_VIEWED_IDS, "")
              .orEmpty()
              .split(",")
              .mapNotNull { it.trim().toIntOrNull() }
              .distinct()
          return StoreViewModel(
            initialFavoriteIds = initialIds,
            onFavoritesChanged = { updated ->
              prefs.edit().putStringSet(KEY_FAVORITE_IDS, updated.map { it.toString() }.toSet()).apply()
            },
            initialRecentlyViewedIds = initialRecentIds,
            onRecentlyViewedChanged = { updated ->
              prefs.edit().putString(KEY_RECENTLY_VIEWED_IDS, updated.joinToString(",")).apply()
            },
          ) as T
        }
      }
    }
  val viewModel: StoreViewModel = viewModel(factory = factory)
  AsPlusStoreAppContent(
    viewModel = viewModel,
    deepLinkAppId = deepLinkAppId,
    onDeepLinkConsumed = onDeepLinkConsumed,
  )
}

@Composable
fun AsPlusStoreAppContent(
  viewModel: StoreViewModel,
  deepLinkAppId: Int? = null,
  onDeepLinkConsumed: () -> Unit = {},
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val context = LocalContext.current
  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()
  val searchFocusRequester = remember { FocusRequester() }

  LaunchedEffect(deepLinkAppId) {
    if (deepLinkAppId != null) {
      viewModel.openAppById(deepLinkAppId)
      onDeepLinkConsumed()
    }
  }

  LaunchedEffect(uiState.requestSearchFocusToken) {
    if (uiState.requestSearchFocusToken > 0) {
      runCatching { searchFocusRequester.requestFocus() }
    }
  }

  val handleDownload: (StoreApp) -> Unit = { app ->
    viewModel.triggerDownload(app)
    scope.launch {
      snackbarHostState.showSnackbar("Starting install: ${app.name} (${app.sizeLabel})")
    }
    runCatching {
      val intent = Intent(Intent.ACTION_VIEW, Uri.parse(app.downloadUrl))
      context.startActivity(intent)
    }
  }

  val handleToggleFavorite: (StoreApp) -> Unit = { app ->
    val added = viewModel.toggleFavorite(app)
    scope.launch {
      snackbarHostState.showSnackbar(
        if (added) "${app.name} added to Favorites" else "${app.name} removed from Favorites",
      )
    }
  }

  val currentOverlay = uiState.overlayStack.lastOrNull()

  Box(modifier = Modifier.fillMaxSize()) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
      val useNavigationRail = maxWidth >= 600.dp

      Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
          when (uiState.selectedTab) {
            StoreTab.Home -> {
              StoreDashboardHeader(
                searchQuery = uiState.searchQuery,
                notificationCount = uiState.notifications.size,
                searchFocusRequester = searchFocusRequester,
                onMenuClick = { viewModel.setMenuSheetVisible(true) },
                onNotificationsClick = { viewModel.setNotificationsSheetVisible(true) },
                onProfileClick = { viewModel.setProfileSheetVisible(true) },
                onSearchQueryChange = viewModel::onSearchQueryChange,
                onSubmitSearch = viewModel::submitSearch,
                onClearSearch = viewModel::clearSearch,
              )
            }
            StoreTab.Apps -> {
              CleanSectionTopBar(
                title = stringResource(R.string.all_apps_title),
                showSearchIcon = true,
                isSearchExpanded = uiState.isAppsSearchOpen,
                searchQuery = uiState.searchQuery,
                searchFocusRequester = searchFocusRequester,
                onBackClick = { viewModel.onTabSelected(StoreTab.Home) },
                onToggleSearch = { viewModel.setAppsSearchOpen(!uiState.isAppsSearchOpen) },
                onSearchQueryChange = viewModel::onSearchQueryChange,
                onSubmitSearch = viewModel::submitSearch,
                onClearSearch = viewModel::clearSearch,
              )
            }
            StoreTab.Favorites -> {
              CleanSectionTopBar(
                title = stringResource(R.string.nav_favorites),
                showSearchIcon = false,
                isSearchExpanded = false,
                searchQuery = "",
                searchFocusRequester = searchFocusRequester,
                onBackClick = { viewModel.onTabSelected(StoreTab.Home) },
                onToggleSearch = {},
                onSearchQueryChange = {},
                onSubmitSearch = {},
                onClearSearch = {},
              )
            }
          }
        },
        bottomBar = {
          if (!useNavigationRail) {
            androidx.compose.foundation.layout.Column {
              HorizontalDivider(color = MaterialTheme.colorScheme.outline)
              NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                tonalElevation = 0.dp,
              ) {
                NavigationBarItem(
                  selected = uiState.selectedTab == StoreTab.Home,
                  onClick = { viewModel.onTabSelected(StoreTab.Home) },
                  icon = {
                    Icon(
                      imageVector =
                        if (uiState.selectedTab == StoreTab.Home) Icons.Rounded.Home
                        else Icons.Outlined.Home,
                      contentDescription = stringResource(R.string.nav_home),
                    )
                  },
                  label = {
                    Text(
                      text = stringResource(R.string.nav_home),
                      fontWeight = FontWeight.SemiBold,
                    )
                  },
                  colors =
                    NavigationBarItemDefaults.colors(
                      indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                      selectedIconColor = MaterialTheme.colorScheme.primary,
                      selectedTextColor = MaterialTheme.colorScheme.primary,
                      unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                      unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                  modifier = Modifier.testTag("nav_home"),
                )
                NavigationBarItem(
                  selected = uiState.selectedTab == StoreTab.Apps,
                  onClick = { viewModel.onTabSelected(StoreTab.Apps) },
                  icon = {
                    Icon(
                      imageVector =
                        if (uiState.selectedTab == StoreTab.Apps) Icons.Rounded.GridView
                        else Icons.Outlined.GridView,
                      contentDescription = stringResource(R.string.nav_apps),
                    )
                  },
                  label = {
                    Text(
                      text = stringResource(R.string.nav_apps),
                      fontWeight = FontWeight.SemiBold,
                    )
                  },
                  colors =
                    NavigationBarItemDefaults.colors(
                      indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                      selectedIconColor = MaterialTheme.colorScheme.primary,
                      selectedTextColor = MaterialTheme.colorScheme.primary,
                      unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                      unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                  modifier = Modifier.testTag("nav_apps"),
                )
                NavigationBarItem(
                  selected = uiState.selectedTab == StoreTab.Favorites,
                  onClick = { viewModel.onTabSelected(StoreTab.Favorites) },
                  icon = {
                    Icon(
                      imageVector =
                        if (uiState.selectedTab == StoreTab.Favorites) Icons.Rounded.Favorite
                        else Icons.Outlined.FavoriteBorder,
                      contentDescription = stringResource(R.string.nav_favorites),
                    )
                  },
                  label = {
                    Text(
                      text = stringResource(R.string.nav_favorites),
                      fontWeight = FontWeight.SemiBold,
                    )
                  },
                  colors =
                    NavigationBarItemDefaults.colors(
                      indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                      selectedIconColor = MaterialTheme.colorScheme.primary,
                      selectedTextColor = MaterialTheme.colorScheme.primary,
                      unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                      unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                  modifier = Modifier.testTag("nav_favorites"),
                )
              }
            }
          }
        },
      ) { innerPadding ->
        Row(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
          if (useNavigationRail) {
            NavigationRail(
              modifier = Modifier.fillMaxHeight(),
              containerColor = MaterialTheme.colorScheme.surfaceContainer,
            ) {
              NavigationRailItem(
                selected = uiState.selectedTab == StoreTab.Home,
                onClick = { viewModel.onTabSelected(StoreTab.Home) },
                icon = {
                  Icon(
                    imageVector =
                      if (uiState.selectedTab == StoreTab.Home) Icons.Rounded.Home
                      else Icons.Outlined.Home,
                    contentDescription = stringResource(R.string.nav_home),
                  )
                },
                label = { Text(stringResource(R.string.nav_home)) },
                colors =
                  NavigationRailItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                  ),
                modifier = Modifier.testTag("nav_home_rail"),
              )
              NavigationRailItem(
                selected = uiState.selectedTab == StoreTab.Apps,
                onClick = { viewModel.onTabSelected(StoreTab.Apps) },
                icon = {
                  Icon(
                    imageVector =
                      if (uiState.selectedTab == StoreTab.Apps) Icons.Rounded.GridView
                      else Icons.Outlined.GridView,
                    contentDescription = stringResource(R.string.nav_apps),
                  )
                },
                label = { Text(stringResource(R.string.nav_apps)) },
                colors =
                  NavigationRailItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                  ),
                modifier = Modifier.testTag("nav_apps_rail"),
              )
              NavigationRailItem(
                selected = uiState.selectedTab == StoreTab.Favorites,
                onClick = { viewModel.onTabSelected(StoreTab.Favorites) },
                icon = {
                  Icon(
                    imageVector =
                      if (uiState.selectedTab == StoreTab.Favorites) Icons.Rounded.Favorite
                      else Icons.Outlined.FavoriteBorder,
                    contentDescription = stringResource(R.string.nav_favorites),
                  )
                },
                label = { Text(stringResource(R.string.nav_favorites)) },
                colors =
                  NavigationRailItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                  ),
                modifier = Modifier.testTag("nav_favorites_rail"),
              )
            }
          }

          StoreHomeScreen(
            uiState = uiState,
            onSelectTab = viewModel::onTabSelected,
            onSelectAppsFilter = viewModel::onAppsFilterSelected,
            onSubmitSearch = viewModel::submitSearch,
            onClearSearch = viewModel::clearSearch,
            onOpenAppDetail = viewModel::openAppDetail,
            onOpenRecentlyViewed = viewModel::openRecentlyViewed,
            onDownloadApp = handleDownload,
            onDismissDownloadsSheet = { viewModel.setDownloadsSheetVisible(false) },
            onDismissNotificationsSheet = { viewModel.setNotificationsSheetVisible(false) },
            onDismissMenuSheet = { viewModel.setMenuSheetVisible(false) },
            onDismissProfileSheet = { viewModel.setProfileSheetVisible(false) },
            onOpenDownloadsFromSheet = { viewModel.setDownloadsSheetVisible(true) },
            modifier = Modifier.weight(1f).fillMaxHeight(),
          )
        }
      }
    }

    AnimatedVisibility(
      visible = currentOverlay is StoreOverlay.AppDetail,
      enter = fadeIn() + slideInVertically(initialOffsetY = { it / 6 }),
      exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 6 }),
    ) {
      val detailOverlay = currentOverlay as? StoreOverlay.AppDetail
      if (detailOverlay != null) {
        AppDetailScreen(
          app = detailOverlay.app,
          isFavorite = detailOverlay.app.id in uiState.favoriteAppIds,
          onBack = viewModel::popOverlay,
          onDownloadClick = handleDownload,
          onToggleFavorite = handleToggleFavorite,
          onDeveloperClick = viewModel::openDeveloperApps,
          onShowMessage = { msg ->
            scope.launch { snackbarHostState.showSnackbar(msg) }
          },
        )
      }
    }

    AnimatedVisibility(
      visible = currentOverlay is StoreOverlay.DeveloperApps,
      enter = fadeIn() + slideInVertically(initialOffsetY = { it / 6 }),
      exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 6 }),
    ) {
      val devOverlay = currentOverlay as? StoreOverlay.DeveloperApps
      if (devOverlay != null) {
        DeveloperAppsScreen(
          developerName = devOverlay.developerName,
          allApps = uiState.allApps,
          activeDownloads = uiState.activeDownloads,
          completedDownloads = uiState.completedDownloads,
          onBack = viewModel::popOverlay,
          onOpenAppDetail = viewModel::openAppDetail,
          onDownloadClick = handleDownload,
          onDownloadsBadgeClick = { viewModel.setDownloadsSheetVisible(true) },
        )
      }
    }

    AnimatedVisibility(
      visible = currentOverlay is StoreOverlay.RecentlyViewed,
      enter = fadeIn() + slideInVertically(initialOffsetY = { it / 6 }),
      exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 6 }),
    ) {
      RecentlyViewedScreen(
        recentlyViewedApps = uiState.recentlyViewedApps,
        activeDownloads = uiState.activeDownloads,
        completedDownloads = uiState.completedDownloads,
        onBack = viewModel::popOverlay,
        onOpenAppDetail = viewModel::openAppDetail,
        onDownloadsBadgeClick = { viewModel.setDownloadsSheetVisible(true) },
      )
    }
  }
}
