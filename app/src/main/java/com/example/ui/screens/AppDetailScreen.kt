package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.StoreApp
import com.example.ui.components.AppIconTile
import com.example.ui.components.AppScreenshotsRow
import com.example.ui.theme.StoreBlue
import com.example.ui.theme.StoreOutlineLight
import com.example.ui.theme.StoreSuccessGreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AppDetailScreen(
  app: StoreApp,
  isFavorite: Boolean,
  onBack: () -> Unit,
  onDownloadClick: (StoreApp) -> Unit,
  onToggleFavorite: (StoreApp) -> Unit,
  onDeveloperClick: (String) -> Unit,
  onShowMessage: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  BackHandler(onBack = onBack)

  val clipboardManager = LocalClipboardManager.current
  var showMoreMenu by remember { mutableStateOf(false) }

  Surface(
    modifier = modifier.fillMaxSize(),
    color = MaterialTheme.colorScheme.background,
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      // Minimal Top Bar: Single Back Arrow (Left) + Single Vertical More Menu (Right)
      Row(
        modifier =
          Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
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
            tint = MaterialTheme.colorScheme.onSurface,
          )
        }

        Box {
          IconButton(
            onClick = { showMoreMenu = true },
            modifier = Modifier.testTag("detail_more_button"),
          ) {
            Icon(
              imageVector = Icons.Rounded.MoreVert,
              contentDescription = stringResource(R.string.cd_more_options),
              tint = MaterialTheme.colorScheme.onSurface,
            )
          }
          DropdownMenu(
            expanded = showMoreMenu,
            onDismissRequest = { showMoreMenu = false },
          ) {
            DropdownMenuItem(
              text = { Text("Share") },
              leadingIcon = {
                Icon(
                  imageVector = Icons.Rounded.Share,
                  contentDescription = null,
                )
              },
              onClick = {
                showMoreMenu = false
                val storeAppUrl = "asplusstore://app/${app.id}"
                clipboardManager.setText(AnnotatedString(storeAppUrl))
                onShowMessage("${app.name} link copied to clipboard")
              },
              modifier = Modifier.testTag("detail_menu_share"),
            )
            DropdownMenuItem(
              text = { Text(if (isFavorite) "Favorited" else "Favorite") },
              leadingIcon = {
                Icon(
                  imageVector =
                    if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                  contentDescription = null,
                  tint =
                    if (isFavorite) Color(0xFFE11D48)
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                )
              },
              onClick = {
                showMoreMenu = false
                onToggleFavorite(app)
              },
              modifier = Modifier.testTag("detail_menu_favorite"),
            )
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
              .verticalScroll(rememberScrollState()),
        ) {
          // Top Light-Blue / White Gradient Header Section: Icon + App Info + Full-Width Download CTA
          Column(
            modifier =
              Modifier
                .fillMaxWidth()
                .background(
                  Brush.verticalGradient(
                    colors =
                      listOf(
                        MaterialTheme.colorScheme.background,
                        Color.White,
                      ),
                  ),
                )
                .padding(horizontal = 20.dp, vertical = 10.dp),
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
              AppIconTile(
                iconText = app.iconText,
                backgroundColor = app.color,
                logoResId = app.logoResId,
                iconVector = app.iconVector,
                fillLogoBounds = app.fillLogoBounds,
                contentDescription = "${app.name} logo",
                size = 86.dp,
                cornerRadius = 20.dp,
                fontSize = 30.sp,
              )

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = app.name,
                  style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 21.sp,
                    lineHeight = 26.sp,
                  ),
                  color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.height(3.dp))

                Row(
                  modifier =
                    Modifier
                      .clip(RoundedCornerShape(6.dp))
                      .clickable { onDeveloperClick(app.developer) }
                      .padding(vertical = 2.dp, horizontal = 2.dp)
                      .testTag("detail_developer_button"),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                  Text(
                    text = app.developer,
                    style = MaterialTheme.typography.bodyMedium.copy(
                      fontWeight = FontWeight.SemiBold,
                      fontSize = 14.sp,
                    ),
                    color = MaterialTheme.colorScheme.primary,
                  )
                  Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = "View all apps by ${app.developer}",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp),
                  )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                  text =
                    buildAnnotatedString {
                      append(app.version)
                      append("  |  ")
                      withStyle(
                        SpanStyle(
                          color = StoreSuccessGreen,
                          fontWeight = FontWeight.SemiBold,
                        ),
                      ) {
                        append(if (app.isPlayStoreApp) "APK • Play Store" else "APK")
                      }
                    },
                  style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp),
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                  text = app.sizeLabel,
                  style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp),
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
              }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Full-Width Royal-Blue Pill Download Button
            Button(
              onClick = { onDownloadClick(app) },
              modifier =
                Modifier
                  .fillMaxWidth()
                  .height(48.dp)
                  .testTag("detail_download_button"),
              shape = RoundedCornerShape(999.dp),
              colors =
                ButtonDefaults.buttonColors(
                  containerColor = StoreBlue,
                  contentColor = Color.White,
                ),
            ) {
              Text(
                text = stringResource(R.string.detail_download_cta),
                style = MaterialTheme.typography.labelLarge.copy(
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold,
                ),
              )
            }

            Spacer(modifier = Modifier.height(10.dp))
          }

          HorizontalDivider(color = StoreOutlineLight)

          // Clean White Main Sheet: Stats Strip + Screenshots + App Description + What's New
          Column(
            modifier =
              Modifier
                .fillMaxWidth()
                .background(Color.White)
                .navigationBarsPadding()
                .padding(bottom = 28.dp),
          ) {
            // 3-Column Metrics Strip (Rating | Downloads | App Size)
            Row(
              modifier =
                Modifier
                  .fillMaxWidth()
                  .height(IntrinsicSize.Min)
                  .padding(horizontal = 16.dp, vertical = 14.dp)
                  .testTag("detail_metrics_strip"),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              MetricCell(
                topText = app.rating,
                subText = stringResource(R.string.metric_rating),
                modifier = Modifier.weight(1f),
              )

              VerticalDivider(
                modifier = Modifier.fillMaxHeight().padding(vertical = 4.dp),
                color = StoreOutlineLight,
              )

              MetricCell(
                topText = app.downloads,
                subText = stringResource(R.string.metric_downloads),
                modifier = Modifier.weight(1f),
              )

              VerticalDivider(
                modifier = Modifier.fillMaxHeight().padding(vertical = 4.dp),
                color = StoreOutlineLight,
              )

              MetricCell(
                topText = app.sizeLabel,
                subText = stringResource(R.string.metric_app_size),
                modifier = Modifier.weight(1f),
              )
            }

            HorizontalDivider(color = StoreOutlineLight)

            // Horizontal Scrollable App Screenshots Strip
            if (app.screenshots.isNotEmpty()) {
              Spacer(modifier = Modifier.height(16.dp))
              AppScreenshotsRow(
                screenshots = app.screenshots,
                onScreenshotClick = { onDownloadClick(app) },
                isDetailSize = true,
                contentPadding = PaddingValues(horizontal = 20.dp),
              )
            }

            Spacer(modifier = Modifier.height(22.dp))

            // App Description Section
            Column(
              modifier =
                Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 20.dp),
            ) {
              Text(
                text = stringResource(R.string.app_description_title),
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 17.5.sp,
                ),
                color = MaterialTheme.colorScheme.onSurface,
              )

              Spacer(modifier = Modifier.height(8.dp))

              Text(
                text = app.about,
                style = MaterialTheme.typography.bodyMedium.copy(
                  fontSize = 14.sp,
                  lineHeight = 21.sp,
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )

              Spacer(modifier = Modifier.height(12.dp))

              FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
              ) {
                MetaChip(text = app.category)
                MetaChip(text = app.version)
                MetaChip(text = "Rated ${app.ageRating}")
                MetaChip(text = app.sizeLabel)
              }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // What's New Section
            Column(
              modifier =
                Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 20.dp),
            ) {
              Text(
                text = stringResource(R.string.whats_new_title),
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 17.5.sp,
                ),
                color = MaterialTheme.colorScheme.onSurface,
              )

              Spacer(modifier = Modifier.height(8.dp))

              Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.background,
                border = BorderStroke(1.dp, StoreOutlineLight),
                modifier = Modifier.fillMaxWidth(),
              ) {
                Column(modifier = Modifier.padding(14.dp)) {
                  Text(
                    text = "${app.promoTitle} (${app.version})",
                    style = MaterialTheme.typography.labelLarge.copy(
                      fontWeight = FontWeight.Bold,
                      fontSize = 13.5.sp,
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                  )
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = app.promoText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                      fontSize = 13.5.sp,
                      lineHeight = 20.sp,
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                  )
                }
              }
            }
          }
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
      style = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
      ),
      color = MaterialTheme.colorScheme.onSurface,
      textAlign = TextAlign.Center,
    )
    Spacer(modifier = Modifier.height(3.dp))
    Text(
      text = subText,
      style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      textAlign = TextAlign.Center,
    )
  }
}

@Composable
private fun MetaChip(text: String) {
  Surface(
    shape = RoundedCornerShape(8.dp),
    color = MaterialTheme.colorScheme.background,
    border = BorderStroke(1.dp, StoreOutlineLight),
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
      color = MaterialTheme.colorScheme.onSurface,
      modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
    )
  }
}
