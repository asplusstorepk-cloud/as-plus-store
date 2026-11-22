package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.CloudDownload
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.DownloadRecord
import com.example.model.StoreApp
import com.example.model.StoreNotification
import com.example.model.StoreTab
import com.example.ui.theme.StoreSuccessGreen

@Composable
fun StoreMenuSheetContent(
  totalAppsCount: Int,
  favoritesCount: Int,
  recentlyViewedCount: Int,
  downloadsCount: Int,
  onSelectTab: (StoreTab) -> Unit,
  onOpenRecentlyViewed: () -> Unit,
  onOpenDownloads: () -> Unit,
) {
  Column(
    modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp, bottom = 28.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Image(
        painter = painterResource(id = R.drawable.logo_as_plus_badge),
        contentDescription = stringResource(R.string.store_brand_title),
        contentScale = ContentScale.Crop,
        modifier = Modifier.size(54.dp).clip(RoundedCornerShape(14.dp)),
      )
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = stringResource(R.string.store_brand_title),
          style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
          text = stringResource(R.string.store_tagline),
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }

    HorizontalDivider(color = MaterialTheme.colorScheme.outline)

    MenuNavRow(
      icon = Icons.Rounded.Home,
      title = stringResource(R.string.nav_home),
      badgeText = null,
      onClick = { onSelectTab(StoreTab.Home) },
    )
    MenuNavRow(
      icon = Icons.Rounded.Apps,
      title = stringResource(R.string.all_apps_title),
      badgeText = "$totalAppsCount",
      onClick = { onSelectTab(StoreTab.Apps) },
    )
    MenuNavRow(
      icon = Icons.Rounded.Favorite,
      title = stringResource(R.string.nav_favorites),
      badgeText = "$favoritesCount",
      onClick = { onSelectTab(StoreTab.Favorites) },
    )
    MenuNavRow(
      icon = Icons.Rounded.History,
      title = stringResource(R.string.recently_viewed_title),
      badgeText = "$recentlyViewedCount",
      onClick = onOpenRecentlyViewed,
    )
    MenuNavRow(
      icon = Icons.Rounded.CloudDownload,
      title = stringResource(R.string.cd_downloads),
      badgeText = "$downloadsCount",
      onClick = onOpenDownloads,
    )
  }
}

@Composable
private fun MenuNavRow(
  icon: ImageVector,
  title: String,
  badgeText: String?,
  onClick: () -> Unit,
) {
  Surface(
    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).clickable(onClick = onClick),
    shape = RoundedCornerShape(14.dp),
    color = MaterialTheme.colorScheme.surfaceVariant,
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(22.dp),
      )
      Text(
        text = title,
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.weight(1f),
      )
      if (badgeText != null) {
        Surface(
          shape = RoundedCornerShape(999.dp),
          color = MaterialTheme.colorScheme.primaryContainer,
        ) {
          Text(
            text = badgeText,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
          )
        }
      }
      Icon(
        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.size(18.dp),
      )
    }
  }
}

@Composable
fun StoreProfileSheetContent(
  favoritesCount: Int,
  recentlyViewedCount: Int,
  completedDownloadsCount: Int,
  onOpenFavorites: () -> Unit,
  onOpenDownloads: () -> Unit,
) {
  Column(
    modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp, bottom = 28.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Surface(
        shape = RoundedCornerShape(999.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.size(48.dp),
      ) {
        Icon(
          imageVector = Icons.Rounded.Person,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.padding(11.dp),
        )
      }
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = "AS Plus Store Activity",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        )
        Text(
          text = "Direct install access unlocked for all apps",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      ProfileStatBox(label = "Favorites", value = favoritesCount.toString(), modifier = Modifier.weight(1f))
      ProfileStatBox(label = "Viewed", value = recentlyViewedCount.toString(), modifier = Modifier.weight(1f))
      ProfileStatBox(label = "Installs", value = completedDownloadsCount.toString(), modifier = Modifier.weight(1f))
    }

    MenuNavRow(
      icon = Icons.Rounded.Favorite,
      title = "Open Saved Favorites",
      badgeText = "$favoritesCount",
      onClick = onOpenFavorites,
    )
    MenuNavRow(
      icon = Icons.Rounded.CloudDownload,
      title = "Recent Install Activity",
      badgeText = "$completedDownloadsCount",
      onClick = onOpenDownloads,
    )
  }
}

@Composable
private fun ProfileStatBox(
  label: String,
  value: String,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier,
    shape = RoundedCornerShape(14.dp),
    color = MaterialTheme.colorScheme.surfaceVariant,
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
  ) {
    Column(
      modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(
        text = value,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.primary,
      )
      Text(
        text = label,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}

@Composable
fun DownloadsSheetContent(
  downloads: List<DownloadRecord>,
  onOpenApp: (StoreApp) -> Unit,
) {
  Column(
    modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp, bottom = 28.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    Text(
      text = "Downloads & Installs",
      style = MaterialTheme.typography.titleLarge,
    )
    if (downloads.isEmpty()) {
      Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Icon(
          imageVector = Icons.Rounded.CloudDownload,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(32.dp),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
          text = "No installs yet. Tap Install on any app to open its download page.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    } else {
      downloads.forEach { record ->
        val sourceText = if (record.app.isPlayStoreApp) "Google Play Store" else "Direct APK (${record.app.version})"
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          AppIconTile(
            iconText = record.app.iconText,
            backgroundColor = record.app.color,
            logoResId = record.app.logoResId,
            iconVector = record.app.iconVector,
            fillLogoBounds = record.app.fillLogoBounds,
            size = 44.dp,
            cornerRadius = 11.dp,
            fontSize = 17.sp,
          )
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(record.app.name, style = MaterialTheme.typography.titleSmall)
            Text(
              text =
                if (record.isComplete) "Opened • $sourceText"
                else "Opening $sourceText…",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (!record.isComplete) {
              Spacer(modifier = Modifier.height(6.dp))
              LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
          }
          Spacer(modifier = Modifier.width(8.dp))
          if (record.isComplete) {
            Icon(
              imageVector = Icons.Rounded.CheckCircle,
              contentDescription = "Completed",
              tint = StoreSuccessGreen,
            )
          }
          TextButton(onClick = { onOpenApp(record.app) }) {
            Text("View")
          }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
      }
    }
  }
}

@Composable
fun NotificationsSheetContent(
  notifications: List<StoreNotification>,
  onOpenApp: (StoreApp) -> Unit,
) {
  Column(
    modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp, bottom = 28.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
        text = "New App Notifications",
        style = MaterialTheme.typography.titleLarge,
      )
      Text(
        text = "Last 7 days",
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }

    if (notifications.isEmpty()) {
      Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Icon(
          imageVector = Icons.Rounded.NotificationsNone,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(32.dp),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
          text = "No new app notifications from the last 7 days.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    } else {
      notifications.forEach { item ->
        Row(
          modifier =
            Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .clickable { onOpenApp(item.app) }
              .padding(vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          AppIconTile(
            iconText = item.app.iconText,
            backgroundColor = item.app.color,
            logoResId = item.app.logoResId,
            iconVector = item.app.iconVector,
            fillLogoBounds = item.app.fillLogoBounds,
            size = 44.dp,
            cornerRadius = 11.dp,
          )
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = item.app.name,
              style = MaterialTheme.typography.titleSmall,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
            Text(
              text = "Newly added ${item.addedDateLabel} • ${item.app.developer}",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(999.dp),
          ) {
            Text(
              text = item.remainingDaysLabel,
              style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.sp),
              color = MaterialTheme.colorScheme.onPrimaryContainer,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            )
          }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
      }
    }
  }
}
