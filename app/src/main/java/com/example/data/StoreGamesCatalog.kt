package com.example.data

import androidx.compose.ui.graphics.Color
import com.example.model.AppScreenshotPreview
import com.example.model.StoreApp
import com.example.model.StoreTab

internal data class GameShotSpec(
  val caption: String,
  val imageResId: Int,
  val isLandscape: Boolean,
)

internal fun createPlayGame(
  id: Int,
  name: String,
  developer: String,
  category: String,
  rating: String,
  downloads: String,
  ageRating: String,
  sizeLabel: String,
  colorHex: Long,
  logoResId: Int,
  about: String,
  downloadUrl: String,
  addedAtEpochMillis: Long,
  shots: List<GameShotSpec>,
): StoreApp {
  val iconText =
    name
      .split(" ", ":")
      .filter { it.isNotEmpty() }
      .take(2)
      .joinToString("") { it.first().uppercaseChar().toString() }
      .ifEmpty { "GM" }
  return StoreApp(
    id = id,
    name = name,
    developer = developer,
    category = category,
    tab = StoreTab.Apps,
    rating = rating,
    downloads = downloads,
    reviews = "Google Play",
    ageRating = ageRating,
    sizeLabel = sizeLabel,
    version = "Play Store",
    color = Color(colorHex),
    iconText = iconText,
    logoResId = logoResId,
    fillLogoBounds = true,
    heroTitle = "$name • $developer",
    promoTitle = "${name.uppercase()} • ${developer.uppercase()}",
    promoText = about.take(150),
    about = about,
    downloadUrl = downloadUrl,
    addedAtEpochMillis = addedAtEpochMillis,
    isPlayStoreApp = true,
    isGame = true,
    screenshots =
      shots.map {
        AppScreenshotPreview(
          caption = it.caption,
          imageResId = it.imageResId,
          isLandscape = it.isLandscape,
        )
      },
  )
}

internal fun buildExtendedPlayStoreGames(addedAtEpochMillis: Long): List<StoreApp> =
  buildGamesPart1(addedAtEpochMillis) + buildGamesPart2(addedAtEpochMillis)
