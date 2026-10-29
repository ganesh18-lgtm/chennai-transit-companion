package com.urbantransit.offline.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object ChennaiColors {
    // Primary Backgrounds & Canvas
    val DeepCanvas = Color(0xFF090D16)
    val SurfaceCard = Color(0xFF131B2A)
    val SurfaceCardElevated = Color(0xFF1B2538)
    val SurfaceBorder = Color(0xFF26354D)

    // Chennai Metro Brand Accents
    val MetroBlue = Color(0xFF2563EB)
    val MetroBlueLight = Color(0xFF60A5FA)
    val MetroBlueGlow = Color(0xFF38BDF8)

    val MetroGreen = Color(0xFF059669)
    val MetroGreenLight = Color(0xFF34D399)
    val MetroGreenGlow = Color(0xFF10B981)

    // CMRL Phase 2 Corridors
    val MetroYellow = Color(0xFFEAB308) // Corridor 4 Poonamallee ↔ Vadapalani
    val MetroYellowLight = Color(0xFFFDE047)
    val MetroYellowGlow = Color(0xFFFEF08A)

    val MetroPurple = Color(0xFF9333EA) // Corridor 3 Madhavaram ↔ SIPCOT
    val MetroPurpleLight = Color(0xFFC084FC)

    val MetroOrange = Color(0xFFEA580C) // Corridor 5 Madhavaram ↔ Sholinganallur
    val MetroOrangeLight = Color(0xFFFB923C)

    // MTC Bus Brand Accents & EV Fleet
    val MtcRed = Color(0xFFDC2626)
    val MtcRedLight = Color(0xFFF87171)
    val MtcAmber = Color(0xFFF59E0B)
    val MtcPinkWomen = Color(0xFFEC4899) // Tamil Nadu women free travel pink (Vidiyal Payanam)
    val ElectricGreen = Color(0xFF10B981) // MTC Switch EV electric bus green
    val ElectricGreenLight = Color(0xFF6EE7B7)

    // State Colors
    val LiveOnlineGreen = Color(0xFF10B981)
    val OfflineAmber = Color(0xFFF59E0B)
    val AlertRed = Color(0xFFEF4444)

    // Glass & Modern Surface Accents
    val GlassSurface = Color(0xCC131B2A)
    val GlassBorder = Color(0x3360A5FA)
    val NeonCyan = Color(0xFF06B6D4)
    val NeonGold = Color(0xFFFBBF24)

    // Text & Content Hierarchy
    val TextPrimary = Color(0xFFF8FAFC)
    val TextSecondary = Color(0xFF94A3B8)
    val TextMuted = Color(0xFF64748B)

    // Gradients
    val MetroBlueGradient = Brush.horizontalGradient(listOf(Color(0xFF1D4ED8), Color(0xFF0284C7)))
    val MetroGreenGradient = Brush.horizontalGradient(listOf(Color(0xFF047857), Color(0xFF10B981)))
    val MetroYellowGradient = Brush.horizontalGradient(listOf(Color(0xFFCA8A04), Color(0xFFEAB308), Color(0xFFFACC15)))
    val MtcBusGradient = Brush.horizontalGradient(listOf(Color(0xFFB91C1C), Color(0xFFF97316)))
    val ElectricEvGradient = Brush.horizontalGradient(listOf(Color(0xFF047857), Color(0xFF059669), Color(0xFF10B981)))
    val GoldenPassGradient = Brush.linearGradient(listOf(Color(0xFFD97706), Color(0xFFFBBF24)))
    val CardGlowGradient = Brush.verticalGradient(listOf(Color(0xFF1E293B), Color(0xFF0F172A)))
    val HoloRainbowGradient = Brush.linearGradient(listOf(Color(0xFF38BDF8), Color(0xFFFACC15), Color(0xFFEC4899), Color(0xFF34D399)))
    val GlassCardBrush = Brush.verticalGradient(listOf(Color(0xFF1E293B).copy(alpha = 0.85f), Color(0xFF0F172A).copy(alpha = 0.95f)))
}

private val DarkColorScheme = darkColorScheme(
    primary = ChennaiColors.MetroBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1E3A8A),
    onPrimaryContainer = Color(0xFFDBEAFE),
    secondary = ChennaiColors.MetroGreen,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF064E3B),
    onSecondaryContainer = Color(0xFFD1FAE5),
    tertiary = ChennaiColors.MtcRed,
    background = ChennaiColors.DeepCanvas,
    surface = ChennaiColors.SurfaceCard,
    surfaceVariant = ChennaiColors.SurfaceCardElevated,
    onBackground = ChennaiColors.TextPrimary,
    onSurface = ChennaiColors.TextPrimary,
    onSurfaceVariant = ChennaiColors.TextSecondary,
    outline = ChennaiColors.SurfaceBorder
)

@Composable
fun ChennaiTransitTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
