package com.example.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.StoreBadgeRed
import com.example.ui.theme.StoreBlue
import com.example.ui.theme.StoreBlueBright
import com.example.ui.theme.StoreBlueDeep
import com.example.ui.theme.StoreBlueMid
import com.example.ui.theme.StoreGold
import com.example.ui.theme.StoreSuccessGreen

@Composable
fun AppIconTile(
  iconText: String,
  backgroundColor: Color,
  @DrawableRes logoResId: Int? = null,
  iconVector: androidx.compose.ui.graphics.vector.ImageVector? = null,
  fillLogoBounds: Boolean = false,
  contentDescription: String? = null,
  size: Dp = 56.dp,
  cornerRadius: Dp = 14.dp,
  fontSize: TextUnit = 21.sp,
  modifier: Modifier = Modifier,
) {
  val tileBg = if (logoResId != null) Color.White else backgroundColor
  val shape = RoundedCornerShape(cornerRadius)

  Box(
    modifier =
      modifier
        .size(size)
        .shadow(elevation = 2.dp, shape = shape, clip = false)
        .clip(shape)
        .background(tileBg)
        .then(
          if (logoResId != null) {
            Modifier.border(1.dp, MaterialTheme.colorScheme.outline, shape)
          } else {
            Modifier
          }
        ),
    contentAlignment = Alignment.Center,
  ) {
    when {
      logoResId != null -> {
        Image(
          painter = painterResource(id = logoResId),
          contentDescription = contentDescription,
          contentScale = if (fillLogoBounds) ContentScale.Crop else ContentScale.Fit,
          modifier =
            if (fillLogoBounds) Modifier.fillMaxSize()
            else Modifier.fillMaxSize().padding((size.value * 0.14f).dp),
        )
      }
      iconVector != null -> {
        Icon(
          imageVector = iconVector,
          contentDescription = contentDescription,
          tint = Color.White,
          modifier = Modifier.size(size * 0.56f),
        )
      }
      else -> {
        Text(
          text = iconText,
          color = Color.White,
          fontWeight = FontWeight.Bold,
          fontSize = fontSize,
        )
      }
    }
  }
}

@Composable
fun DownloadStatusBadge(
  activeDownloads: Int,
  completedDownloads: Int,
  onClick: () -> Unit,
  tintColor: Color = MaterialTheme.colorScheme.onSurface,
  modifier: Modifier = Modifier,
) {
  val isDownloading = activeDownloads > 0
  val isCompleted = !isDownloading && completedDownloads > 0
  val badgeCount = if (isDownloading) activeDownloads else completedDownloads

  Box(
    modifier =
      modifier
        .minimumInteractiveComponentSize()
        .clip(CircleShape)
        .clickable(role = Role.Button, onClick = onClick)
        .padding(8.dp)
        .testTag("download_status_button"),
    contentAlignment = Alignment.Center,
  ) {
    Icon(
      imageVector = if (isCompleted) Icons.Rounded.Check else Icons.Rounded.ArrowDownward,
      contentDescription = stringResource(R.string.cd_downloads),
      tint = if (isCompleted) StoreSuccessGreen else tintColor,
      modifier = Modifier.size(22.dp),
    )

    Box(
      modifier =
        Modifier
          .align(Alignment.TopEnd)
          .offset(x = 5.dp, y = (-4).dp)
          .size(17.dp)
          .clip(CircleShape)
          .background(StoreBadgeRed),
      contentAlignment = Alignment.Center,
    ) {
      Text(
        text = badgeCount.toString(),
        color = Color.White,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 10.sp,
      )
    }
  }
}

@Composable
fun NotificationBellBadge(
  count: Int,
  onClick: () -> Unit,
  tintColor: Color = Color.White,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier =
      modifier
        .minimumInteractiveComponentSize()
        .clip(CircleShape)
        .clickable(role = Role.Button, onClick = onClick)
        .padding(8.dp)
        .testTag("notifications_button"),
    contentAlignment = Alignment.Center,
  ) {
    Icon(
      imageVector = Icons.Outlined.Notifications,
      contentDescription = stringResource(R.string.cd_notifications),
      tint = tintColor,
      modifier = Modifier.size(24.dp),
    )

    if (count > 0) {
      Box(
        modifier =
          Modifier
            .align(Alignment.TopEnd)
            .offset(x = 4.dp, y = (-3).dp)
            .size(18.dp)
            .clip(CircleShape)
            .background(StoreBadgeRed)
            .border(1.dp, Color.White.copy(alpha = 0.7f), CircleShape),
        contentAlignment = Alignment.Center,
      ) {
        Text(
          text = count.toString(),
          color = Color.White,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          lineHeight = 10.sp,
        )
      }
    }
  }
}

@Composable
fun StoreDashboardHeader(
  searchQuery: String,
  notificationCount: Int,
  searchFocusRequester: FocusRequester,
  onMenuClick: () -> Unit,
  onNotificationsClick: () -> Unit,
  onProfileClick: () -> Unit,
  onSearchQueryChange: (String) -> Unit,
  onSubmitSearch: (String) -> Unit,
  onClearSearch: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val keyboardController = LocalSoftwareKeyboardController.current

  Box(
    modifier =
      modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(bottomStart = 22.dp, bottomEnd = 22.dp))
        .background(
          Brush.verticalGradient(
            colors = listOf(StoreBlueDeep, StoreBlueMid, StoreBlueBright),
          ),
        )
        .statusBarsPadding()
        .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 14.dp)
        .testTag("home_dashboard_header"),
  ) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      // Top Navigation Row: 3-line Menu + Crown Logo + Tagline + Bell + Profile
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Row(
          modifier = Modifier.weight(1f),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          IconButton(
            onClick = onMenuClick,
            modifier = Modifier.size(48.dp).testTag("menu_button"),
          ) {
            Icon(
              imageVector = Icons.Rounded.Menu,
              contentDescription = stringResource(R.string.cd_menu),
              tint = Color.White,
              modifier = Modifier.size(26.dp),
            )
          }

          AsPlusCrownLogo(size = 44.dp)

          Spacer(modifier = Modifier.width(6.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text =
                buildAnnotatedString {
                  withStyle(SpanStyle(color = Color.White, fontWeight = FontWeight.ExtraBold)) {
                    append("AS ")
                  }
                  withStyle(
                    SpanStyle(
                      color = StoreGold,
                      fontWeight = FontWeight.ExtraBold,
                      fontStyle = FontStyle.Italic,
                    ),
                  ) {
                    append("Plus Store")
                  }
                },
              fontSize = 18.sp,
              lineHeight = 20.sp,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
            Text(
              text = stringResource(R.string.store_tagline),
              color = Color.White.copy(alpha = 0.88f),
              fontSize = 10.5.sp,
              fontWeight = FontWeight.Medium,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
          }
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
          NotificationBellBadge(
            count = notificationCount,
            onClick = onNotificationsClick,
            tintColor = Color.White,
          )

          Box(
            modifier =
              Modifier
                .minimumInteractiveComponentSize()
                .clip(CircleShape)
                .clickable(role = Role.Button, onClick = onProfileClick)
                .padding(4.dp)
                .testTag("profile_button"),
            contentAlignment = Alignment.Center,
          ) {
            Box(
              modifier =
                Modifier
                  .size(34.dp)
                  .clip(CircleShape)
                  .background(
                    Brush.verticalGradient(
                      colors = listOf(Color(0xFF4BA3FF), Color(0xFF1565F5)),
                    ),
                  )
                  .border(1.5.dp, Color.White.copy(alpha = 0.65f), CircleShape),
              contentAlignment = Alignment.Center,
            ) {
              Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = stringResource(R.string.cd_profile),
                tint = Color.White,
                modifier = Modifier.size(20.dp),
              )
            }
          }
        }
      }

      // Pill Search Bar inside Royal Blue Dashboard Header
      PillSearchBar(
        query = searchQuery,
        focusRequester = searchFocusRequester,
        onQueryChange = onSearchQueryChange,
        onSubmit = {
          keyboardController?.hide()
          onSubmitSearch(searchQuery)
        },
        onClear = onClearSearch,
      )
    }
  }
}

@Composable
fun PillSearchBar(
  query: String,
  focusRequester: FocusRequester,
  onQueryChange: (String) -> Unit,
  onSubmit: () -> Unit,
  onClear: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier.fillMaxWidth().height(46.dp),
    shape = RoundedCornerShape(999.dp),
    color = Color.White,
    shadowElevation = 4.dp,
  ) {
    Row(
      modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      Icon(
        imageVector = Icons.Rounded.Search,
        contentDescription = null,
        tint = Color(0xFF3C4557),
        modifier = Modifier.size(20.dp),
      )

      Box(
        modifier = Modifier.weight(1f),
        contentAlignment = Alignment.CenterStart,
      ) {
        if (query.isEmpty()) {
          Text(
            text = stringResource(R.string.search_placeholder),
            color = Color(0xFF7B8494),
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
        }
        BasicTextField(
          value = query,
          onValueChange = onQueryChange,
          singleLine = true,
          textStyle =
            MaterialTheme.typography.bodyLarge.copy(
              color = Color(0xFF141B29),
              fontSize = 14.sp,
            ),
          cursorBrush = SolidColor(StoreBlue),
          keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
          keyboardActions = KeyboardActions(onSearch = { onSubmit() }),
          modifier = Modifier.fillMaxWidth().focusRequester(focusRequester).testTag("search_input"),
        )
      }

      if (query.isNotEmpty()) {
        IconButton(
          onClick = onClear,
          modifier = Modifier.size(32.dp).testTag("search_clear_button"),
        ) {
          Icon(
            imageVector = Icons.Rounded.Close,
            contentDescription = "Clear search",
            tint = Color(0xFF5F697D),
            modifier = Modifier.size(18.dp),
          )
        }
      } else {
        IconButton(
          onClick = onSubmit,
          modifier = Modifier.size(32.dp).testTag("search_submit_button"),
        ) {
          Icon(
            imageVector = Icons.Rounded.Mic,
            contentDescription = stringResource(R.string.search_button),
            tint = Color(0xFF283040),
            modifier = Modifier.size(20.dp),
          )
        }
      }
    }
  }
}

@Composable
fun CleanSectionTopBar(
  title: String,
  showSearchIcon: Boolean,
  isSearchExpanded: Boolean,
  searchQuery: String,
  searchFocusRequester: FocusRequester,
  onBackClick: () -> Unit,
  onToggleSearch: () -> Unit,
  onSearchQueryChange: (String) -> Unit,
  onSubmitSearch: (String) -> Unit,
  onClearSearch: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val keyboardController = LocalSoftwareKeyboardController.current

  Surface(
    color = MaterialTheme.colorScheme.surface,
    modifier = modifier.fillMaxWidth(),
  ) {
    Column(modifier = Modifier.fillMaxWidth().statusBarsPadding()) {
      Box(
        modifier = Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 4.dp),
      ) {
        IconButton(
          onClick = onBackClick,
          modifier = Modifier.align(Alignment.CenterStart).size(48.dp).testTag("section_back_button"),
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
            contentDescription = stringResource(R.string.cd_back),
            tint = MaterialTheme.colorScheme.onSurface,
          )
        }

        Text(
          text = title,
          style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 19.sp),
          color = MaterialTheme.colorScheme.onSurface,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          modifier = Modifier.align(Alignment.Center),
        )

        if (showSearchIcon) {
          IconButton(
            onClick = onToggleSearch,
            modifier = Modifier.align(Alignment.CenterEnd).size(48.dp).testTag("all_apps_search_icon"),
          ) {
            Icon(
              imageVector = if (isSearchExpanded) Icons.Rounded.Close else Icons.Rounded.Search,
              contentDescription = stringResource(R.string.cd_search),
              tint = MaterialTheme.colorScheme.onSurface,
            )
          }
        }
      }

      if (isSearchExpanded) {
        Box(modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, bottom = 10.dp)) {
          PillSearchBar(
            query = searchQuery,
            focusRequester = searchFocusRequester,
            onQueryChange = onSearchQueryChange,
            onSubmit = {
              keyboardController?.hide()
              onSubmitSearch(searchQuery)
            },
            onClear = onClearSearch,
          )
        }
      }

      HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f))
    }
  }
}
