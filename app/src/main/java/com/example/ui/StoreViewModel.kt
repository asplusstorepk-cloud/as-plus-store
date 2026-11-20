package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.StoreCatalogRepository
import com.example.model.DownloadRecord
import com.example.model.StoreApp
import com.example.model.StoreNotification
import com.example.model.StoreTab
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.max
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StoreUiState(
  val allApps: List<StoreApp> = emptyList(),
  val matchingApps: List<StoreApp> = emptyList(),
  val otherApps: List<StoreApp> = emptyList(),
  val newApps: List<StoreApp> = emptyList(),
  val featuredApp: StoreApp? = null,
  val selectedTab: StoreTab = StoreTab.Apps,
  val searchQuery: String = "",
  val selectedApp: StoreApp? = null,
  val activeDownloads: Int = 0,
  val completedDownloads: Int = 0,
  val downloadHistory: List<DownloadRecord> = emptyList(),
  val notifications: List<StoreNotification> = emptyList(),
  val showDownloadsSheet: Boolean = false,
  val showNotificationsSheet: Boolean = false,
  val showProfileDialog: Boolean = false,
  val requestSearchFocusToken: Int = 0,
)

class StoreViewModel(
  private val repository: StoreCatalogRepository = StoreCatalogRepository(),
  private val clockMillisProvider: () -> Long = { System.currentTimeMillis() },
) : ViewModel() {

  companion object {
    const val SEVEN_DAYS_MILLIS = 7L * 24L * 60L * 60L * 1000L
    private const val ONE_DAY_MILLIS = 24L * 60L * 60L * 1000L
  }

  private val _uiState = MutableStateFlow(createInitialState())
  val uiState: StateFlow<StoreUiState> = _uiState.asStateFlow()

  private fun createInitialState(): StoreUiState {
    val apps = repository.getApps()
    val now = clockMillisProvider()
    val activeNew = computeActiveNewApps(apps, now)
    val activeNotifs = buildNotifications(activeNew, now)

    return StoreUiState(
      allApps = apps,
      matchingApps = apps,
      otherApps = emptyList(),
      newApps = activeNew,
      featuredApp = apps.firstOrNull { it.featured } ?: apps.firstOrNull(),
      notifications = activeNotifs,
    )
  }

  fun onTabSelected(tab: StoreTab) {
    _uiState.update { state ->
      state.copy(
        selectedTab = tab,
        selectedApp = null,
        requestSearchFocusToken =
          if (tab == StoreTab.Search) state.requestSearchFocusToken + 1
          else state.requestSearchFocusToken,
      )
    }
  }

  fun onSearchQueryChange(query: String) {
    _uiState.update { state ->
      val (matches, others) = partitionSearchResults(state.allApps, query)
      state.copy(
        searchQuery = query,
        matchingApps = matches,
        otherApps = others,
      )
    }
  }

  fun openAppDetail(app: StoreApp) {
    _uiState.update { it.copy(selectedApp = app) }
  }

  fun closeAppDetail() {
    _uiState.update { it.copy(selectedApp = null) }
  }

  fun triggerDownload(app: StoreApp) {
    _uiState.update { state ->
      state.copy(
        activeDownloads = state.activeDownloads + 1,
        downloadHistory = listOf(DownloadRecord(app = app, isComplete = false)) + state.downloadHistory,
      )
    }

    viewModelScope.launch {
      delay(1500L)
      _uiState.update { state ->
        val updatedHistory =
          state.downloadHistory.mapIndexed { index, record ->
            if (index == 0 && record.app.id == app.id && !record.isComplete) {
              record.copy(isComplete = true)
            } else {
              record
            }
          }
        state.copy(
          activeDownloads = (state.activeDownloads - 1).coerceAtLeast(0),
          completedDownloads = state.completedDownloads + 1,
          downloadHistory = updatedHistory,
        )
      }
    }
  }

  fun setDownloadsSheetVisible(visible: Boolean) {
    _uiState.update { it.copy(showDownloadsSheet = visible) }
  }

  fun setNotificationsSheetVisible(visible: Boolean) {
    if (visible) {
      val now = clockMillisProvider()
      _uiState.update { state ->
        val activeNew = computeActiveNewApps(state.allApps, now)
        state.copy(
          newApps = activeNew,
          notifications = buildNotifications(activeNew, now),
          showNotificationsSheet = true,
        )
      }
    } else {
      _uiState.update { it.copy(showNotificationsSheet = false) }
    }
  }

  fun setProfileDialogVisible(visible: Boolean) {
    _uiState.update { it.copy(showProfileDialog = visible) }
  }

  private fun computeActiveNewApps(apps: List<StoreApp>, nowMillis: Long): List<StoreApp> =
    apps
      .filter { app ->
        val age = nowMillis - app.addedAtEpochMillis
        age in 0L..SEVEN_DAYS_MILLIS
      }
      .sortedByDescending { it.addedAtEpochMillis }

  private fun buildNotifications(activeNewApps: List<StoreApp>, nowMillis: Long): List<StoreNotification> {
    val formatter = SimpleDateFormat("MMM d", Locale.US)
    return activeNewApps.map { app ->
      val elapsed = nowMillis - app.addedAtEpochMillis
      val daysLeft = max(1, ceil((SEVEN_DAYS_MILLIS - elapsed).toDouble() / ONE_DAY_MILLIS).toInt())
      StoreNotification(
        app = app,
        addedDateLabel = formatter.format(Date(app.addedAtEpochMillis)),
        remainingDaysLabel = "${daysLeft}d left",
      )
    }
  }

  private fun partitionSearchResults(
    apps: List<StoreApp>,
    query: String,
  ): Pair<List<StoreApp>, List<StoreApp>> {
    val q = query.trim().lowercase()
    if (q.isEmpty()) return apps to emptyList()

    val scoredMatches = mutableListOf<Pair<StoreApp, Int>>()
    val others = mutableListOf<StoreApp>()

    for (app in apps) {
      val nameLower = app.name.lowercase()
      val devLower = app.developer.lowercase()
      val catLower = app.category.lowercase()
      val aboutLower = app.about.lowercase()

      val score =
        when {
          nameLower == q -> 100
          nameLower.startsWith(q) -> 80
          nameLower.contains(q) -> 60
          devLower.contains(q) || catLower.contains(q) -> 40
          aboutLower.contains(q) -> 20
          else -> 0
        }

      if (score > 0) {
        scoredMatches.add(app to score)
      } else {
        others.add(app)
      }
    }

    val sortedMatches = scoredMatches.sortedByDescending { it.second }.map { it.first }
    return sortedMatches to others
  }
}
