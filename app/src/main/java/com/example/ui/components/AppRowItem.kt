package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.StoreApp

@Composable
fun AppRowItem(
  app: StoreApp,
  isPriorityMatch: Boolean = false,
  onOpenDetail: (StoreApp) -> Unit,
  onDownloadClick: (StoreApp) -> Unit,
  modifier: Modifier = Modifier,
) {
  val cleanRating = app.rating.replace("★", "").trim()

  Card(
    modifier = modifier.fillMaxWidth().testTag("app_row_${app.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth().clickable { onOpenDetail(app) }.padding(horizontal = 12.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      AppIconTile(
        iconText = app.iconText,
        backgroundColor = app.color,
        logoResId = app.logoResId,
        iconVector = app.iconVector,
        fillLogoBounds = app.fillLogoBounds,
        contentDescription = "${app.name} logo",
        size = 54.dp,
        cornerRadius = 14.dp,
      )

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = app.name,
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp),
          color = MaterialTheme.colorScheme.onSurface,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(1.dp))
        Text(
          text = if (isPriorityMatch) "${app.developer} • ${app.category}" else app.category,
          style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.5.sp),
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(3.dp))
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(3.dp),
        ) {
          Icon(
            imageVector = Icons.Rounded.Star,
            contentDescription = null,
            tint = Color(0xFF475467),
            modifier = Modifier.size(13.dp),
          )
          Text(
            text = "$cleanRating  •  ${app.sizeLabel}",
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
        }
      }

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
      ) {
        Button(
          onClick = { onDownloadClick(app) },
          shape = RoundedCornerShape(999.dp),
          contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
          colors =
            ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.primary,
              contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
          modifier = Modifier.height(34.dp).testTag("download_button_${app.id}"),
        ) {
          Text(
            text = stringResource(R.string.install_button),
            style = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp, fontWeight = FontWeight.Bold),
          )
        }

        Spacer(modifier = Modifier.width(2.dp))

        Icon(
          imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(20.dp),
        )
      }
    }
  }
}
