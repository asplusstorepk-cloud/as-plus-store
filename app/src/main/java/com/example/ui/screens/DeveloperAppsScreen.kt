package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.StoreApp
import com.example.ui.components.AppRowItem
import com.example.ui.components.DownloadStatusBadge

@Composable
fun DeveloperAppsScreen(
  developerName: String,
  allApps: List<StoreApp>,
  activeDownloads: Int,
  completedDownloads: Int,
  onBack: () -> Unit,
  onOpenAppDetail: (StoreApp) -> Unit,
  onDownloadClick: (StoreApp) -> Unit,
  onDownloadsBadgeClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  BackHandler(onBack = onBack)

  val developerApps =
    remember(developerName, allApps) {
      allApps.filter { it.developer == developerName }
    }

  Surface(
    modifier = modifier.fillMaxSize().testTag("developer_apps_screen"),
    color = MaterialTheme.colorScheme.background,
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      Row(
        modifier =
          Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        IconButton(
          onClick = onBack,
          modifier = Modifier.testTag("dev_back_button"),
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
            contentDescription = stringResource(R.string.cd_back),
          )
        }

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = developerName,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
          val countLabel = if (developerApps.size == 1) "1 app" else "${developerApps.size} apps"
          Text(
            text = "$countLabel on AS Plus Store",
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.5.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }

        DownloadStatusBadge(
          activeDownloads = activeDownloads,
          completedDownloads = completedDownloads,
          onClick = onDownloadsBadgeClick,
        )
      }

      HorizontalDivider(color = MaterialTheme.colorScheme.outline)

      Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
      ) {
        LazyColumn(
          modifier = Modifier.fillMaxSize().widthIn(max = 920.dp).navigationBarsPadding(),
          contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          items(
            items = developerApps,
            key = { app -> "dev_app_${app.id}" },
          ) { app ->
            AppRowItem(
              app = app,
              onOpenDetail = onOpenAppDetail,
              onDownloadClick = onDownloadClick,
            )
          }
        }
      }
    }
  }
}
