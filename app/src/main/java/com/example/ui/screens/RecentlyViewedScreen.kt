package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.StoreApp
import com.example.ui.components.AppIconTile
import com.example.ui.components.DownloadStatusBadge

@Composable
fun RecentlyViewedScreen(
  recentlyViewedApps: List<StoreApp>,
  activeDownloads: Int,
  completedDownloads: Int,
  onBack: () -> Unit,
  onOpenAppDetail: (StoreApp) -> Unit,
  onDownloadsBadgeClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  BackHandler(onBack = onBack)

  Surface(
    modifier = modifier.fillMaxSize().testTag("recently_viewed_screen"),
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
          modifier = Modifier.testTag("recently_viewed_back_button"),
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
            contentDescription = stringResource(R.string.cd_back),
          )
        }

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = stringResource(R.string.recently_viewed_title),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
          val countLabel =
            if (recentlyViewedApps.size == 1) "1 visited app"
            else "${recentlyViewedApps.size} visited apps"
          Text(
            text = countLabel,
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
        if (recentlyViewedApps.isEmpty()) {
          Column(
            modifier =
              Modifier
                .fillMaxWidth()
                .widthIn(max = 520.dp)
                .padding(horizontal = 24.dp, vertical = 48.dp)
                .testTag("recently_viewed_screen_empty"),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
          ) {
            Icon(
              imageVector = Icons.Rounded.History,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(40.dp),
            )
            Text(
              text = "No recently viewed apps yet",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface,
              textAlign = TextAlign.Center,
            )
            Text(
              text = "Apps you open and inspect inside their details page will appear here.",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              textAlign = TextAlign.Center,
            )
          }
        } else {
          LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier =
              Modifier
                .fillMaxSize()
                .widthIn(max = 920.dp)
                .navigationBarsPadding()
                .testTag("recently_viewed_grid"),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
          ) {
            items(
              items = recentlyViewedApps,
              key = { app -> "recent_grid_${app.id}" },
            ) { app ->
              RecentlyViewedGridTile(
                app = app,
                onClick = { onOpenAppDetail(app) },
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun RecentlyViewedGridTile(
  app: StoreApp,
  onClick: () -> Unit,
) {
  Card(
    modifier =
      Modifier
        .fillMaxWidth()
        .clickable(onClick = onClick)
        .testTag("recently_viewed_grid_item_${app.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
  ) {
    Column(
      modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      AppIconTile(
        iconText = app.iconText,
        backgroundColor = app.color,
        logoResId = app.logoResId,
        iconVector = app.iconVector,
        fillLogoBounds = app.fillLogoBounds,
        contentDescription = "${app.name} logo",
        size = 64.dp,
        cornerRadius = 16.dp,
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = app.name,
        style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.5.sp, lineHeight = 16.sp),
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
        maxLines = 2,
        minLines = 2,
        overflow = TextOverflow.Ellipsis,
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = app.rating,
        style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.5.sp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    }
  }
}
