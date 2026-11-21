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
  Card(
    modifier = modifier.fillMaxWidth().testTag("app_row_${app.id}"),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth().clickable { onOpenDetail(app) }.padding(14.dp),
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
        size = 58.dp,
        cornerRadius = 15.dp,
      )

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = app.name,
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        Text(
          text = app.developer,
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.primary,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(1.dp))
        Text(
          text = app.category,
          style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.5.sp),
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          if (isPriorityMatch) {
            Surface(
              color = MaterialTheme.colorScheme.primaryContainer,
              shape = RoundedCornerShape(999.dp),
            ) {
              Text(
                text = "Match",
                style = MaterialTheme.typography.labelMedium.copy(fontSize = 10.5.sp),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
              )
            }
          }
          Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(999.dp),
          ) {
            Text(
              text = app.sourceLabel ?: "Google Play",
              style = MaterialTheme.typography.labelMedium.copy(fontSize = 10.5.sp),
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
            )
          }
        }
      }

      Button(
        onClick = { onDownloadClick(app) },
        shape = RoundedCornerShape(999.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        colors =
          ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
          ),
        modifier = Modifier.testTag("download_button_${app.id}"),
      ) {
        Text(
          text = stringResource(R.string.download_button),
          style = MaterialTheme.typography.labelLarge,
        )
      }
    }
  }
}
