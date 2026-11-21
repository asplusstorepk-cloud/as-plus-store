package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.StoreCatalogRepository
import com.example.model.DownloadRecord
import com.example.model.GoogleUserProfile
import com.example.model.SearchSuggestion
import com.example.model.StoreApp
import com.example.model.StoreNotification
import com.example.model.StoreOverlay
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
  val recentlyViewedAppIds: List<Int> = emptyList(),
  val recentlyViewedApps: List<StoreApp> = emptyList(),
  val featuredApp: StoreApp? = null,
  val selectedTab: StoreTab = StoreTab.Apps,
  val searchQuery: String = "",
  val submittedQuery: String = "",
  val isTypingSuggestions: Boolean = false,
  val searchSuggestions: List<SearchSuggestion> = emptyList(),
  val selectedApp: StoreApp? = null,
  val selectedDeveloper: String? = null,
  val overlayStack: List<StoreOverlay> = emptyList(),
  val favoriteAppIds: Set<Int> = emptySet(),
  val signedInUser: GoogleUserProfile? = null,
  val pendingDownloadApp: StoreApp? = null,
  val showGoogleSignInPrompt: Boolean = false,
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
  initialFavoriteIds: Set<Int> = emptySet(),
  private val onFavoritesChanged: (Set<Int>) -> Unit = {},
  initialRecentlyViewedIds: List<Int> = emptyList(),
  private val onRecentlyViewedChanged: (List<Int>) -> Unit = {},
  initialGoogleUser: GoogleUserProfile? = null,
  private val onGoogleUserChanged: (GoogleUserProfile?) -> Unit = {},
) : ViewModel() {

  companion object {
    const val SEVEN_DAYS_MILLIS = 7L * 24L * 60L * 60L * 1000L
    private const val ONE_DAY_MILLIS = 24L * 60L * 60L * 1000L
  }

  private val _uiState =
    MutableStateFlow(
      createInitialState(
        initialFavoriteIds = initialFavoriteIds,
        initialRecentlyViewedIds = initialRecentlyViewedIds,
        initialGoogleUser = initialGoogleUser,
      ),
    )
  val uiState: StateFlow<StoreUiState> = _uiState.asStateFlow()

  private fun createInitialState(
    initialFavoriteIds: Set<Int>,
    initialRecentlyViewedIds: List<Int>,
    initialGoogleUser: GoogleUserProfile?,
  ): StoreUiState {
    val apps = repository.getApps()
    val now = clockMillisProvider()
    val activeNew = computeActiveNewApps(apps, now)
    val activeNotifs = buildNotifications(activeNew, now)
    val sanitizedRecentIds = initialRecentlyViewedIds.distinct()
    val recentApps = resolveRecentlyViewedApps(apps, sanitizedRecentIds)

    return StoreUiState(
      allApps = apps,
      matchingApps = apps,
      otherApps = emptyList(),
      newApps = activeNew,
      recentlyViewedAppIds = recentApps.map { it.id },
      recentlyViewedApps = recentApps,
      featuredApp = apps.firstOrNull { it.featured } ?: apps.firstOrNull(),
      favoriteAppIds = initialFavoriteIds,
      signedInUser = initialGoogleUser,
      notifications = activeNotifs,
    )
  }

  fun onTabSelected(tab: StoreTab) {
    _uiState.update { state ->
      when (tab) {
        StoreTab.Apps,
        StoreTab.Favorites ->
          state.copy(
            selectedTab = tab,
            overlayStack = emptyList(),
            selectedApp = null,
            selectedDeveloper = null,
            searchQuery = "",
            submittedQuery = "",
            isTypingSuggestions = false,
            searchSuggestions = emptyList(),
            matchingApps = state.allApps,
            otherApps = emptyList(),
          )
        StoreTab.Search ->
          state.copy(
            selectedTab = tab,
            overlayStack = emptyList(),
            selectedApp = null,
            selectedDeveloper = null,
            requestSearchFocusToken = state.requestSearchFocusToken + 1,
          )
      }
    }
  }

  fun onSearchQueryChange(query: String) {
    _uiState.update { state ->
      val trimmed = query.trim()
      if (trimmed.isEmpty()) {
        state.copy(
          searchQuery = query,
          submittedQuery = "",
          isTypingSuggestions = false,
          searchSuggestions = emptyList(),
          matchingApps = state.allApps,
          otherApps = emptyList(),
        )
      } else {
        state.copy(
          searchQuery = query,
          isTypingSuggestions = true,
          searchSuggestions = buildSearchSuggestions(state.allApps, trimmed),
        )
      }
    }
  }

  fun submitSearch(query: String = _uiState.value.searchQuery) {
    val trimmed = query.trim()
    _uiState.update { state ->
      if (trimmed.isEmpty()) {
        state.copy(
          searchQuery = "",
          submittedQuery = "",
          isTypingSuggestions = false,
          searchSuggestions = emptyList(),
          matchingApps = state.allApps,
          otherApps = emptyList(),
        )
      } else {
        val (matches, others) = partitionSearchResults(state.allApps, trimmed)
        state.copy(
          searchQuery = trimmed,
          submittedQuery = trimmed,
          isTypingSuggestions = false,
          searchSuggestions = emptyList(),
          matchingApps = matches,
          otherApps = others,
        )
      }
    }
  }

  fun clearSearch() {
    _uiState.update { state ->
      state.copy(
        searchQuery = "",
        submittedQuery = "",
        isTypingSuggestions = false,
        searchSuggestions = emptyList(),
        matchingApps = state.allApps,
        otherApps = emptyList(),
      )
    }
  }

  fun openAppDetail(app: StoreApp) {
    _uiState.update { state ->
      val nextStack = state.overlayStack + StoreOverlay.AppDetail(app)
      val nextRecentIds = listOf(app.id) + state.recentlyViewedAppIds.filterNot { it == app.id }
      onRecentlyViewedChanged(nextRecentIds)
      state.copy(
        overlayStack = nextStack,
        selectedApp = app,
        selectedDeveloper = null,
        recentlyViewedAppIds = nextRecentIds,
        recentlyViewedApps = resolveRecentlyViewedApps(state.allApps, nextRecentIds),
      )
    }
  }

  fun openDeveloperApps(developerName: String) {
    _uiState.update { state ->
      val currentTop = state.overlayStack.lastOrNull()
      if (currentTop is StoreOverlay.DeveloperApps && currentTop.developerName == developerName) {
        return@update state
      }
      val nextStack = state.overlayStack + StoreOverlay.DeveloperApps(developerName)
      state.copy(
        overlayStack = nextStack,
        selectedApp = null,
        selectedDeveloper = developerName,
      )
    }
  }

  fun openRecentlyViewed() {
    _uiState.update { state ->
      if (state.overlayStack.lastOrNull() is StoreOverlay.RecentlyViewed) {
        return@update state
      }
      state.copy(
        overlayStack = state.overlayStack + StoreOverlay.RecentlyViewed,
        selectedApp = null,
        selectedDeveloper = null,
      )
    }
  }

  fun popOverlay() {
    _uiState.update { state ->
      val nextStack = if (state.overlayStack.isNotEmpty()) state.overlayStack.dropLast(1) else emptyList()
      val nextTop = nextStack.lastOrNull()
      state.copy(
        overlayStack = nextStack,
        selectedApp = (nextTop as? StoreOverlay.AppDetail)?.app,
        selectedDeveloper = (nextTop as? StoreOverlay.DeveloperApps)?.developerName,
      )
    }
  }

  fun closeAppDetail() {
    popOverlay()
  }

  fun toggleFavorite(app: StoreApp): Boolean {
    var added = false
    _uiState.update { state ->
      val nextFavorites = state.favoriteAppIds.toMutableSet()
      added =
        if (app.id in nextFavorites) {
          nextFavorites.remove(app.id)
          false
        } else {
          nextFavorites.add(app.id)
          true
        }
      val frozen = nextFavorites.toSet()
      onFavoritesChanged(frozen)
      state.copy(favoriteAppIds = frozen)
    }
    return added
  }

  fun requestDownload(app: StoreApp): Boolean {
    val currentUser = _uiState.value.signedInUser
    if (currentUser == null) {
      _uiState.update {
        it.copy(
          pendingDownloadApp = app,
          showGoogleSignInPrompt = true,
        )
      }
      return false
    }
    triggerDownload(app)
    return true
  }

  fun openGoogleSignInPrompt(pendingApp: StoreApp? = null) {
    _uiState.update {
      it.copy(
        pendingDownloadApp = pendingApp,
        showGoogleSignInPrompt = true,
      )
    }
  }

  fun dismissGoogleSignInPrompt() {
    _uiState.update {
      it.copy(
        pendingDownloadApp = null,
        showGoogleSignInPrompt = false,
      )
    }
  }

  fun completeGoogleSignIn(user: GoogleUserProfile): StoreApp? {
    var appToDownload: StoreApp? = null
    _uiState.update { state ->
      appToDownload = state.pendingDownloadApp
      onGoogleUserChanged(user)
      state.copy(
        signedInUser = user,
        pendingDownloadApp = null,
        showGoogleSignInPrompt = false,
      )
    }
    appToDownload?.let { triggerDownload(it) }
    return appToDownload
  }

  fun signOutGoogle() {
    onGoogleUserChanged(null)
    _uiState.update {
      it.copy(
        signedInUser = null,
        pendingDownloadApp = null,
        showGoogleSignInPrompt = false,
      )
    }
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

  private fun resolveRecentlyViewedApps(apps: List<StoreApp>, recentIds: List<Int>): List<StoreApp> {
    if (recentIds.isEmpty()) return emptyList()
    val byId = apps.associateBy { it.id }
    return recentIds.mapNotNull { byId[it] }
  }

  private fun buildSearchSuggestions(apps: List<StoreApp>, rawQuery: String): List<SearchSuggestion> {
    val q = rawQuery.trim().lowercase()
    if (q.isEmpty()) return emptyList()

    val suggestions = mutableListOf<SearchSuggestion>()
    val seen = mutableSetOf<String>()

    for (app in apps) {
      if (
        app.name.lowercase().contains(q) ||
          app.developer.lowercase().contains(q) ||
          app.category.lowercase().contains(q)
      ) {
        if (seen.add(app.name)) {
          suggestions.add(SearchSuggestion(text = app.name, subtitle = app.category))
        }
      }
    }

    if (seen.add(rawQuery.trim())) {
      suggestions.add(SearchSuggestion(text = rawQuery.trim(), subtitle = "Search store"))
    }

    return suggestions.take(7)
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
