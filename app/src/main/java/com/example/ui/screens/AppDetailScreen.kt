package com.example.ui.screens

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.StoreApp
import com.example.ui.components.AppIconTile
import com.example.ui.components.AppScreenshotsRow
import com.example.ui.components.DownloadStatusBadge
import com.example.ui.theme.PreviewBannerEnd
import com.example.ui.theme.PreviewBannerMid
import com.example.ui.theme.PreviewBannerStart

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AppDetailScreen(
  app: StoreApp,
  activeDownloads: Int,
  completedDownloads: Int,
  onBack: () -> Unit,
  onDownloadClick: (StoreApp) -> Unit,
  onDownloadsBadgeClick: () -> Unit,
  onShowMessage: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  BackHandler(onBack = onBack)

  val context = LocalContext.current
  val clipboardManager = LocalClipboardManager.current
  var showMoreMenu by remember { mutableStateOf(false) }

  Surface(
    modifier = modifier.fillMaxSize(),
    color = MaterialTheme.colorScheme.background,
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      // Sticky Detail Top Bar
      Row(
        modifier =
          Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        IconButton(
          onClick = onBack,
          modifier = Modifier.testTag("detail_back_button"),
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
            contentDescription = stringResource(R.string.cd_back),
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          DownloadStatusBadge(
            activeDownloads = activeDownloads,
            completedDownloads = completedDownloads,
            onClick = onDownloadsBadgeClick,
          )

          Box {
            IconButton(
              onClick = { showMoreMenu = true },
              modifier = Modifier.testTag("detail_more_button"),
            ) {
              Icon(
                imageVector = Icons.Rounded.MoreVert,
                contentDescription = stringResource(R.string.cd_more_options),
              )
            }
            DropdownMenu(
              expanded = showMoreMenu,
              onDismissRequest = { showMoreMenu = false },
            ) {
              DropdownMenuItem(
                text = { Text("Share download link") },
                onClick = {
                  showMoreMenu = false
                  val shareIntent =
                    Intent(Intent.ACTION_SEND).apply {
                      type = "text/plain"
                      putExtra(Intent.EXTRA_SUBJECT, "${app.name} - AS Plus Store")
                      putExtra(
                        Intent.EXTRA_TEXT,
                        "Download ${app.name} on AS Plus Store: ${app.downloadUrl}",
                      )
                    }
                  context.startActivity(Intent.createChooser(shareIntent, "Share ${app.name}"))
                },
              )
              DropdownMenuItem(
                text = {
                  Text(if (app.isPlayStoreApp) "Copy Play Store URL" else "Copy download URL")
                },
                onClick = {
                  showMoreMenu = false
                  clipboardManager.setText(AnnotatedString(app.downloadUrl))
                  onShowMessage("Download link copied to clipboard")
                },
              )
            }
          }
        }
      }

      // Scrollable Detail Content
      Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
      ) {
        Column(
          modifier =
            Modifier
              .fillMaxWidth()
              .widthIn(max = 780.dp)
              .verticalScroll(rememberScrollState())
              .navigationBarsPadding()
              .padding(horizontal = 18.dp, vertical = 8.dp),
        ) {
          // Hero Header
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
          ) {
            AppIconTile(
              iconText = app.iconText,
              backgroundColor = app.color,
              logoResId = app.logoResId,
              fillLogoBounds = app.fillLogoBounds,
              contentDescription = "${app.name} logo",
              size = 84.dp,
              cornerRadius = 20.dp,
              fontSize = 30.sp,
            )

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = app.name,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = app.developer,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.primary,
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = app.sourceLabel ?: "Google Play Store • Official App",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.5.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
          }

          Spacer(modifier = Modifier.height(20.dp))

          // Full-Width Download Button
          Button(
            onClick = { onDownloadClick(app) },
            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("detail_download_button"),
            shape = RoundedCornerShape(999.dp),
            colors =
              ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
              ),
          ) {
            Text(
              text = if (app.isPlayStoreApp) "Download on Google Play" else "Download",
              style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp),
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          // 3-Column Metrics Strip Under Download Button (Rating, Downloads, Size)
          Row(
            modifier =
              Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(vertical = 6.dp)
                .testTag("detail_metrics_strip"),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            MetricCell(
              topText = app.rating,
              subText = "Rating",
              modifier = Modifier.weight(1f),
            )

            VerticalDivider(
              modifier = Modifier.fillMaxHeight().padding(vertical = 4.dp),
              color = MaterialTheme.colorScheme.outline,
            )

            MetricCell(
              topText = app.downloads,
              subText = "Downloads",
              modifier = Modifier.weight(1f),
            )

            VerticalDivider(
              modifier = Modifier.fillMaxHeight().padding(vertical = 4.dp),
              color = MaterialTheme.colorScheme.outline,
            )

            MetricCell(
              topText = app.sizeLabel,
              subText = "Size",
              modifier = Modifier.weight(1f),
            )
          }

          Spacer(modifier = Modifier.height(18.dp))

          // Play Store Screenshots Strip (Only in Detail Screen)
          if (app.screenshots.isNotEmpty()) {
            AppScreenshotsRow(
              screenshots = app.screenshots,
              onScreenshotClick = { onDownloadClick(app) },
              isDetailSize = true,
            )
            Spacer(modifier = Modifier.height(20.dp))
          }

          // Preview / Promo Card
          Box(
            modifier =
              Modifier
                .fillMaxWidth()
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp), clip = false)
                .clip(RoundedCornerShape(16.dp))
                .background(
                  Brush.linearGradient(
                    colors = listOf(PreviewBannerStart, PreviewBannerMid, PreviewBannerEnd),
                  )
                )
                .padding(20.dp),
          ) {
            Column {
              Text(
                text = app.promoTitle,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
              )
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = app.promoText,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.95f),
              )
            }
          }

          Spacer(modifier = Modifier.height(22.dp))

          // About this app
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = stringResource(R.string.about_this_app),
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp),
            )
            Icon(
              imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = app.about,
            style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 22.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )

          Spacer(modifier = Modifier.height(14.dp))

          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            MetaChip(text = app.category)
            MetaChip(text = app.version)
            MetaChip(text = "Rated ${app.ageRating}")
            MetaChip(text = app.sizeLabel)
          }

          Spacer(modifier = Modifier.height(24.dp))
        }
      }
    }
  }
}

@Composable
private fun MetricCell(
  topText: String,
  subText: String,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier.padding(horizontal = 8.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      text = topText,
      style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
      color = MaterialTheme.colorScheme.onSurface,
      textAlign = TextAlign.Center,
    )
    Spacer(modifier = Modifier.height(3.dp))
    Text(
      text = subText,
      style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      textAlign = TextAlign.Center,
    )
  }
}

@Composable
private fun MetaChip(text: String) {
  Surface(
    shape = RoundedCornerShape(8.dp),
    color = MaterialTheme.colorScheme.surfaceVariant,
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
      color = MaterialTheme.colorScheme.onSurface,
      modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
    )
  }
}
