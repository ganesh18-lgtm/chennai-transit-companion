package com.urbantransit.offline.presentation.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urbantransit.offline.presentation.ui.theme.ChennaiColors
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Interactive 3D Perspective Tilt Modifier (Non-blocking).
 * Allows cards to have smooth 3D tactile elevation and micro-tilt without
 * intercepting or interfering with list drag scrolling.
 */
fun Modifier.interactive3DTilt(
    maxRotationDegrees: Float = 4f
): Modifier = composed {
    var isPressed by remember { mutableStateOf(false) }
    val rotX by animateFloatAsState(
        targetValue = if (isPressed) -maxRotationDegrees else 0f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessMediumLow),
        label = "tiltX"
    )
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.985f else 1f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessMediumLow),
        label = "tiltScale"
    )

    this
        .graphicsLayer {
            this.rotationX = rotX
            this.scaleX = scale
            this.scaleY = scale
            this.cameraDistance = 16f * density
        }
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    isPressed = true
                    tryAwaitRelease()
                    isPressed = false
                }
            )
        }
}

/**
 * Animated Metallic Specular Sheen Modifier.
 * Slides an angled beam of light continuously across high-importance cards and buttons.
 */
fun Modifier.animatedSpecularSheen(
    sheenColor: Color = Color.White.copy(alpha = 0.16f),
    durationMillis: Int = 2800
): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "specularSheen")
    val progress by infiniteTransition.animateFloat(
        initialValue = -0.6f,
        targetValue = 1.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sheenProgress"
    )

    this.drawWithContent {
        drawContent()
        val width = size.width
        val height = size.height
        val startX = width * progress
        val endX = startX + width * 0.45f

        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color.Transparent,
                    sheenColor,
                    Color.Transparent
                ),
                start = Offset(startX, 0f),
                end = Offset(endX, height)
            )
        )
    }
}

/**
 * Tactile Bounce Click Modifier with spring physics.
 */
fun Modifier.bounceClick(
    scaleDown: Float = 0.95f,
    onClick: () -> Unit
): Modifier = composed {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) scaleDown else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMedium),
        label = "bounceScale"
    )

    this
        .scale(scale)
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    isPressed = true
                    tryAwaitRelease()
                    isPressed = false
                },
                onTap = { onClick() }
            )
        }
}

/**
 * 3D Holographic Smart Ticket Pass with Interactive Tilt and Scanning Laser.
 */
@Composable
fun Holographic3DPass(
    ticketId: String,
    routeName: String,
    originName: String,
    destinationName: String,
    fareRupees: String,
    isWomenFree: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "laserScan")
    val laserOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laserOffset"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .interactive3DTilt(maxRotationDegrees = 12f)
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E293B),
                        Color(0xFF0F172A),
                        Color(0xFF020617)
                    )
                )
            )
            .border(
                width = 1.5.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF38BDF8).copy(alpha = 0.7f),
                        Color(0xFFFACC15).copy(alpha = 0.4f),
                        Color(0xFFEC4899).copy(alpha = 0.6f),
                        Color(0xFF22C55E).copy(alpha = 0.4f)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .animatedSpecularSheen(sheenColor = Color.White.copy(alpha = 0.12f))
            .padding(20.dp)
    ) {
        Column {
            // Header Row: Smart Pass Branding & Hologram Chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color(0xFFF59E0B), Color(0xFFFACC15))
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(1.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("CMRL", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "CHENNAI TRANSIT SMARTPASS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Offline Cryptographic Token",
                            fontSize = 9.sp,
                            color = ChennaiColors.TextSecondary
                        )
                    }
                }

                Surface(
                    color = Color(0xFF38BDF8).copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "VALID",
                        color = Color(0xFF38BDF8),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Route Details
            Text(
                text = routeName,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("FROM", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = ChennaiColors.TextMuted)
                    Text(originName, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
                Text("➔", color = ChennaiColors.MetroBlueLight, fontSize = 16.sp, modifier = Modifier.padding(horizontal = 8.dp))
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Text("TO", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = ChennaiColors.TextMuted)
                    Text(destinationName, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // QR & Barcode Section with Scanning Laser Effect
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(95.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Stylized Offline Barcode Lines
                    Canvas(modifier = Modifier.fillMaxWidth().height(42.dp)) {
                        val barWidth = size.width / 50f
                        for (i in 0 until 50) {
                            if (i % 3 != 0) {
                                drawRect(
                                    color = Color.Black,
                                    topLeft = Offset(i * barWidth, 0f),
                                    size = androidx.compose.ui.geometry.Size(barWidth * 0.7f, size.height)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "ID: ${ticketId.take(18).uppercase()} • OFFLINE VERIFIED",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                // Sweeping Green Scanning Laser Beam
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .offset { IntOffset(0, (laserOffset * 70.dp.toPx() - 35.dp.toPx()).roundToInt()) }
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0xFF22C55E),
                                    Color(0xFF4ADE80),
                                    Color(0xFF22C55E),
                                    Color.Transparent
                                )
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Footer: Fare & Scheme Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("TOTAL FARE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = ChennaiColors.TextMuted)
                    Text(fareRupees, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                }

                if (isWomenFree) {
                    Surface(
                        color = ChennaiColors.MtcPinkWomen.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ChennaiColors.MtcPinkWomen)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🌸", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "Vetri Payanam Free Pass",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Pulsing Radar Circle for Live Location.
 */
@Composable
fun PulseRadarBeacon(
    color: Color = ChennaiColors.LiveOnlineGreen,
    sizeDp: Int = 24
) {
    val infiniteTransition = rememberInfiniteTransition(label = "radarPulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 2.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radarScale"
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radarAlpha"
    )

    Box(
        modifier = Modifier.size(sizeDp.dp),
        contentAlignment = Alignment.Center
    ) {
        // Expanding ripple
        Box(
            modifier = Modifier
                .size((sizeDp * 0.8).dp)
                .scale(scale)
                .background(color.copy(alpha = alpha), CircleShape)
        )
        // Solid core
        Box(
            modifier = Modifier
                .size((sizeDp * 0.5).dp)
                .background(color, CircleShape)
                .border(1.5.dp, Color.White, CircleShape)
        )
    }
}
