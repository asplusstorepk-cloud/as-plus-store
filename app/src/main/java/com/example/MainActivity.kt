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
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Search
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.credentials.CredentialManager
import com.example.model.GoogleUserProfile
import com.example.model.StoreApp
import com.example.model.StoreOverlay
import com.example.model.StoreTab
import com.example.ui.StoreViewModel
import com.example.ui.components.GoogleSignInGateScreen
import com.example.ui.components.StoreTopBar
import com.example.ui.components.signOutGoogleAccount
import com.example.ui.screens.AppDetailScreen
import com.example.ui.screens.DeveloperAppsScreen
import com.example.ui.screens.RecentlyViewedScreen
import com.example.ui.screens.StoreHomeScreen
import com.example.ui.theme.AsPlusStoreTheme
import kotlinx.coroutines.launch

private const val PREFS_NAME = "as_plus_store_prefs"
private const val KEY_FAVORITE_IDS = "favorite_app_ids"
private const val KEY_RECENTLY_VIEWED_IDS = "recently_viewed_app_ids"
private const val KEY_GOOGLE_USER_EMAIL = "google_user_email"
private const val KEY_GOOGLE_USER_NAME = "google_user_name"
private const val KEY_GOOGLE_USER_PHOTO = "google_user_photo"

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      AsPlusStoreTheme {
        AsPlusStoreApp()
      }
    }
  }
}

@Composable
fun AsPlusStoreApp() {
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
          val savedEmail = prefs.getString(KEY_GOOGLE_USER_EMAIL, null)
          val savedName = prefs.getString(KEY_GOOGLE_USER_NAME, null)
          val initialUser =
            if (!savedEmail.isNullOrBlank() && !savedName.isNullOrBlank()) {
              GoogleUserProfile(
                email = savedEmail,
                displayName = savedName,
                photoUrl = prefs.getString(KEY_GOOGLE_USER_PHOTO, null),
              )
            } else {
              null
            }
          return StoreViewModel(
            initialFavoriteIds = initialIds,
            onFavoritesChanged = { updated ->
              prefs.edit().putStringSet(KEY_FAVORITE_IDS, updated.map { it.toString() }.toSet()).apply()
            },
            initialRecentlyViewedIds = initialRecentIds,
            onRecentlyViewedChanged = { updated ->
              prefs.edit().putString(KEY_RECENTLY_VIEWED_IDS, updated.joinToString(",")).apply()
            },
            initialGoogleUser = initialUser,
            onGoogleUserChanged = { user ->
              prefs.edit().apply {
                if (user == null) {
                  remove(KEY_GOOGLE_USER_EMAIL)
                  remove(KEY_GOOGLE_USER_NAME)
                  remove(KEY_GOOGLE_USER_PHOTO)
                } else {
                  putString(KEY_GOOGLE_USER_EMAIL, user.email)
                  putString(KEY_GOOGLE_USER_NAME, user.displayName)
                  putString(KEY_GOOGLE_USER_PHOTO, user.photoUrl)
                }
              }.apply()
            },
          ) as T
        }
      }
    }
  val viewModel: StoreViewModel = viewModel(factory = factory)
  AsPlusStoreAppContent(viewModel = viewModel)
}

@Composable
fun AsPlusStoreAppContent(viewModel: StoreViewModel) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val context = LocalContext.current
  val credentialManager = remember(context) { CredentialManager.create(context) }
  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()

  val launchExternalDownload: (StoreApp) -> Unit = { app ->
    scope.launch {
      snackbarHostState.showSnackbar("Starting download: ${app.name} (${app.sizeLabel})")
    }
    runCatching {
      val intent = Intent(Intent.ACTION_VIEW, Uri.parse(app.downloadUrl))
      context.startActivity(intent)
    }
  }

  val handleDownload: (StoreApp) -> Unit = { app ->
    val allowed = viewModel.requestDownload(app)
    if (allowed) {
      launchExternalDownload(app)
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
          StoreTopBar(
            activeDownloads = uiState.activeDownloads,
            completedDownloads = uiState.completedDownloads,
            notificationCount = uiState.notifications.size,
            signedInUser = uiState.signedInUser,
            onDownloadsClick = { viewModel.setDownloadsSheetVisible(true) },
            onNotificationsClick = { viewModel.setNotificationsSheetVisible(true) },
            onProfileClick = { viewModel.setProfileDialogVisible(true) },
          )
        },
        bottomBar = {
          if (!useNavigationRail) {
            NavigationBar(
              containerColor = MaterialTheme.colorScheme.surfaceContainer,
              tonalElevation = 0.dp,
            ) {
              NavigationBarItem(
                selected = uiState.selectedTab == StoreTab.Apps,
                onClick = { viewModel.onTabSelected(StoreTab.Apps) },
                icon = {
                  Icon(
                    imageVector =
                      if (uiState.selectedTab == StoreTab.Apps) Icons.Rounded.Apps
                      else Icons.Outlined.Apps,
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
                    selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                  ),
                modifier = Modifier.testTag("nav_apps"),
              )
              NavigationBarItem(
                selected = uiState.selectedTab == StoreTab.Search,
                onClick = { viewModel.onTabSelected(StoreTab.Search) },
                icon = {
                  Icon(
                    imageVector =
                      if (uiState.selectedTab == StoreTab.Search) Icons.Rounded.Search
                      else Icons.Outlined.Search,
                    contentDescription = stringResource(R.string.nav_search),
                  )
                },
                label = {
                  Text(
                    text = stringResource(R.string.nav_search),
                    fontWeight = FontWeight.SemiBold,
                  )
                },
                colors =
                  NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                  ),
                modifier = Modifier.testTag("nav_search"),
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
                    selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                  ),
                modifier = Modifier.testTag("nav_favorites"),
              )
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
                selected = uiState.selectedTab == StoreTab.Apps,
                onClick = { viewModel.onTabSelected(StoreTab.Apps) },
                icon = {
                  Icon(
                    imageVector =
                      if (uiState.selectedTab == StoreTab.Apps) Icons.Rounded.Apps
                      else Icons.Outlined.Apps,
                    contentDescription = stringResource(R.string.nav_apps),
                  )
                },
                label = { Text(stringResource(R.string.nav_apps)) },
                colors =
                  NavigationRailItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                  ),
                modifier = Modifier.testTag("nav_apps_rail"),
              )
              NavigationRailItem(
                selected = uiState.selectedTab == StoreTab.Search,
                onClick = { viewModel.onTabSelected(StoreTab.Search) },
                icon = {
                  Icon(
                    imageVector =
                      if (uiState.selectedTab == StoreTab.Search) Icons.Rounded.Search
                      else Icons.Outlined.Search,
                    contentDescription = stringResource(R.string.nav_search),
                  )
                },
                label = { Text(stringResource(R.string.nav_search)) },
                colors =
                  NavigationRailItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                  ),
                modifier = Modifier.testTag("nav_search_rail"),
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
                  ),
                modifier = Modifier.testTag("nav_favorites_rail"),
              )
            }
          }

          StoreHomeScreen(
            uiState = uiState,
            onSearchQueryChange = viewModel::onSearchQueryChange,
            onSubmitSearch = viewModel::submitSearch,
            onClearSearch = viewModel::clearSearch,
            onOpenAppDetail = viewModel::openAppDetail,
            onOpenRecentlyViewed = viewModel::openRecentlyViewed,
            onDownloadApp = handleDownload,
            onDismissDownloadsSheet = { viewModel.setDownloadsSheetVisible(false) },
            onDismissNotificationsSheet = { viewModel.setNotificationsSheetVisible(false) },
            onDismissProfileDialog = { viewModel.setProfileDialogVisible(false) },
            onSignInClick = { viewModel.openGoogleSignInPrompt() },
            onSignOutClick = {
              signOutGoogleAccount(credentialManager = credentialManager, scope = scope) {
                viewModel.signOutGoogle()
                scope.launch { snackbarHostState.showSnackbar("Signed out of Google account") }
              }
            },
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
          activeDownloads = uiState.activeDownloads,
          completedDownloads = uiState.completedDownloads,
          onBack = viewModel::popOverlay,
          onDownloadClick = handleDownload,
          onToggleFavorite = handleToggleFavorite,
          onDeveloperClick = viewModel::openDeveloperApps,
          onDownloadsBadgeClick = { viewModel.setDownloadsSheetVisible(true) },
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

    AnimatedVisibility(
      visible = uiState.showGoogleSignInPrompt,
      enter = fadeIn() + slideInVertically(initialOffsetY = { it / 6 }),
      exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 6 }),
    ) {
      GoogleSignInGateScreen(
        pendingApp = uiState.pendingDownloadApp,
        onDismiss = viewModel::dismissGoogleSignInPrompt,
        onSignInSuccess = { profile ->
          val resumedApp = viewModel.completeGoogleSignIn(profile)
          if (resumedApp != null) {
            launchExternalDownload(resumedApp)
          } else {
            scope.launch {
              snackbarHostState.showSnackbar("Signed in as ${profile.displayName}")
            }
          }
        },
      )
    }
  }
}
