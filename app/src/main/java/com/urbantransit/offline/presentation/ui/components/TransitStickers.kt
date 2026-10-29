package com.urbantransit.offline.presentation.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urbantransit.offline.presentation.ui.theme.ChennaiColors
import kotlinx.coroutines.launch

/**
 * Expressive tactile sticker with micro-tilt, holographic sheen, and spring peel feedback.
 */
@Composable
fun TransitSticker(
    emoji: String? = null,
    icon: ImageVector? = null,
    title: String,
    subtitle: String? = null,
    primaryColor: Color,
    accentGlow: Color = primaryColor.copy(alpha = 0.5f),
    gradientBrush: Brush? = null,
    rotationDeg: Float = 0f,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }
    val tilt = remember { Animatable(rotationDeg) }

    val infiniteTransition = rememberInfiniteTransition(label = "stickerSheen")
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -0.5f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(3600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFF131B2A),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.2.dp,
            brush = gradientBrush ?: Brush.linearGradient(
                listOf(primaryColor, accentGlow, primaryColor.copy(alpha = 0.3f))
            )
        ),
        shadowElevation = 4.dp,
        modifier = modifier
            .rotate(tilt.value)
            .scale(scale.value)
            .clip(RoundedCornerShape(14.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                coroutineScope.launch {
                    scale.animateTo(0.92f, spring(dampingRatio = 0.4f))
                    scale.animateTo(1f, spring(dampingRatio = 0.5f))
                }
                onClick?.invoke()
            }
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            primaryColor.copy(alpha = 0.16f),
                            Color.White.copy(alpha = 0.10f),
                            primaryColor.copy(alpha = 0.16f)
                        ),
                        startX = shimmerOffset * 200f,
                        endX = (shimmerOffset + 0.5f) * 200f
                    )
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (emoji != null) {
                    Text(text = emoji, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(7.dp))
                } else if (icon != null) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(primaryColor.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = primaryColor,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(7.dp))
                }

                Column {
                    Text(
                        text = title,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = 0.4.sp
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium,
                            color = primaryColor
                        )
                    }
                }
            }
        }
    }
}

/**
 * Interactive sticker ribbon displaying real-time city transit badges, welfare schemes, and status chips.
 */
@Composable
fun TransitStickersRibbon(
    onSelectSticker: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(ChennaiColors.NeonCyan, CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "LIVE CHENNAI TRANSIT BADGES & PERKS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ChennaiColors.TextSecondary,
                    letterSpacing = 1.1.sp
                )
            }
            Text(
                text = "Tap to explore",
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = ChennaiColors.TextMuted
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 1. Vidiyal Payanam 100% Free Pass Sticker
            TransitSticker(
                emoji = "🌸",
                title = "விடியல் பயணம்",
                subtitle = "100% Free Women Commute",
                primaryColor = ChennaiColors.MtcPinkWomen,
                accentGlow = Color(0xFFF472B6),
                rotationDeg = -1f,
                onClick = { onSelectSticker("vidiyal_payanam") }
            )

            // 2. CMRL Phase 2 Yellow Line Active
            TransitSticker(
                icon = Icons.Default.Bolt,
                title = "PHASE 2 LINE 4 OPEN",
                subtitle = "Poonamallee ↔ Vadapalani (15km)",
                primaryColor = ChennaiColors.MetroYellow,
                accentGlow = ChennaiColors.MetroYellowLight,
                rotationDeg = 1f,
                onClick = { onSelectSticker("phase2_yellow") }
            )

            // 3. Singara Chennai NCMC 20% Discount
            TransitSticker(
                icon = Icons.Default.CreditCard,
                title = "SINGARA CHENNAI NCMC",
                subtitle = "20% Metro Off • MTC ETM Ready",
                primaryColor = ChennaiColors.NeonCyan,
                accentGlow = ChennaiColors.MetroBlueGlow,
                rotationDeg = -0.5f,
                onClick = { onSelectSticker("singara_ncmc") }
            )

            // 4. Switch EV Electric Bus Fleet
            TransitSticker(
                icon = Icons.Default.ElectricBolt,
                title = "SWITCH EV FLEET",
                subtitle = "Zero Emission Low-Floor MTC",
                primaryColor = ChennaiColors.ElectricGreen,
                accentGlow = ChennaiColors.ElectricGreenLight,
                rotationDeg = 1.2f,
                onClick = { onSelectSticker("electric_bus") }
            )

            // 5. Offline GPS 0ms Sync
            TransitSticker(
                icon = Icons.Default.Sensors,
                title = "OFFLINE 0ms TIMETABLE",
                subtitle = "No Internet Required",
                primaryColor = Color(0xFFA855F7),
                accentGlow = Color(0xFFC084FC),
                rotationDeg = -1f,
                onClick = { onSelectSticker("offline_sync") }
            )

            // 6. Crowd Status Indicator
            TransitSticker(
                icon = Icons.Default.Groups,
                title = "CROWD: MODERATE",
                subtitle = "Normal Boarding Frequencies",
                primaryColor = Color(0xFF10B981),
                accentGlow = Color(0xFF34D399),
                rotationDeg = 0.5f,
                onClick = { onSelectSticker("crowd_status") }
            )
        }
    }
}
