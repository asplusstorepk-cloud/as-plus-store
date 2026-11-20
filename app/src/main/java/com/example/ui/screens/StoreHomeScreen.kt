package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.CloudDownload
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.DownloadRecord
import com.example.model.StoreApp
import com.example.model.StoreNotification
import com.example.model.StoreTab
import com.example.ui.StoreUiState
import com.example.ui.components.AppIconTile
import com.example.ui.components.AppRowItem
import com.example.ui.components.FeaturedAppCard
import com.example.ui.theme.StoreSuccessGreen
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreHomeScreen(
  uiState: StoreUiState,
  onSearchQueryChange: (String) -> Unit,
  onOpenAppDetail: (StoreApp) -> Unit,
  onDownloadApp: (StoreApp) -> Unit,
  onDismissDownloadsSheet: () -> Unit,
  onDismissNotificationsSheet: () -> Unit,
  onDismissProfileDialog: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val searchFocusRequester = remember { FocusRequester() }
  val isSearching = uiState.searchQuery.trim().isNotEmpty()

  LaunchedEffect(uiState.requestSearchFocusToken) {
    if (uiState.requestSearchFocusToken > 0) {
      searchFocusRequester.requestFocus()
    }
  }

  Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
    LazyColumn(
      modifier = Modifier.fillMaxSize().widthIn(max = 920.dp),
      contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 24.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      item(key = "search_bar") {
        OutlinedTextField(
          value = uiState.searchQuery,
          onValueChange = onSearchQueryChange,
          modifier =
            Modifier
              .fillMaxWidth()
              .focusRequester(searchFocusRequester)
              .testTag("search_input"),
          placeholder = {
            Text(
              text = stringResource(R.string.search_placeholder),
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          },
          leadingIcon = {
            Icon(
              imageVector = Icons.Rounded.Search,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          },
          trailingIcon = {
            if (uiState.searchQuery.isNotEmpty()) {
              IconButton(onClick = { onSearchQueryChange("") }) {
                Icon(
                  imageVector = Icons.Rounded.Close,
                  contentDescription = "Clear search",
                  tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
              }
            }
          },
          singleLine = true,
          shape = RoundedCornerShape(999.dp),
          colors =
            OutlinedTextFieldDefaults.colors(
              focusedContainerColor = MaterialTheme.colorScheme.surface,
              unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
              focusedBorderColor = MaterialTheme.colorScheme.primary,
              unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            ),
        )
      }

      // Top New Apps Auto-Scroll Carousel (3 Apps Visible)
      if (!isSearching && uiState.selectedTab == StoreTab.Apps && uiState.newApps.isNotEmpty()) {
        item(key = "new_apps_carousel") {
          NewAppsAutoScrollSection(
            newApps = uiState.newApps,
            onOpenDetail = onOpenAppDetail,
            onDownloadClick = onDownloadApp,
          )
        }
      }

      // Featured Hero Card
      if (!isSearching && uiState.featuredApp != null && uiState.selectedTab == StoreTab.Apps) {
        item(key = "featured_hero") {
          FeaturedAppCard(
            app = uiState.featuredApp,
            onOpenDetail = onOpenAppDetail,
            onDownloadClick = onDownloadApp,
          )
        }
      }

      // Section Header
      item(key = "section_header") {
        Row(
          modifier = Modifier.fillMaxWidth().padding(top = 6.dp, bottom = 2.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
            text =
              when {
                isSearching -> stringResource(R.string.search_results)
                uiState.selectedTab == StoreTab.Search -> "Search & browse apps"
                else -> stringResource(R.string.suggested_for_you)
              },
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp),
            color = MaterialTheme.colorScheme.onSurface,
          )
          Text(
            text =
              if (isSearching) "${uiState.matchingApps.size} matching"
              else "${uiState.allApps.size} apps",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }

      // Priority Matches First
      if (isSearching && uiState.matchingApps.isEmpty()) {
        item(key = "no_exact_match") {
          Text(
            text = "No exact match found for \"${uiState.searchQuery.trim()}\".",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(vertical = 8.dp),
          )
        }
      } else {
        items(items = uiState.matchingApps, key = { "match_${it.id}" }) { app ->
          AppRowItem(
            app = app,
            isPriorityMatch = isSearching,
            onOpenDetail = onOpenAppDetail,
            onDownloadClick = onDownloadApp,
          )
        }
      }

      // Other Store Apps Below Matching Results
      if (isSearching && uiState.otherApps.isNotEmpty()) {
        item(key = "more_apps_divider") {
          Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Text(
                text = "More apps from AS Plus Store",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
              )
              Text(
                text = "${uiState.otherApps.size} apps",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
          }
        }

        items(items = uiState.otherApps, key = { "other_${it.id}" }) { app ->
          AppRowItem(
            app = app,
            isPriorityMatch = false,
            onOpenDetail = onOpenAppDetail,
            onDownloadClick = onDownloadApp,
          )
        }
      }
    }
  }

  if (uiState.showDownloadsSheet) {
    ModalBottomSheet(onDismissRequest = onDismissDownloadsSheet) {
      DownloadsSheetContent(
        downloads = uiState.downloadHistory,
        onOpenApp = { app ->
          onDismissDownloadsSheet()
          onOpenAppDetail(app)
        },
      )
    }
  }

  if (uiState.showNotificationsSheet) {
    ModalBottomSheet(onDismissRequest = onDismissNotificationsSheet) {
      NotificationsSheetContent(
        notifications = uiState.notifications,
        onOpenApp = { app ->
          onDismissNotificationsSheet()
          onOpenAppDetail(app)
        },
      )
    }
  }

  if (uiState.showProfileDialog) {
    AlertDialog(
      onDismissRequest = onDismissProfileDialog,
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier =
              Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center,
          ) {
            Text(
              text = "A",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp,
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text("Muhammad Ansar", style = MaterialTheme.typography.titleMedium)
            Text(
              "AS Plus Limited • Publisher",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        }
      },
      text = {
        Text(
          "Signed in to AS Plus Store portal. All ${uiState.allApps.size} catalog apps are active and ready.",
          style = MaterialTheme.typography.bodyMedium,
        )
      },
      confirmButton = {
        TextButton(onClick = onDismissProfileDialog) {
          Text("Done")
        }
      },
    )
  }
}

@Composable
private fun NewAppsAutoScrollSection(
  newApps: List<StoreApp>,
  onOpenDetail: (StoreApp) -> Unit,
  onDownloadClick: (StoreApp) -> Unit,
) {
  val listState = rememberLazyListState()
  var direction by remember { mutableFloatStateOf(1f) }

  LaunchedEffect(newApps.size) {
    if (newApps.size <= 3) return@LaunchedEffect
    while (true) {
      delay(16L)
      if (!listState.isScrollInProgress) {
        val delta = 1.1f * direction
        val consumed = listState.scrollBy(delta)
        if (kotlin.math.abs(consumed) < 0.05f) {
          direction *= -1f
          delay(700L)
        }
      }
    }
  }

  Column(modifier = Modifier.fillMaxWidth().testTag("new_apps_section")) {
    Row(
      modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
        text = "New Apps",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp),
        color = MaterialTheme.colorScheme.onSurface,
      )
      Text(
        text = "${newApps.size} new",
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
      val cardWidth = ((maxWidth - 20.dp) / 3).coerceAtLeast(100.dp)
      LazyRow(
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth().testTag("new_apps_carousel"),
      ) {
        items(items = newApps, key = { "new_${it.id}" }) { app ->
          Card(
            modifier =
              Modifier
                .width(cardWidth)
                .clickable { onOpenDetail(app) }
                .testTag("new_app_card_${app.id}"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
          ) {
            Column(
              modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
              AppIconTile(
                iconText = app.iconText,
                backgroundColor = app.color,
                logoResId = app.logoResId,
                fillLogoBounds = app.fillLogoBounds,
                contentDescription = "${app.name} logo",
                size = 54.dp,
                cornerRadius = 14.dp,
              )

              Text(
                text = app.name,
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp, lineHeight = 16.sp),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 2,
                minLines = 2,
                overflow = TextOverflow.Ellipsis,
              )

              Text(
                text = app.version,
                style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
              )

              Button(
                onClick = { onDownloadClick(app) },
                shape = RoundedCornerShape(999.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.fillMaxWidth().height(34.dp),
                colors =
                  ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                  ),
              ) {
                Text(
                  text = stringResource(R.string.download_button),
                  style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                  maxLines = 1,
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun DownloadsSheetContent(
  downloads: List<DownloadRecord>,
  onOpenApp: (StoreApp) -> Unit,
) {
  Column(
    modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp, bottom = 28.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    Text(
      text = "Downloads",
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
          text = "No downloads yet. Tap Download on any app to start.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    } else {
      downloads.forEach { record ->
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          AppIconTile(
            iconText = record.app.iconText,
            backgroundColor = record.app.color,
            logoResId = record.app.logoResId,
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
                if (record.isComplete) "Opened • ${record.app.sizeLabel}"
                else "Preparing ${record.app.version}…",
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
private fun NotificationsSheetContent(
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
