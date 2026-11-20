package com.example

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
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Apps
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.StoreApp
import com.example.model.StoreTab
import com.example.ui.StoreViewModel
import com.example.ui.components.StoreTopBar
import com.example.ui.screens.AppDetailScreen
import com.example.ui.screens.StoreHomeScreen
import com.example.ui.theme.AsPlusStoreTheme
import kotlinx.coroutines.launch

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
fun AsPlusStoreApp(viewModel: StoreViewModel = viewModel()) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val context = LocalContext.current
  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()

  val handleDownload: (StoreApp) -> Unit = { app ->
    viewModel.triggerDownload(app)
    scope.launch {
      snackbarHostState.showSnackbar("Starting download: ${app.name} (${app.sizeLabel})")
    }
    runCatching {
      val intent = Intent(Intent.ACTION_VIEW, Uri.parse(app.downloadUrl))
      context.startActivity(intent)
    }
  }

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
            }
          }

          StoreHomeScreen(
            uiState = uiState,
            onSearchQueryChange = viewModel::onSearchQueryChange,
            onOpenAppDetail = viewModel::openAppDetail,
            onDownloadApp = handleDownload,
            onDismissDownloadsSheet = { viewModel.setDownloadsSheetVisible(false) },
            onDismissNotificationsSheet = { viewModel.setNotificationsSheetVisible(false) },
            onDismissProfileDialog = { viewModel.setProfileDialogVisible(false) },
            modifier = Modifier.weight(1f).fillMaxHeight(),
          )
        }
      }
    }

    AnimatedVisibility(
      visible = uiState.selectedApp != null,
      enter = fadeIn() + slideInVertically(initialOffsetY = { it / 6 }),
      exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 6 }),
    ) {
      val selectedApp = uiState.selectedApp
      if (selectedApp != null) {
        AppDetailScreen(
          app = selectedApp,
          activeDownloads = uiState.activeDownloads,
          completedDownloads = uiState.completedDownloads,
          onBack = viewModel::closeAppDetail,
          onDownloadClick = handleDownload,
          onDownloadsBadgeClick = { viewModel.setDownloadsSheetVisible(true) },
          onShowMessage = { msg ->
            scope.launch { snackbarHostState.showSnackbar(msg) }
          },
        )
      }
    }
  }
}
