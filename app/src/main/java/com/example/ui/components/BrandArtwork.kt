package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AsPlusCrownLogo(
  size: Dp = 44.dp,
  modifier: Modifier = Modifier,
) {
  val textMeasurer = rememberTextMeasurer()
  val goldBrush =
    remember {
      Brush.linearGradient(
        colors = listOf(Color(0xFFFFF1A8), Color(0xFFFFCA28), Color(0xFFF59E0B)),
      )
    }
  val silverBrush =
    remember {
      Brush.verticalGradient(
        colors = listOf(Color(0xFFFFFFFF), Color(0xFFE4ECFA), Color(0xFFBBD0F5)),
      )
    }

  Canvas(modifier = modifier.size(size)) {
    val w = this.size.width
    val h = this.size.height

    // Subtle radial glow behind emblem
    drawCircle(
      brush =
        Brush.radialGradient(
          colors = listOf(Color(0x664DA3FF), Color.Transparent),
          center = Offset(w * 0.52f, h * 0.58f),
          radius = w * 0.52f,
        ),
      radius = w * 0.52f,
      center = Offset(w * 0.52f, h * 0.58f),
    )

    // Crown (5-point tilted slightly left)
    rotate(degrees = -5f, pivot = Offset(w * 0.52f, h * 0.28f)) {
      val crownPath =
        Path().apply {
          moveTo(w * 0.30f, h * 0.35f)
          lineTo(w * 0.25f, h * 0.17f)
          lineTo(w * 0.39f, h * 0.26f)
          lineTo(w * 0.51f, h * 0.11f)
          lineTo(w * 0.63f, h * 0.26f)
          lineTo(w * 0.77f, h * 0.17f)
          lineTo(w * 0.72f, h * 0.35f)
          close()
        }
      drawPath(crownPath, brush = goldBrush)
      drawPath(
        crownPath,
        color = Color(0xFFB45309),
        style = Stroke(width = w * 0.018f, join = StrokeJoin.Round),
      )
      // Base band of crown
      drawRoundRect(
        brush = goldBrush,
        topLeft = Offset(w * 0.29f, h * 0.335f),
        size = Size(w * 0.44f, h * 0.052f),
        cornerRadius = CornerRadius(w * 0.03f, w * 0.03f),
      )
      // Crown pearl tips
      val tipRadius = w * 0.028f
      drawCircle(Color(0xFFFFF3B0), radius = tipRadius, center = Offset(w * 0.25f, h * 0.165f))
      drawCircle(Color(0xFFFFD54F), radius = tipRadius * 1.08f, center = Offset(w * 0.39f, h * 0.255f))
      drawCircle(Color(0xFFFFF59D), radius = tipRadius * 1.25f, center = Offset(w * 0.51f, h * 0.105f))
      drawCircle(Color(0xFFFFD54F), radius = tipRadius * 1.08f, center = Offset(w * 0.63f, h * 0.255f))
      drawCircle(Color(0xFFFFF3B0), radius = tipRadius, center = Offset(w * 0.77f, h * 0.165f))
    }

    // Bold "AS" Monogram
    val fontSizeSp = (w * 0.42f).toSp()
    val asLayout =
      textMeasurer.measure(
        text = "AS",
        style =
          TextStyle(
            brush = silverBrush,
            fontSize = fontSizeSp,
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic,
            letterSpacing = (-1.5).sp,
          ),
      )
    drawText(
      textLayoutResult = asLayout,
      topLeft =
        Offset(
          x = w * 0.53f - asLayout.size.width / 2f,
          y = h * 0.34f,
        ),
    )

    // Sweeping Gold Crescent Swoosh wrapping left & bottom of "AS"
    val swoosh =
      Path().apply {
        moveTo(w * 0.48f, h * 0.36f)
        cubicTo(
          w * 0.12f,
          h * 0.48f,
          w * 0.03f,
          h * 0.80f,
          w * 0.32f,
          h * 0.89f,
        )
        cubicTo(
          w * 0.54f,
          h * 0.95f,
          w * 0.78f,
          h * 0.84f,
          w * 0.95f,
          h * 0.66f,
        )
        cubicTo(
          w * 0.72f,
          h * 0.80f,
          w * 0.48f,
          h * 0.85f,
          w * 0.32f,
          h * 0.79f,
        )
        cubicTo(
          w * 0.16f,
          h * 0.73f,
          w * 0.20f,
          h * 0.48f,
          w * 0.48f,
          h * 0.36f,
        )
        close()
      }
    drawPath(path = swoosh, brush = goldBrush)
  }
}

@Composable
fun VipCrownIcon(
  size: Dp = 20.dp,
  modifier: Modifier = Modifier,
) {
  Canvas(modifier = modifier.size(size)) {
    val w = this.size.width
    val h = this.size.height
    val path =
      Path().apply {
        moveTo(w * 0.14f, h * 0.76f)
        lineTo(w * 0.08f, h * 0.32f)
        lineTo(w * 0.32f, h * 0.52f)
        lineTo(w * 0.50f, h * 0.18f)
        lineTo(w * 0.68f, h * 0.52f)
        lineTo(w * 0.92f, h * 0.32f)
        lineTo(w * 0.86f, h * 0.76f)
        close()
      }
    drawPath(
      path = path,
      brush = Brush.verticalGradient(listOf(Color(0xFFFFF176), Color(0xFFFFB300))),
    )
    drawRoundRect(
      color = Color(0xFFFFD54F),
      topLeft = Offset(w * 0.14f, h * 0.79f),
      size = Size(w * 0.72f, h * 0.11f),
      cornerRadius = CornerRadius(w * 0.05f, w * 0.05f),
    )
  }
}

@Composable
fun HeroPhoneIllustration(
  modifier: Modifier = Modifier,
) {
  Canvas(modifier = modifier.size(width = 132.dp, height = 136.dp)) {
    val w = this.size.width
    val h = this.size.height

    // Neon cyan & magenta radial aura
    drawCircle(
      brush =
        Brush.radialGradient(
          colors = listOf(Color(0x8800E5FF), Color(0x44D500F9), Color.Transparent),
          center = Offset(w * 0.56f, h * 0.54f),
          radius = w * 0.58f,
        ),
      radius = w * 0.58f,
      center = Offset(w * 0.56f, h * 0.54f),
    )

    // Tilted futuristic phone frame
    rotate(degrees = 14f, pivot = Offset(w * 0.56f, h * 0.54f)) {
      drawRoundRect(
        brush = Brush.linearGradient(listOf(Color(0xFF00E5FF), Color(0xFF8E24AA), Color(0xFFFF4081))),
        topLeft = Offset(w * 0.25f, h * 0.08f),
        size = Size(w * 0.56f, h * 0.84f),
        cornerRadius = CornerRadius(w * 0.10f, w * 0.10f),
      )
      drawRoundRect(
        brush = Brush.verticalGradient(listOf(Color(0xFF102A83), Color(0xFF07113A))),
        topLeft = Offset(w * 0.275f, h * 0.105f),
        size = Size(w * 0.51f, h * 0.79f),
        cornerRadius = CornerRadius(w * 0.08f, w * 0.08f),
      )
      // Dynamic island notch
      drawRoundRect(
        color = Color(0xFF050B24),
        topLeft = Offset(w * 0.44f, h * 0.135f),
        size = Size(w * 0.18f, h * 0.032f),
        cornerRadius = CornerRadius(w * 0.02f, w * 0.02f),
      )
    }

    // Floating colorful app tiles matching mockup (Play, Music, Settings, Games)
    // 1. Blue Video Play tile
    rotate(degrees = -12f, pivot = Offset(w * 0.32f, h * 0.40f)) {
      drawRoundRect(
        brush = Brush.linearGradient(listOf(Color(0xFF4FC3F7), Color(0xFF1565F5))),
        topLeft = Offset(w * 0.14f, h * 0.26f),
        size = Size(w * 0.28f, w * 0.28f),
        cornerRadius = CornerRadius(w * 0.07f, w * 0.07f),
      )
      val playTri =
        Path().apply {
          moveTo(w * 0.24f, h * 0.34f)
          lineTo(w * 0.35f, h * 0.40f)
          lineTo(w * 0.24f, h * 0.46f)
          close()
        }
      drawPath(playTri, color = Color.White)
    }

    // 2. Coral/Red Music tile
    rotate(degrees = 10f, pivot = Offset(w * 0.72f, h * 0.31f)) {
      drawRoundRect(
        brush = Brush.linearGradient(listOf(Color(0xFFFF7043), Color(0xFFE53935))),
        topLeft = Offset(w * 0.57f, h * 0.17f),
        size = Size(w * 0.28f, w * 0.28f),
        cornerRadius = CornerRadius(w * 0.07f, w * 0.07f),
      )
      drawCircle(Color.White, radius = w * 0.038f, center = Offset(w * 0.67f, h * 0.35f))
      drawLine(
        color = Color.White,
        start = Offset(w * 0.70f, h * 0.35f),
        end = Offset(w * 0.70f, h * 0.24f),
        strokeWidth = w * 0.028f,
        cap = StrokeCap.Round,
      )
      drawLine(
        color = Color.White,
        start = Offset(w * 0.70f, h * 0.24f),
        end = Offset(w * 0.77f, h * 0.26f),
        strokeWidth = w * 0.028f,
        cap = StrokeCap.Round,
      )
    }

    // 3. Green Tools/Settings tile
    rotate(degrees = -6f, pivot = Offset(w * 0.52f, h * 0.64f)) {
      drawRoundRect(
        brush = Brush.linearGradient(listOf(Color(0xFF4ADE80), Color(0xFF00B248))),
        topLeft = Offset(w * 0.38f, h * 0.50f),
        size = Size(w * 0.27f, w * 0.27f),
        cornerRadius = CornerRadius(w * 0.07f, w * 0.07f),
      )
      drawCircle(
        color = Color.White,
        radius = w * 0.068f,
        center = Offset(w * 0.515f, h * 0.635f),
        style = Stroke(width = w * 0.032f),
      )
    }

    // 4. Purple Gamepad tile
    rotate(degrees = 12f, pivot = Offset(w * 0.80f, h * 0.58f)) {
      drawRoundRect(
        brush = Brush.linearGradient(listOf(Color(0xFF7C4DFF), Color(0xFF3949AB))),
        topLeft = Offset(w * 0.66f, h * 0.45f),
        size = Size(w * 0.26f, w * 0.26f),
        cornerRadius = CornerRadius(w * 0.065f, w * 0.065f),
      )
      drawRoundRect(
        color = Color.White,
        topLeft = Offset(w * 0.71f, h * 0.545f),
        size = Size(w * 0.16f, h * 0.085f),
        cornerRadius = CornerRadius(w * 0.04f, w * 0.04f),
      )
    }

    // Sparkling neon dots
    drawCircle(Color(0xFFFFD54F), radius = w * 0.018f, center = Offset(w * 0.12f, h * 0.68f))
    drawCircle(Color(0xFF00E5FF), radius = w * 0.020f, center = Offset(w * 0.24f, h * 0.78f))
    drawCircle(Color(0xFFFFFFFF), radius = w * 0.014f, center = Offset(w * 0.88f, h * 0.20f))
    drawCircle(Color(0xFFFF80AB), radius = w * 0.018f, center = Offset(w * 0.90f, h * 0.78f))
  }
}

@Composable
fun VipShieldEmblem(
  modifier: Modifier = Modifier,
) {
  val textMeasurer = rememberTextMeasurer()
  val goldGradient =
    remember {
      Brush.linearGradient(
        colors = listOf(Color(0xFFFFF59D), Color(0xFFFFCA28), Color(0xFFD97706)),
      )
    }

  Canvas(modifier = modifier.size(width = 108.dp, height = 94.dp)) {
    val w = this.size.width
    val h = this.size.height

    // Soft golden radial glow
    drawCircle(
      brush =
        Brush.radialGradient(
          colors = listOf(Color(0x77FFD54F), Color.Transparent),
          center = Offset(w * 0.50f, h * 0.54f),
          radius = w * 0.48f,
        ),
      radius = w * 0.48f,
      center = Offset(w * 0.50f, h * 0.54f),
    )

    // Laurel wreath arcs left & right
    drawArc(
      brush = goldGradient,
      startAngle = 115f,
      sweepAngle = 120f,
      useCenter = false,
      topLeft = Offset(w * 0.10f, h * 0.22f),
      size = Size(w * 0.80f, h * 0.68f),
      style = Stroke(width = w * 0.048f, cap = StrokeCap.Round),
    )
    drawArc(
      brush = goldGradient,
      startAngle = -55f,
      sweepAngle = 120f,
      useCenter = false,
      topLeft = Offset(w * 0.10f, h * 0.22f),
      size = Size(w * 0.80f, h * 0.68f),
      style = Stroke(width = w * 0.048f, cap = StrokeCap.Round),
    )

    // Top tiny crown above shield
    val crownPath =
      Path().apply {
        moveTo(w * 0.37f, h * 0.24f)
        lineTo(w * 0.34f, h * 0.10f)
        lineTo(w * 0.44f, h * 0.17f)
        lineTo(w * 0.50f, h * 0.06f)
        lineTo(w * 0.56f, h * 0.17f)
        lineTo(w * 0.66f, h * 0.10f)
        lineTo(w * 0.63f, h * 0.24f)
        close()
      }
    drawPath(crownPath, brush = goldGradient)

    // Shield outer gold crest
    val shieldOuter =
      Path().apply {
        moveTo(w * 0.50f, h * 0.22f)
        lineTo(w * 0.78f, h * 0.31f)
        lineTo(w * 0.74f, h * 0.63f)
        quadraticTo(w * 0.66f, h * 0.80f, w * 0.50f, h * 0.88f)
        quadraticTo(w * 0.34f, h * 0.80f, w * 0.26f, h * 0.63f)
        lineTo(w * 0.22f, h * 0.31f)
        close()
      }
    drawPath(shieldOuter, brush = goldGradient)

    // Shield inner dark royal crest
    val shieldInner =
      Path().apply {
        moveTo(w * 0.50f, h * 0.27f)
        lineTo(w * 0.73f, h * 0.35f)
        lineTo(w * 0.70f, h * 0.61f)
        quadraticTo(w * 0.63f, h * 0.75f, w * 0.50f, h * 0.82f)
        quadraticTo(w * 0.37f, h * 0.75f, w * 0.30f, h * 0.61f)
        lineTo(w * 0.27f, h * 0.35f)
        close()
      }
    drawPath(
      shieldInner,
      brush = Brush.verticalGradient(listOf(Color(0xFF4A2C00), Color(0xFF211200))),
    )

    // "VIP" gold text centered inside shield
    val vipLayout =
      textMeasurer.measure(
        text = "VIP",
        style =
          TextStyle(
            brush = goldGradient,
            fontSize = (w * 0.20f).toSp(),
            fontWeight = FontWeight.Black,
            letterSpacing = 0.8.sp,
          ),
      )
    drawText(
      textLayoutResult = vipLayout,
      topLeft =
        Offset(
          x = w * 0.50f - vipLayout.size.width / 2f,
          y = h * 0.53f - vipLayout.size.height / 2f,
        ),
    )
  }
}
