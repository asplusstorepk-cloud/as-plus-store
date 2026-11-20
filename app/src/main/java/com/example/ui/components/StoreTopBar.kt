package com.example.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.StoreBadgeRed
import com.example.ui.theme.StoreSuccessGreen

@Composable
fun PlayTriangleLogo(modifier: Modifier = Modifier) {
  val primaryColor = MaterialTheme.colorScheme.primary
  Canvas(modifier = modifier.size(width = 22.dp, height = 24.dp)) {
    val path =
      Path().apply {
        moveTo(0f, 0f)
        lineTo(size.width, size.height / 2f)
        lineTo(0f, size.height)
        close()
      }
    drawPath(path = path, color = primaryColor)
  }
}

@Composable
fun AppIconTile(
  iconText: String,
  backgroundColor: Color,
  @DrawableRes logoResId: Int? = null,
  fillLogoBounds: Boolean = false,
  contentDescription: String? = null,
  size: Dp = 56.dp,
  cornerRadius: Dp = 14.dp,
  fontSize: TextUnit = 21.sp,
  modifier: Modifier = Modifier,
) {
  val tileBg = if (logoResId != null) Color.White else backgroundColor
  val shape = RoundedCornerShape(cornerRadius)

  Box(
    modifier =
      modifier
        .size(size)
        .shadow(elevation = 2.dp, shape = shape, clip = false)
        .clip(shape)
        .background(tileBg)
        .then(
          if (logoResId != null) {
            Modifier.border(1.dp, MaterialTheme.colorScheme.outline, shape)
          } else {
            Modifier
          }
        ),
    contentAlignment = Alignment.Center,
  ) {
    if (logoResId != null) {
      Image(
        painter = painterResource(id = logoResId),
        contentDescription = contentDescription,
        contentScale = if (fillLogoBounds) ContentScale.Crop else ContentScale.Fit,
        modifier =
          if (fillLogoBounds) Modifier.fillMaxSize()
          else Modifier.fillMaxSize().padding((size.value * 0.14f).dp),
      )
    } else {
      Text(
        text = iconText,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = fontSize,
      )
    }
  }
}

@Composable
fun DownloadStatusBadge(
  activeDownloads: Int,
  completedDownloads: Int,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val isDownloading = activeDownloads > 0
  val isCompleted = !isDownloading && completedDownloads > 0
  val badgeCount = if (isDownloading) activeDownloads else completedDownloads

  Box(
    modifier =
      modifier
        .minimumInteractiveComponentSize()
        .clip(CircleShape)
        .clickable(role = Role.Button, onClick = onClick)
        .padding(8.dp)
        .testTag("download_status_button"),
    contentAlignment = Alignment.Center,
  ) {
    Icon(
      imageVector = if (isCompleted) Icons.Rounded.Check else Icons.Rounded.ArrowDownward,
      contentDescription = stringResource(R.string.cd_downloads),
      tint = if (isCompleted) StoreSuccessGreen else MaterialTheme.colorScheme.onSurface,
      modifier = Modifier.size(22.dp),
    )

    Box(
      modifier =
        Modifier
          .align(Alignment.TopEnd)
          .offset(x = 5.dp, y = (-4).dp)
          .size(17.dp)
          .clip(CircleShape)
          .background(StoreBadgeRed),
      contentAlignment = Alignment.Center,
    ) {
      Text(
        text = badgeCount.toString(),
        color = Color.White,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 10.sp,
      )
    }
  }
}

@Composable
fun NotificationBellBadge(
  count: Int,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier =
      modifier
        .minimumInteractiveComponentSize()
        .clip(CircleShape)
        .clickable(role = Role.Button, onClick = onClick)
        .padding(8.dp)
        .testTag("notifications_button"),
    contentAlignment = Alignment.Center,
  ) {
    Icon(
      imageVector = Icons.Outlined.Notifications,
      contentDescription = stringResource(R.string.cd_notifications),
      tint = MaterialTheme.colorScheme.onSurface,
      modifier = Modifier.size(22.dp),
    )

    if (count > 0) {
      Box(
        modifier =
          Modifier
            .align(Alignment.TopEnd)
            .offset(x = 5.dp, y = (-4).dp)
            .size(17.dp)
            .clip(CircleShape)
            .background(StoreBadgeRed),
        contentAlignment = Alignment.Center,
      ) {
        Text(
          text = count.toString(),
          color = Color.White,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          lineHeight = 10.sp,
        )
      }
    }
  }
}

@Composable
fun StoreTopBar(
  activeDownloads: Int,
  completedDownloads: Int,
  notificationCount: Int,
  onDownloadsClick: () -> Unit,
  onNotificationsClick: () -> Unit,
  onProfileClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Surface(
    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
    modifier = modifier.fillMaxWidth(),
  ) {
    Row(
      modifier =
        Modifier
          .fillMaxWidth()
          .statusBarsPadding()
          .padding(horizontal = 16.dp, vertical = 10.dp)
          .height(48.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        PlayTriangleLogo()
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = stringResource(R.string.store_brand_title),
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface,
        )
      }

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
      ) {
        DownloadStatusBadge(
          activeDownloads = activeDownloads,
          completedDownloads = completedDownloads,
          onClick = onDownloadsClick,
        )

        NotificationBellBadge(
          count = notificationCount,
          onClick = onNotificationsClick,
        )

        Spacer(modifier = Modifier.width(4.dp))

        Box(
          modifier =
            Modifier
              .minimumInteractiveComponentSize()
              .clip(CircleShape)
              .clickable(role = Role.Button, onClick = onProfileClick)
              .padding(6.dp)
              .testTag("profile_avatar_button"),
          contentAlignment = Alignment.Center,
        ) {
          Box(
            modifier =
              Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center,
          ) {
            Text(
              text = "A",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
            )
          }
        }
      }
    }
  }
}
