package com.example.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class StoreTab {
  Home,
  Apps,
  Favorites,
}

enum class StoreAppsFilter {
  All,
  Games,
  Apps,
}

sealed interface StoreOverlay {
  data class AppDetail(val app: StoreApp) : StoreOverlay

  data class DeveloperApps(val developerName: String) : StoreOverlay

  data object RecentlyViewed : StoreOverlay
}

data class ScreenshotRowPreview(
  val title: String,
  val subtitle: String,
  val backgroundColor: Color,
  val textColor: Color,
  val borderColor: Color? = null,
)

data class AppScreenshotPreview(
  val header: String = "",
  val caption: String = "",
  val backgroundColor: Color = Color(0xFF0F1419),
  val textColor: Color = Color.White,
  val topBarColor: Color = Color(0xFF0F1419),
  val topBarTextColor: Color = Color.White,
  val rows: List<ScreenshotRowPreview> = emptyList(),
  @DrawableRes val imageResId: Int? = null,
  val isLandscape: Boolean = false,
)

data class SearchSuggestion(
  val text: String,
  val subtitle: String,
)

data class StoreApp(
  val id: Int,
  val name: String,
  val developer: String,
  val category: String,
  val tab: StoreTab,
  val rating: String,
  val downloads: String,
  val reviews: String,
  val ageRating: String,
  val sizeLabel: String,
  val version: String,
  val color: Color,
  val iconText: String,
  @DrawableRes val logoResId: Int? = null,
  val iconVector: ImageVector? = null,
  val fillLogoBounds: Boolean = true,
  val heroTitle: String,
  val promoTitle: String,
  val promoText: String,
  val about: String,
  val downloadUrl: String,
  val addedAtEpochMillis: Long,
  val isPlayStoreApp: Boolean = true,
  val isGame: Boolean = false,
  val sourceLabel: String? = null,
  val screenshots: List<AppScreenshotPreview> = emptyList(),
  val featured: Boolean = false,
)

data class StoreNotification(
  val app: StoreApp,
  val addedDateLabel: String,
  val remainingDaysLabel: String,
)

data class DownloadRecord(
  val app: StoreApp,
  val isComplete: Boolean,
)
