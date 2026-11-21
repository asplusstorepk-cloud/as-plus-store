package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.StoreApp
import com.example.ui.theme.StoreBlue
import com.example.ui.theme.StoreBlueDark

@Composable
fun FeaturedAppCard(
  app: StoreApp,
  onOpenDetail: (StoreApp) -> Unit,
  onDownloadClick: (StoreApp) -> Unit,
  modifier: Modifier = Modifier,
) {
  Card(
    modifier = modifier.fillMaxWidth().testTag("featured_app_card"),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
  ) {
    Column {
      Box(
        modifier =
          Modifier
            .fillMaxWidth()
            .height(185.dp)
            .background(
              Brush.linearGradient(
                colors = listOf(StoreBlue, StoreBlueDark, Color(0xFF1565C0)),
              )
            )
            .clickable { onOpenDetail(app) },
      ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
          drawCircle(
            color = Color.White.copy(alpha = 0.09f),
            radius = size.height * 0.65f,
            center = Offset(size.width * 0.88f, size.height * 0.22f),
          )
          drawCircle(
            color = Color.White.copy(alpha = 0.06f),
            radius = size.height * 0.42f,
            center = Offset(size.width * 0.72f, size.height * 0.78f),
          )
        }

        Column(
          modifier = Modifier.fillMaxSize().padding(18.dp),
          verticalArrangement = Arrangement.Bottom,
        ) {
          Surface(
            color = Color.White.copy(alpha = 0.2f),
            shape = RoundedCornerShape(999.dp),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
          ) {
            Text(
              text = "Featured • ${app.version}",
              style = MaterialTheme.typography.labelMedium,
              color = Color.White,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            )
          }
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = app.heroTitle,
            style =
              MaterialTheme.typography.headlineSmall.copy(
                color = Color.White,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                shadow = Shadow(color = Color.Black.copy(alpha = 0.35f), blurRadius = 10f),
              ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
          )
        }
      }

      Row(
        modifier =
          Modifier
            .fillMaxWidth()
            .clickable { onOpenDetail(app) }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Row(
          modifier = Modifier.weight(1f),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          AppIconTile(
            iconText = app.iconText,
            backgroundColor = app.color,
            logoResId = app.logoResId,
            iconVector = app.iconVector,
            fillLogoBounds = app.fillLogoBounds,
            contentDescription = "${app.name} logo",
            size = 56.dp,
            cornerRadius = 14.dp,
          )

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = app.name,
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "${app.developer} • ${app.version} • ${app.sizeLabel}",
              style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.5.sp),
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
          }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Button(
          onClick = { onDownloadClick(app) },
          shape = RoundedCornerShape(999.dp),
          contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp),
          colors =
            ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.primary,
              contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
          modifier = Modifier.testTag("featured_download_button"),
        ) {
          Text(
            text = stringResource(R.string.download_button),
            style = MaterialTheme.typography.labelLarge,
          )
        }
      }
    }
  }
}
