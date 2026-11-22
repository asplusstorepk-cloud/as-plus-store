package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.NorthWest
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.SearchSuggestion
import com.example.model.StoreApp
import com.example.model.StoreAppsFilter
import com.example.model.StoreTab
import com.example.ui.StoreUiState
import com.example.ui.components.AppIconTile
import com.example.ui.components.AppRowItem
import com.example.ui.components.DownloadsSheetContent
import com.example.ui.components.HeroPhoneIllustration
import com.example.ui.components.NotificationsSheetContent
import com.example.ui.components.StoreMenuSheetContent
import com.example.ui.components.StoreProfileSheetContent
import com.example.ui.components.VipCrownIcon
import com.example.ui.components.VipShieldEmblem
import com.example.ui.theme.HeroBannerEnd
import com.example.ui.theme.HeroBannerMid
import com.example.ui.theme.HeroBannerStart
import com.example.ui.theme.StoreBadgeRed
import com.example.ui.theme.StoreGold
import com.example.ui.theme.StoreGoldDeep
import com.example.ui.theme.VipBannerEnd
import com.example.ui.theme.VipBannerMid
import com.example.ui.theme.VipBannerStart
import kotlinx.coroutines.launch

private data class HeroSlide(
  val badge: String,
  val titleTop: String,
  val titleHighlight: String,
  val buttonLabel: String,
)

private val HomeHeroSlides =
  listOf(
    HeroSlide(badge = "TOP APPS", titleTop = "Best Apps", titleHighlight = "For You", buttonLabel = "Explore Now"),
    HeroSlide(badge = "NEW APPS", titleTop = "Fresh Picks", titleHighlight = "Updated", buttonLabel = "Explore Now"),
    HeroSlide(badge = "OFFICIAL", titleTop = "Google Suite", titleHighlight = "Verified", buttonLabel = "Explore Now"),
    HeroSlide(badge = "DIRECT APK", titleTop = "Fast & Safe", titleHighlight = "Installs", buttonLabel = "Explore Now"),
  )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreHomeScreen(
  uiState: StoreUiState,
  onSelectTab: (StoreTab) -> Unit,
  onSelectAppsFilter: (StoreAppsFilter) -> Unit,
  onSubmitSearch: (String) -> Unit,
  onClearSearch: () -> Unit,
  onOpenAppDetail: (StoreApp) -> Unit,
  onOpenRecentlyViewed: () -> Unit,
  onDownloadApp: (StoreApp) -> Unit,
  onDismissDownloadsSheet: () -> Unit,
  onDismissNotificationsSheet: () -> Unit,
  onDismissMenuSheet: () -> Unit,
  onDismissProfileSheet: () -> Unit,
  onOpenDownloadsFromSheet: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val keyboardController = LocalSoftwareKeyboardController.current
  val isSubmittedSearch = uiState.submittedQuery.trim().isNotEmpty()
  val isShowingSuggestions = uiState.isTypingSuggestions && uiState.searchQuery.trim().isNotEmpty()

  val favoriteApps =
    remember(uiState.allApps, uiState.favoriteAppIds) {
      uiState.allApps.filter { it.id in uiState.favoriteAppIds }
    }

  BackHandler(
    enabled =
      uiState.overlayStack.isEmpty() &&
        (isShowingSuggestions || isSubmittedSearch || uiState.selectedTab != StoreTab.Home),
  ) {
    when {
      isShowingSuggestions || isSubmittedSearch -> {
        keyboardController?.hide()
        onClearSearch()
      }
      uiState.selectedTab != StoreTab.Home -> onSelectTab(StoreTab.Home)
    }
  }

  Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
    LazyColumn(
      modifier = Modifier.fillMaxSize().widthIn(max = 920.dp),
      contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 24.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
      if (isShowingSuggestions) {
        item(key = "search_suggestions_panel") {
          SearchSuggestionsCard(
            suggestions = uiState.searchSuggestions,
            onSuggestionClick = { picked ->
              keyboardController?.hide()
              onSubmitSearch(picked)
            },
          )
        }
      } else if (isSubmittedSearch) {
        item(key = "search_results_header") {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = stringResource(R.string.search_results),
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp),
              color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
              text = "${uiState.matchingApps.size} matching",
              style = MaterialTheme.typography.labelMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        }

        if (uiState.matchingApps.isEmpty()) {
          item(key = "no_exact_match") {
            Text(
              text = "No exact match found for \"${uiState.submittedQuery.trim()}\".",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(vertical = 8.dp),
            )
          }
        } else {
          items(items = uiState.matchingApps, key = { "match_${it.id}" }) { app ->
            AppRowItem(
              app = app,
              isPriorityMatch = true,
              onOpenDetail = onOpenAppDetail,
              onDownloadClick = onDownloadApp,
            )
          }
        }

        if (uiState.otherApps.isNotEmpty()) {
          item(key = "more_apps_divider") {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) {
              HorizontalDivider(color = MaterialTheme.colorScheme.outline)
              Spacer(modifier = Modifier.height(10.dp))
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
      } else {
        when (uiState.selectedTab) {
          StoreTab.Home -> {
            item(key = "top_apps_hero_banner") {
              TopAppsHeroBanner(
                onExploreClick = { onSelectTab(StoreTab.Apps) },
              )
            }

            item(key = "recommended_for_you_section") {
              RecommendedForYouSection(
                recommendedApps = uiState.recommendedApps,
                onSeeAllClick = { onSelectTab(StoreTab.Apps) },
                onOpenDetail = onOpenAppDetail,
                onInstallClick = onDownloadApp,
              )
            }

            item(key = "vip_apps_banner") {
              VipAppsBanner(
                onExploreVipClick = { onSelectTab(StoreTab.Apps) },
              )
            }

            item(key = "recently_viewed_section") {
              RecentlyViewedSection(
                recentlyViewedApps = uiState.recentlyViewedApps,
                onOpenAll = onOpenRecentlyViewed,
                onOpenDetail = onOpenAppDetail,
                onDownloadClick = onDownloadApp,
              )
            }

            item(key = "home_all_apps_header") {
              Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
              ) {
                Text(
                  text = stringResource(R.string.home_all_apps_title),
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 17.5.sp),
                  color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                  text = "${uiState.allApps.size} available",
                  style = MaterialTheme.typography.labelMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
              }
            }

            items(items = uiState.allApps, key = { "home_continuous_${it.id}" }) { app ->
              AppRowItem(
                app = app,
                isPriorityMatch = false,
                onOpenDetail = onOpenAppDetail,
                onDownloadClick = onDownloadApp,
              )
            }
          }

          StoreTab.Apps -> {
            val filteredApps =
              when (uiState.selectedAppsFilter) {
                StoreAppsFilter.All -> uiState.allApps
                StoreAppsFilter.Games -> uiState.allApps.filter { it.isGame }
                StoreAppsFilter.Apps -> uiState.allApps.filter { !it.isGame }
              }

            item(key = "apps_category_bar") {
              Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
              ) {
                Row(
                  horizontalArrangement = Arrangement.spacedBy(8.dp),
                  verticalAlignment = Alignment.CenterVertically,
                ) {
                  AppsCategoryChip(
                    label = stringResource(R.string.filter_all),
                    selected = uiState.selectedAppsFilter == StoreAppsFilter.All,
                    onClick = { onSelectAppsFilter(StoreAppsFilter.All) },
                    modifier = Modifier.testTag("filter_chip_all"),
                  )
                  AppsCategoryChip(
                    label = stringResource(R.string.filter_games),
                    selected = uiState.selectedAppsFilter == StoreAppsFilter.Games,
                    onClick = { onSelectAppsFilter(StoreAppsFilter.Games) },
                    modifier = Modifier.testTag("filter_chip_games"),
                  )
                  AppsCategoryChip(
                    label = stringResource(R.string.filter_apps),
                    selected = uiState.selectedAppsFilter == StoreAppsFilter.Apps,
                    onClick = { onSelectAppsFilter(StoreAppsFilter.Apps) },
                    modifier = Modifier.testTag("filter_chip_apps"),
                  )
                }

                Text(
                  text =
                    when (uiState.selectedAppsFilter) {
                      StoreAppsFilter.All -> "${filteredApps.size} items"
                      StoreAppsFilter.Games -> "${filteredApps.size} games"
                      StoreAppsFilter.Apps -> "${filteredApps.size} apps"
                    },
                  style = MaterialTheme.typography.labelMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
              }
            }

            items(items = filteredApps, key = { "all_app_${uiState.selectedAppsFilter.name}_${it.id}" }) { app ->
              AppRowItem(
                app = app,
                isPriorityMatch = false,
                onOpenDetail = onOpenAppDetail,
                onDownloadClick = onDownloadApp,
              )
            }
          }

          StoreTab.Favorites -> {
            item(key = "favorites_count_row") {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
              ) {
                Text(
                  text = "Saved Apps",
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp),
                  color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                  text = "${favoriteApps.size} saved",
                  style = MaterialTheme.typography.labelMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
              }
            }

            if (favoriteApps.isEmpty()) {
              item(key = "favorites_empty") {
                FavoritesEmptyCard()
              }
            } else {
              items(items = favoriteApps, key = { "fav_${it.id}" }) { app ->
                AppRowItem(
                  app = app,
                  onOpenDetail = onOpenAppDetail,
                  onDownloadClick = onDownloadApp,
                )
              }
            }
          }
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

  if (uiState.showMenuSheet) {
    ModalBottomSheet(onDismissRequest = onDismissMenuSheet) {
      StoreMenuSheetContent(
        totalAppsCount = uiState.allApps.size,
        favoritesCount = uiState.favoriteAppIds.size,
        recentlyViewedCount = uiState.recentlyViewedApps.size,
        downloadsCount = uiState.completedDownloads + uiState.activeDownloads,
        onSelectTab = onSelectTab,
        onOpenRecentlyViewed = onOpenRecentlyViewed,
        onOpenDownloads = onOpenDownloadsFromSheet,
      )
    }
  }

  if (uiState.showProfileSheet) {
    ModalBottomSheet(onDismissRequest = onDismissProfileSheet) {
      StoreProfileSheetContent(
        favoritesCount = uiState.favoriteAppIds.size,
        recentlyViewedCount = uiState.recentlyViewedApps.size,
        completedDownloadsCount = uiState.completedDownloads,
        onOpenFavorites = { onSelectTab(StoreTab.Favorites) },
        onOpenDownloads = onOpenDownloadsFromSheet,
      )
    }
  }
}

@Composable
private fun TopAppsHeroBanner(
  onExploreClick: () -> Unit,
) {
  val pagerState = rememberPagerState(pageCount = { HomeHeroSlides.size })
  val scope = rememberCoroutineScope()

  Box(
    modifier =
      Modifier
        .fillMaxWidth()
        .shadow(elevation = 6.dp, shape = RoundedCornerShape(20.dp), clip = false)
        .clip(RoundedCornerShape(20.dp))
        .background(
          Brush.linearGradient(
            colors = listOf(HeroBannerStart, HeroBannerMid, HeroBannerEnd),
          ),
        )
        .testTag("top_apps_hero_banner"),
  ) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
      HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth(),
      ) { page ->
        val slide = HomeHeroSlides[page]
        Row(
          modifier =
            Modifier
              .fillMaxWidth()
              .clickable(onClick = onExploreClick)
              .padding(start = 16.dp, end = 10.dp, top = 14.dp, bottom = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
        ) {
          Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            Surface(
              shape = RoundedCornerShape(999.dp),
              color = StoreBadgeRed,
            ) {
              Text(
                text = slide.badge,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.4.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
              )
            }

            Column {
              Text(
                text = slide.titleTop,
                color = Color.White,
                fontSize = 23.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 26.sp,
              )
              Text(
                text = slide.titleHighlight,
                color = StoreGold,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                fontStyle = FontStyle.Italic,
                lineHeight = 27.sp,
              )
            }

            Surface(
              shape = RoundedCornerShape(999.dp),
              color = Color.Transparent,
              modifier = Modifier.clip(RoundedCornerShape(999.dp)).clickable(onClick = onExploreClick),
            ) {
              Row(
                modifier =
                  Modifier
                    .background(Brush.horizontalGradient(listOf(StoreGold, StoreGoldDeep)))
                    .padding(horizontal = 14.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp),
              ) {
                Text(
                  text = slide.buttonLabel,
                  color = Color(0xFF091A4A),
                  fontSize = 12.5.sp,
                  fontWeight = FontWeight.ExtraBold,
                )
                Icon(
                  imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                  contentDescription = null,
                  tint = Color(0xFF091A4A),
                  modifier = Modifier.size(16.dp),
                )
              }
            }
          }

          HeroPhoneIllustration()
        }
      }

      // Pagination dots
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        repeat(HomeHeroSlides.size) { index ->
          val isSelected = pagerState.currentPage == index
          Box(
            modifier =
              Modifier
                .size(if (isSelected) 8.dp else 7.dp)
                .clip(CircleShape)
                .background(if (isSelected) Color.White else Color.White.copy(alpha = 0.4f))
                .clickable { scope.launch { pagerState.animateScrollToPage(index) } },
          )
        }
      }
    }
  }
}

@Composable
private fun RecommendedForYouSection(
  recommendedApps: List<StoreApp>,
  onSeeAllClick: () -> Unit,
  onOpenDetail: (StoreApp) -> Unit,
  onInstallClick: (StoreApp) -> Unit,
) {
  Column(modifier = Modifier.fillMaxWidth().testTag("recommended_section")) {
    Row(
      modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
      ) {
        Icon(
          imageVector = Icons.Rounded.Star,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(20.dp),
        )
        Text(
          text = stringResource(R.string.suggested_for_you),
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 17.5.sp),
          color = MaterialTheme.colorScheme.onSurface,
        )
      }

      Row(
        modifier =
          Modifier
            .clip(RoundedCornerShape(999.dp))
            .clickable(onClick = onSeeAllClick)
            .padding(horizontal = 6.dp, vertical = 4.dp)
            .testTag("recommended_see_all_button"),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = "See All",
          style = MaterialTheme.typography.labelLarge.copy(fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold),
          color = MaterialTheme.colorScheme.primary,
        )
        Icon(
          imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(18.dp),
        )
      }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
      val cardWidth = ((maxWidth - 24.dp) / 4).coerceAtLeast(80.dp)
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth().testTag("recommended_apps_row"),
      ) {
        items(items = recommendedApps, key = { "rec_${it.id}" }) { app ->
          val cleanRating = app.rating.replace("★", "").trim()
          Card(
            modifier =
              Modifier
                .width(cardWidth)
                .clickable { onOpenDetail(app) }
                .testTag("recommended_card_${app.id}"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          ) {
            Column(
              modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 10.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
              AppIconTile(
                iconText = app.iconText,
                backgroundColor = app.color,
                logoResId = app.logoResId,
                iconVector = app.iconVector,
                fillLogoBounds = app.fillLogoBounds,
                contentDescription = "${app.name} logo",
                size = 46.dp,
                cornerRadius = 12.dp,
              )

              Text(
                text = app.name,
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 11.5.sp, lineHeight = 14.sp),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 2,
                minLines = 2,
                overflow = TextOverflow.Ellipsis,
              )

              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
              ) {
                Icon(
                  imageVector = Icons.Rounded.Star,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.size(11.dp),
                )
                Text(
                  text = cleanRating,
                  style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.sp),
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  maxLines = 1,
                )
              }

              Button(
                onClick = { onInstallClick(app) },
                shape = RoundedCornerShape(999.dp),
                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp),
                modifier = Modifier.fillMaxWidth().height(28.dp).testTag("recommended_install_${app.id}"),
                colors =
                  ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                  ),
              ) {
                Text(
                  text = stringResource(R.string.install_button),
                  style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp),
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
private fun VipAppsBanner(
  onExploreVipClick: () -> Unit,
) {
  Box(
    modifier =
      Modifier
        .fillMaxWidth()
        .shadow(elevation = 4.dp, shape = RoundedCornerShape(18.dp), clip = false)
        .clip(RoundedCornerShape(18.dp))
        .background(
          Brush.horizontalGradient(
            colors = listOf(VipBannerStart, VipBannerMid, VipBannerEnd),
          ),
        )
        .clickable(onClick = onExploreVipClick)
        .padding(horizontal = 16.dp, vertical = 14.dp)
        .testTag("vip_apps_banner"),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(6.dp),
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
          VipCrownIcon(size = 20.dp)
          Text(
            text = "VIP Apps",
            color = StoreGold,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
          )
        }

        Text(
          text = "Premium Apps for You",
          color = Color.White.copy(alpha = 0.95f),
          fontSize = 13.5.sp,
          fontWeight = FontWeight.Medium,
        )

        Spacer(modifier = Modifier.height(2.dp))

        Surface(
          shape = RoundedCornerShape(999.dp),
          color = Color.Transparent,
          modifier = Modifier.clip(RoundedCornerShape(999.dp)).clickable(onClick = onExploreVipClick),
        ) {
          Row(
            modifier =
              Modifier
                .background(Brush.horizontalGradient(listOf(StoreGold, StoreGoldDeep)))
                .padding(horizontal = 14.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp),
          ) {
            Text(
              text = "Explore VIP",
              color = Color(0xFF2B1400),
              fontSize = 12.5.sp,
              fontWeight = FontWeight.ExtraBold,
            )
            Icon(
              imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
              contentDescription = null,
              tint = Color(0xFF2B1400),
              modifier = Modifier.size(16.dp),
            )
          }
        }
      }

      VipShieldEmblem()
    }
  }
}

@Composable
private fun RecentlyViewedSection(
  recentlyViewedApps: List<StoreApp>,
  onOpenAll: () -> Unit,
  onOpenDetail: (StoreApp) -> Unit,
  onDownloadClick: (StoreApp) -> Unit,
) {
  Column(modifier = Modifier.fillMaxWidth().testTag("recently_viewed_section")) {
    Row(
      modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
        text = stringResource(R.string.recently_viewed_title),
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp),
        color = MaterialTheme.colorScheme.onSurface,
      )

      IconButton(
        onClick = onOpenAll,
        modifier = Modifier.size(48.dp).testTag("recently_viewed_arrow_button"),
      ) {
        Surface(
          shape = CircleShape,
          color = MaterialTheme.colorScheme.surface,
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
          modifier = Modifier.size(34.dp),
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(
              imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
              contentDescription = stringResource(R.string.cd_view_all_recently_viewed),
              tint = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.size(18.dp),
            )
          }
        }
      }
    }

    if (recentlyViewedApps.isEmpty()) {
      Card(
        modifier = Modifier.fillMaxWidth().testTag("recently_viewed_empty"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
      ) {
        Row(
          modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          Icon(
            imageVector = Icons.Rounded.History,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(22.dp),
          )
          Text(
            text = "Apps you open and view inside their details page will appear here.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }
    } else {
      BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val cardWidth = ((maxWidth - 16.dp) / 3).coerceAtLeast(100.dp)
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth().testTag("recently_viewed_slider"),
        ) {
          items(items = recentlyViewedApps, key = { "recent_${it.id}" }) { app ->
            Card(
              modifier =
                Modifier
                  .width(cardWidth)
                  .clickable { onOpenDetail(app) }
                  .testTag("recently_viewed_card_${app.id}"),
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
              border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            ) {
              Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
              ) {
                AppIconTile(
                  iconText = app.iconText,
                  backgroundColor = app.color,
                  logoResId = app.logoResId,
                  iconVector = app.iconVector,
                  fillLogoBounds = app.fillLogoBounds,
                  contentDescription = "${app.name} logo",
                  size = 48.dp,
                  cornerRadius = 13.dp,
                )

                Text(
                  text = app.name,
                  style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.5.sp, lineHeight = 15.sp),
                  color = MaterialTheme.colorScheme.onSurface,
                  textAlign = TextAlign.Center,
                  maxLines = 2,
                  minLines = 2,
                  overflow = TextOverflow.Ellipsis,
                )

                Text(
                  text = app.rating,
                  style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.sp),
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  maxLines = 1,
                )

                Button(
                  onClick = { onDownloadClick(app) },
                  shape = RoundedCornerShape(999.dp),
                  contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                  modifier = Modifier.fillMaxWidth().height(30.dp),
                  colors =
                    ButtonDefaults.buttonColors(
                      containerColor = MaterialTheme.colorScheme.primary,
                      contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                ) {
                  Text(
                    text = stringResource(R.string.install_button),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, fontSize = 11.5.sp),
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
}

@Composable
private fun FavoritesEmptyCard() {
  Card(
    modifier = Modifier.fillMaxWidth().testTag("favorites_empty_card"),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
  ) {
    Column(
      modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 36.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      Icon(
        imageVector = Icons.Rounded.FavoriteBorder,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(36.dp),
      )
      Text(
        text = "No favorite apps yet",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
      )
      Text(
        text = "Open any app and tap the three-dots menu (⋮) → Favorite to save it here.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
      )
    }
  }
}

@Composable
private fun SearchSuggestionsCard(
  suggestions: List<SearchSuggestion>,
  onSuggestionClick: (String) -> Unit,
) {
  Card(
    modifier = Modifier.fillMaxWidth().testTag("search_suggestions_card"),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      suggestions.forEachIndexed { index, suggestion ->
        Row(
          modifier =
            Modifier
              .fillMaxWidth()
              .clickable { onSuggestionClick(suggestion.text) }
              .padding(horizontal = 16.dp, vertical = 13.dp)
              .testTag("search_suggestion_$index"),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          Icon(
            imageVector = Icons.Rounded.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp),
          )
          Text(
            text = suggestion.text,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
          )
          Text(
            text = suggestion.subtitle,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
          Icon(
            imageVector = Icons.Rounded.NorthWest,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp),
          )
        }
        if (index < suggestions.lastIndex) {
          HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f))
        }
      }
    }
  }
}

@Composable
private fun AppsCategoryChip(
  label: String,
  selected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Surface(
    shape = RoundedCornerShape(999.dp),
    color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
    border = if (selected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    modifier = modifier.clip(RoundedCornerShape(999.dp)).clickable(onClick = onClick),
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.labelLarge.copy(fontSize = 13.5.sp, fontWeight = FontWeight.Bold),
      color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
      modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp),
    )
  }
}
