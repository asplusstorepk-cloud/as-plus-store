package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.StoreCatalogRepository
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
  fun `catalog starts with 13 core apps followed by 48 official Google LLC apps`() {
    val apps = StoreCatalogRepository().getApps()
    assertEquals(61, apps.size)

    val expectedFirstThirteen =
      listOf(
        "NICH Employee Attendance 2",
        "Rider Profit 1.0",
        "NICH Employee Attendance 3",
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
    assertEquals(expectedFirstThirteen, apps.take(13).map { it.name })

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

    val playApps = apps.drop(3)
    assertEquals(58, playApps.size)
    assertTrue(playApps.all { it.downloadUrl.startsWith("https://play.google.com/store/apps/details?id=") })
    assertTrue(playApps.all { it.downloads.isNotEmpty() && it.sizeLabel.endsWith("MB") })

    val googleApps = apps.drop(13)
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
    assertEquals(59, submittedState.otherApps.size)
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
    val addedAt = StoreCatalogRepository().getApps()[2].addedAtEpochMillis
    val withinWeek = StoreViewModel(clockMillisProvider = { addedAt + 2L * 24 * 60 * 60 * 1000 })
    assertEquals(13, withinWeek.uiState.value.notifications.size)

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
}
