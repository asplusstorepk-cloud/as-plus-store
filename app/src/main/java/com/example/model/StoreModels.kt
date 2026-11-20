package com.example.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

enum class StoreTab {
  Apps,
  Search,
}

data class StoreApp(
  val id: Int,
  val name: String,
  val developer: String,
  val category: String,
  val tab: StoreTab,
  val rating: String,
  val reviews: String,
  val ageRating: String,
  val sizeLabel: String,
  val version: String,
  val color: Color,
  val iconText: String,
  @DrawableRes val logoResId: Int? = null,
  val fillLogoBounds: Boolean = false,
  val heroTitle: String,
  val promoTitle: String,
  val promoText: String,
  val about: String,
  val downloadUrl: String,
  val addedAtEpochMillis: Long,
  val isPlayStoreApp: Boolean = false,
  val screenshotCaptions: List<String> = emptyList(),
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
