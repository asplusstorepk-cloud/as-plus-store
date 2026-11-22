package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppScreenshotPreview

@Composable
fun AppScreenshotsRow(
  screenshots: List<AppScreenshotPreview>,
  onScreenshotClick: () -> Unit,
  isDetailSize: Boolean = false,
  contentPadding: PaddingValues = PaddingValues(0.dp),
  modifier: Modifier = Modifier,
) {
  if (screenshots.isEmpty()) return

  val cardWidth = if (isDetailSize) 186.dp else 156.dp
  val cardHeight = if (isDetailSize) 326.dp else 242.dp

  LazyRow(
    modifier = modifier.fillMaxWidth().testTag("app_screenshots_row"),
    contentPadding = contentPadding,
    horizontalArrangement = Arrangement.spacedBy(10.dp),
  ) {
    itemsIndexed(screenshots) { index, shot ->
      val itemWidth =
        if (shot.isLandscape) {
          if (isDetailSize) 284.dp else 224.dp
        } else {
          cardWidth
        }
      val itemHeight =
        if (shot.isLandscape) {
          if (isDetailSize) 160.dp else 128.dp
        } else {
          cardHeight
        }

      if (shot.imageResId != null) {
        Box(
          modifier =
            Modifier
              .width(itemWidth)
              .height(itemHeight)
              .shadow(elevation = 2.dp, shape = RoundedCornerShape(14.dp), clip = false)
              .clip(RoundedCornerShape(14.dp))
              .background(Color(0xFF0F1419))
              .border(1.dp, Color.Black.copy(alpha = 0.12f), RoundedCornerShape(14.dp))
              .clickable(onClick = onScreenshotClick)
              .testTag("screenshot_item_$index"),
        ) {
          Image(
            painter = painterResource(id = shot.imageResId),
            contentDescription = shot.caption.ifBlank { "App screenshot ${index + 1}" },
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
          )
        }
      } else {
        Column(
          modifier =
            Modifier
              .width(cardWidth)
              .height(cardHeight)
              .shadow(elevation = 2.dp, shape = RoundedCornerShape(14.dp), clip = false)
              .clip(RoundedCornerShape(14.dp))
              .background(shot.backgroundColor)
              .border(1.dp, Color.Black.copy(alpha = 0.08f), RoundedCornerShape(14.dp))
              .clickable(onClick = onScreenshotClick)
              .testTag("screenshot_item_$index"),
        ) {
          // Status Bar + App Header
          Column(
            modifier =
              Modifier
                .fillMaxWidth()
                .background(shot.topBarColor)
                .padding(horizontal = 8.dp, vertical = 5.dp),
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
            ) {
              Text(
                text = "9:41",
                color = shot.topBarTextColor.copy(alpha = 0.9f),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
              )
              Text(
                text = "5G • 100%",
                color = shot.topBarTextColor.copy(alpha = 0.9f),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
              )
            }
            Spacer(modifier = Modifier.height(3.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Text(
                text = shot.header,
                color = shot.topBarTextColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
              )
              Text(
                text = "⋮",
                color = shot.topBarTextColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
              )
            }
          }

          // App UI Mock Rows
          Column(
            modifier = Modifier.weight(1f).padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
          ) {
            shot.rows.forEach { row ->
              Surface(
                shape = RoundedCornerShape(9.dp),
                color = row.backgroundColor,
                border = row.borderColor?.let { BorderStroke(1.dp, it) },
                modifier = Modifier.fillMaxWidth(),
              ) {
                Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
                  Text(
                    text = row.title,
                    color = row.textColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                  )
                  Text(
                    text = row.subtitle,
                    color = row.textColor.copy(alpha = 0.8f),
                    fontSize = 9.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                  )
                }
              }
            }
          }

          // Screenshot Caption Footer
          HorizontalDivider(color = Color.Black.copy(alpha = 0.06f))
          Text(
            text = shot.caption,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier =
              Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.94f))
                .padding(horizontal = 8.dp, vertical = 6.dp),
          )
        }
      }
    }
  }
}
