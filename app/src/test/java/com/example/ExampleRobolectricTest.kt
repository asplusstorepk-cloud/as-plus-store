package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.StoreCatalogRepository
import com.example.ui.StoreViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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
  fun `homepage catalog contains NICH Employee Attendance 2, Rider Profit 1_0, and all 10 existing apps`() {
    val apps = StoreCatalogRepository().getApps()
    val expectedNames =
      listOf(
        "NICH Employee Attendance 2",
        "Rider Profit 1.0",
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
    assertEquals(expectedNames, apps.map { it.name })

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

    val playApps = apps.drop(2)
    assertEquals(10, playApps.size)
    assertTrue(playApps.all { it.downloadUrl.startsWith("https://play.google.com/store/apps/details?id=") })
    assertTrue(playApps.all { it.downloads.isNotEmpty() && it.sizeLabel.endsWith("MB") })
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
    assertEquals(10, submittedState.otherApps.size)
  }

  @Test
  fun `notifications expire automatically after 7 days`() {
    val addedAt = StoreCatalogRepository().getApps()[1].addedAtEpochMillis
    val withinWeek = StoreViewModel(clockMillisProvider = { addedAt + 2L * 24 * 60 * 60 * 1000 })
    assertEquals(12, withinWeek.uiState.value.notifications.size)

    val afterWeek = StoreViewModel(clockMillisProvider = { addedAt + 8L * 24 * 60 * 60 * 1000 })
    assertEquals(0, afterWeek.uiState.value.notifications.size)
  }
}
