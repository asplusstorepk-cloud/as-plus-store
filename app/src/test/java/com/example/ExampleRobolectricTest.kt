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
  fun `nich employee attendance 2 has mediafire link and no fake mb size`() {
    val apps = StoreCatalogRepository().getApps()
    val nich = apps.first { it.name == "NICH Employee Attendance 2" }
    assertEquals("AS Plus Limited", nich.developer)
    assertEquals("v1.4", nich.version)
    assertEquals(
      "https://www.mediafire.com/file/dq27ppr8z9wuc8v/NICH_Employee_Attendance_2_1.4.apk/file",
      nich.downloadUrl,
    )
    assertFalse(nich.sizeLabel.contains("MB"))
  }

  @Test
  fun `priority search puts matching apps first and keeps other apps below`() {
    val now = StoreCatalogRepository().getApps().first().addedAtEpochMillis + 1000L
    val viewModel = StoreViewModel(clockMillisProvider = { now })

    viewModel.onSearchQueryChange("NICH")
    val state = viewModel.uiState.value
    assertEquals(1, state.matchingApps.size)
    assertEquals("NICH Employee Attendance 2", state.matchingApps.first().name)
    assertEquals(6, state.otherApps.size)
  }

  @Test
  fun `notifications expire automatically after 7 days`() {
    val addedAt = StoreCatalogRepository().getApps().first().addedAtEpochMillis
    val withinWeek = StoreViewModel(clockMillisProvider = { addedAt + 2L * 24 * 60 * 60 * 1000 })
    assertTrue(withinWeek.uiState.value.notifications.isNotEmpty())
    assertTrue(withinWeek.uiState.value.newApps.isNotEmpty())

    val afterWeek = StoreViewModel(clockMillisProvider = { addedAt + 8L * 24 * 60 * 60 * 1000 })
    assertEquals(0, afterWeek.uiState.value.notifications.size)
    assertEquals(0, afterWeek.uiState.value.newApps.size)
  }
}
