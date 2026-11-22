package com.example

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import com.example.data.StoreCatalogRepository
import com.example.model.StoreAppsFilter
import com.example.model.StoreOverlay
import com.example.model.StoreTab
import com.example.ui.StoreViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("AS Plus Store", appName)
  }

  @Test
  fun `catalog starts with 18 core and game apps followed by 48 official Google LLC apps`() {
    val apps = StoreCatalogRepository().getApps()
    assertEquals(66, apps.size)

    val expectedFirstEighteen =
      listOf(
        "NICH Employee Attendance 2",
        "Rider Profit 1.0",
        "NICH Employee Attendance 3",
        "ZEE 5",
        "Exion Hill Racing",
        "Devices Tycoon",
        "GTA: San Andreas - Definitive",
        "Grand Theft Auto: Vice City",
        "WhatsApp",
        "TikTok",
        "ChatGPT",
        "TikTok Lite",
        "Facebook",
        "Snapchat",
        "WhatsApp Business",
        "CapCut",
        "Easypaisa",
        "JazzCash",
      )
    assertEquals(expectedFirstEighteen, apps.take(18).map { it.name })

    val nich = apps[0]
    assertEquals("2.5 ★", nich.rating)
    assertEquals("500", nich.downloads)
    assertEquals("28 MB", nich.sizeLabel)
    assertEquals(
      "https://www.mediafire.com/file/dq27ppr8z9wuc8v/NICH_Employee_Attendance_2_1.4.apk/file",
      nich.downloadUrl,
    )
    assertEquals(4, nich.screenshots.size)
    assertTrue(nich.screenshots.all { it.imageResId != null } && nich.logoResId != null)

    val riderProfit = apps[1]
    assertEquals("4.8 ★", riderProfit.rating)
    assertEquals("1M+", riderProfit.downloads)
    assertEquals("23 MB", riderProfit.sizeLabel)
    assertEquals(
      "https://www.mediafire.com/file/klxzx12f9fjvs17/Rider_Profit_1.0.apk/file",
      riderProfit.downloadUrl,
    )
    assertTrue(riderProfit.screenshots.isNotEmpty() && riderProfit.logoResId != null)

    val nich3 = apps[2]
    assertEquals("NICH", nich3.developer)
    assertEquals("v2.0", nich3.version)
    assertEquals("4.8 ★", nich3.rating)
    assertEquals("1k", nich3.downloads)
    assertEquals("110 MB", nich3.sizeLabel)
    assertEquals(
      "https://www.mediafire.com/file/7jngoot02aruyt3/NICH_Employee_Attendance_3_2.0.apk/file",
      nich3.downloadUrl,
    )
    assertTrue(nich3.screenshots.isNotEmpty() && nich3.logoResId != null)

    val zee5 = apps[3]
    assertEquals("ZEE 5", zee5.name)
    assertEquals("Zee Entertainment Enterprises Limited", zee5.developer)
    assertEquals("v39.64.4", zee5.version)
    assertEquals("3.6 ★", zee5.rating)
    assertEquals("100M+", zee5.downloads)
    assertEquals("129 MB", zee5.sizeLabel)
    assertEquals(
      "https://getmodsapk.com/zee5-mod-apk-2022/download/675320/",
      zee5.downloadUrl,
    )
    assertTrue(zee5.screenshots.isNotEmpty() && zee5.logoResId != null)

    val exion = apps[4]
    assertTrue(exion.isGame)
    assertEquals("Exion Games", exion.developer)
    assertEquals("4.3 ★", exion.rating)
    assertEquals("50M+", exion.downloads)
    assertEquals("76 MB", exion.sizeLabel)
    assertEquals("https://play.google.com/store/apps/details?id=com.exiongames.exionhillracing", exion.downloadUrl)
    assertEquals(4, exion.screenshots.size)
    assertTrue(exion.screenshots.all { it.imageResId != null && it.isLandscape } && exion.logoResId != null)

    val tycoon = apps[5]
    assertTrue(tycoon.isGame)
    assertEquals("Roastery Games", tycoon.developer)
    assertEquals("3.8 ★", tycoon.rating)
    assertEquals("1M+", tycoon.downloads)
    assertEquals("75 MB", tycoon.sizeLabel)
    assertEquals("https://play.google.com/store/apps/details?id=com.roasterygames.devicestycoon", tycoon.downloadUrl)
    assertEquals(4, tycoon.screenshots.size)
    assertTrue(tycoon.screenshots.all { it.imageResId != null && it.isLandscape } && tycoon.logoResId != null)

    val gtaSa = apps[6]
    assertTrue(gtaSa.isGame)
    assertEquals("Rockstar Games", gtaSa.developer)
    assertEquals("4.2 ★", gtaSa.rating)
    assertEquals("50K+", gtaSa.downloads)
    assertEquals("2.8 GB", gtaSa.sizeLabel)
    assertEquals("https://play.google.com/store/apps/details?id=com.rockstargames.gtasa.de", gtaSa.downloadUrl)
    assertEquals(4, gtaSa.screenshots.size)
    assertTrue(gtaSa.screenshots.all { it.imageResId != null && it.isLandscape } && gtaSa.logoResId != null)

    val gtaVc = apps[7]
    assertTrue(gtaVc.isGame)
    assertEquals("Rockstar Games", gtaVc.developer)
    assertEquals("3.3 ★", gtaVc.rating)
    assertEquals("1M+", gtaVc.downloads)
    assertEquals("1.4 GB", gtaVc.sizeLabel)
    assertEquals("https://play.google.com/store/apps/details?id=com.rockstargames.gtavc", gtaVc.downloadUrl)
    assertEquals(4, gtaVc.screenshots.size)
    assertTrue(gtaVc.screenshots.all { it.imageResId != null && it.isLandscape } && gtaVc.logoResId != null)

    val games = apps.filter { it.isGame }
    assertEquals(4, games.size)

    val playApps = apps.drop(4)
    assertEquals(62, playApps.size)
    assertTrue(playApps.all { it.downloadUrl.startsWith("https://play.google.com/store/apps/details?id=") })
    assertTrue(playApps.all { it.downloads.isNotEmpty() && (it.sizeLabel.endsWith("MB") || it.sizeLabel.endsWith("GB")) })

    val googleApps = apps.drop(18)
    assertEquals(48, googleApps.size)
    assertTrue(googleApps.all { it.developer == "Google LLC" })
    assertTrue(googleApps.all { it.logoResId != null })
  }

  @Test
  fun `typing shows text suggestions only until search is submitted`() {
    val now = StoreCatalogRepository().getApps().first().addedAtEpochMillis + 1000L
    val viewModel = StoreViewModel(clockMillisProvider = { now })

    viewModel.onSearchQueryChange("WhatsApp")
    val typingState = viewModel.uiState.value
    assertTrue(typingState.isTypingSuggestions)
    assertTrue(typingState.searchSuggestions.any { it.text == "WhatsApp" })
    assertEquals("", typingState.submittedQuery)

    viewModel.submitSearch()
    val submittedState = viewModel.uiState.value
    assertFalse(submittedState.isTypingSuggestions)
    assertEquals("WhatsApp", submittedState.submittedQuery)
    assertEquals(listOf("WhatsApp", "WhatsApp Business"), submittedState.matchingApps.map { it.name })
    assertEquals(64, submittedState.otherApps.size)
  }

  @Test
  fun `favorites can be toggled and persisted across callbacks`() {
    var persistedIds = emptySet<Int>()
    val viewModel =
      StoreViewModel(
        onFavoritesChanged = { persistedIds = it },
      )
    val youtube = viewModel.uiState.value.allApps.first { it.name == "YouTube" }

    val added = viewModel.toggleFavorite(youtube)
    assertTrue(added)
    assertTrue(youtube.id in viewModel.uiState.value.favoriteAppIds)
    assertEquals(setOf(youtube.id), persistedIds)

    viewModel.onTabSelected(StoreTab.Favorites)
    assertEquals(StoreTab.Favorites, viewModel.uiState.value.selectedTab)

    val removed = viewModel.toggleFavorite(youtube)
    assertFalse(removed)
    assertFalse(youtube.id in viewModel.uiState.value.favoriteAppIds)
    assertTrue(persistedIds.isEmpty())
  }

  @Test
  fun `developer apps navigation supports multi-step overlay back stack`() {
    val viewModel = StoreViewModel()
    val youtube = viewModel.uiState.value.allApps.first { it.name == "YouTube" }
    val gmail = viewModel.uiState.value.allApps.first { it.name == "Gmail" }

    viewModel.openAppDetail(youtube)
    assertEquals(youtube, viewModel.uiState.value.selectedApp)

    viewModel.openDeveloperApps("Google LLC")
    assertTrue(viewModel.uiState.value.overlayStack.lastOrNull() is StoreOverlay.DeveloperApps)
    assertEquals("Google LLC", viewModel.uiState.value.selectedDeveloper)

    viewModel.openAppDetail(gmail)
    assertEquals(gmail, viewModel.uiState.value.selectedApp)

    viewModel.popOverlay()
    assertEquals("Google LLC", viewModel.uiState.value.selectedDeveloper)

    viewModel.popOverlay()
    assertEquals(youtube, viewModel.uiState.value.selectedApp)

    viewModel.popOverlay()
    assertTrue(viewModel.uiState.value.overlayStack.isEmpty())
    assertNull(viewModel.uiState.value.selectedApp)
    assertNull(viewModel.uiState.value.selectedDeveloper)
  }

  @Test
  fun `notifications expire automatically after 7 days`() {
    val addedAt = StoreCatalogRepository().getApps()[3].addedAtEpochMillis
    val withinWeek = StoreViewModel(clockMillisProvider = { addedAt + 2L * 24 * 60 * 60 * 1000 })
    assertEquals(18, withinWeek.uiState.value.notifications.size)

    val afterWeek = StoreViewModel(clockMillisProvider = { addedAt + 8L * 24 * 60 * 60 * 1000 })
    assertEquals(0, afterWeek.uiState.value.notifications.size)
  }

  @Test
  fun `recently viewed tracks only visited apps most recent first without duplicates`() {
    var persistedRecent = emptyList<Int>()
    val viewModel =
      StoreViewModel(
        onRecentlyViewedChanged = { persistedRecent = it },
      )
    assertTrue(viewModel.uiState.value.recentlyViewedApps.isEmpty())

    val whatsapp = viewModel.uiState.value.allApps.first { it.name == "WhatsApp" }
    val youtube = viewModel.uiState.value.allApps.first { it.name == "YouTube" }
    val gmail = viewModel.uiState.value.allApps.first { it.name == "Gmail" }

    viewModel.openAppDetail(whatsapp)
    viewModel.popOverlay()
    viewModel.openAppDetail(youtube)
    viewModel.popOverlay()
    viewModel.openAppDetail(whatsapp)
    viewModel.popOverlay()

    assertEquals(listOf(whatsapp.id, youtube.id), viewModel.uiState.value.recentlyViewedAppIds)
    assertEquals(listOf(whatsapp, youtube), viewModel.uiState.value.recentlyViewedApps)
    assertFalse(gmail in viewModel.uiState.value.recentlyViewedApps)
    assertEquals(listOf(whatsapp.id, youtube.id), persistedRecent)

    viewModel.openRecentlyViewed()
    assertTrue(viewModel.uiState.value.overlayStack.lastOrNull() is StoreOverlay.RecentlyViewed)
    viewModel.popOverlay()
    assertTrue(viewModel.uiState.value.overlayStack.isEmpty())
  }

  @Test
  fun `download starts directly without requiring any sign in`() {
    val viewModel = StoreViewModel()
    val nich3 = viewModel.uiState.value.allApps.first { it.name == "NICH Employee Attendance 3" }

    viewModel.triggerDownload(nich3)

    assertEquals(1, viewModel.uiState.value.activeDownloads)
    assertEquals(1, viewModel.uiState.value.downloadHistory.size)
    assertEquals(nich3, viewModel.uiState.value.downloadHistory.first().app)
  }

  @Test
  fun `home tab is default with recommended apps and clean tab switching`() {
    val viewModel = StoreViewModel()
    val initial = viewModel.uiState.value

    assertEquals(StoreTab.Home, initial.selectedTab)
    assertTrue(initial.recommendedApps.take(4).map { it.name } == listOf("WhatsApp", "Snapchat", "TikTok", "YouTube"))

    viewModel.onTabSelected(StoreTab.Apps)
    assertEquals(StoreTab.Apps, viewModel.uiState.value.selectedTab)

    viewModel.setAppsSearchOpen(true)
    assertTrue(viewModel.uiState.value.isAppsSearchOpen)

    viewModel.onSearchQueryChange("Rider")
    viewModel.submitSearch()
    assertEquals("Rider", viewModel.uiState.value.submittedQuery)

    viewModel.setAppsSearchOpen(false)
    assertFalse(viewModel.uiState.value.isAppsSearchOpen)
    assertEquals("", viewModel.uiState.value.submittedQuery)

    viewModel.onTabSelected(StoreTab.Home)
    assertEquals(StoreTab.Home, viewModel.uiState.value.selectedTab)
  }

  @Test
  fun `deep link parser and openAppById open the exact target app directly`() {
    assertEquals(
      12,
      parseDeepLinkAppId(Intent(Intent.ACTION_VIEW, Uri.parse("asplusstore://app/12"))),
    )
    assertEquals(
      61,
      parseDeepLinkAppId(Intent(Intent.ACTION_VIEW, Uri.parse("asplusstore://app?id=61"))),
    )
    assertEquals(
      11,
      parseDeepLinkAppId(
        Intent(Intent.ACTION_VIEW, Uri.parse("https://asplusstore.github.io/?app=11#app-11")),
      ),
    )
    assertEquals(
      13,
      parseDeepLinkAppId(
        Intent(
          Intent.ACTION_VIEW,
          Uri.parse("https://ais-pre-2spsihphri4z5h5xoxcov5-902105820419.asia-northeast1.run.app/#app-13"),
        ),
      ),
    )
    assertNull(
      parseDeepLinkAppId(
        Intent(Intent.ACTION_VIEW, Uri.parse("https://asplusstore.github.io/")),
      ),
    )

    val viewModel = StoreViewModel()
    assertTrue(viewModel.openAppById(12))
    val state = viewModel.uiState.value
    assertEquals("Rider Profit 1.0", state.selectedApp?.name)
    assertEquals(listOf(StoreOverlay.AppDetail(state.selectedApp!!)), state.overlayStack)
    assertEquals(listOf(12), state.recentlyViewedAppIds)

    assertFalse(viewModel.openAppById(999999))
  }

  @Test
  fun `apps tab supports All Games Apps category filters and resets to All`() {
    val viewModel = StoreViewModel()
    assertEquals(66, viewModel.uiState.value.allApps.size)
    assertTrue(viewModel.uiState.value.recommendedApps.any { it.isGame })

    viewModel.onTabSelected(StoreTab.Apps)
    assertEquals(StoreAppsFilter.All, viewModel.uiState.value.selectedAppsFilter)

    viewModel.onAppsFilterSelected(StoreAppsFilter.Games)
    assertEquals(StoreAppsFilter.Games, viewModel.uiState.value.selectedAppsFilter)
    val gamesOnly = viewModel.uiState.value.allApps.filter { it.isGame }
    assertEquals(4, gamesOnly.size)

    viewModel.onAppsFilterSelected(StoreAppsFilter.Apps)
    assertEquals(StoreAppsFilter.Apps, viewModel.uiState.value.selectedAppsFilter)
    val appsOnly = viewModel.uiState.value.allApps.filter { !it.isGame }
    assertEquals(62, appsOnly.size)

    // Re-selecting Apps tab resets filter back to All
    viewModel.onTabSelected(StoreTab.Apps)
    assertEquals(StoreAppsFilter.All, viewModel.uiState.value.selectedAppsFilter)
  }
}
