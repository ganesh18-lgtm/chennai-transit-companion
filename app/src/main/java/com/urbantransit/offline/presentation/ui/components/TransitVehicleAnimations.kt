package com.urbantransit.offline.presentation.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urbantransit.offline.presentation.ui.theme.ChennaiColors

/**
 * Animated High-Fidelity Chennai Metro Train.
 * Renders a sleek metro train car gliding along an elevated viaduct track with forward headlight beam.
 */
@Composable
fun MetroTrainAnimation(
    lineColor: Color = ChennaiColors.MetroBlue,
    lineName: String = "Chennai Metro Live Progression",
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "metroTrain")
    val trainProgress by infiniteTransition.animateFloat(
        initialValue = -0.3f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(4200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "trainProgress"
    )

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = ChennaiColors.DeepCanvas,
        border = androidx.compose.foundation.BorderStroke(1.dp, lineColor.copy(alpha = 0.35f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(lineColor, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = lineName.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = lineColor,
                        letterSpacing = 1.1.sp
                    )
                }

                Surface(
                    color = lineColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "CRUISING • 65 KM/H",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = lineColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Canvas Scene: Viaduct + Elevated Track + Moving Metro Train
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0A0F1D))
            ) {
                Canvas(modifier = Modifier.fillMaxWidth().height(64.dp)) {
                    val w = size.width
                    val h = size.height
                    val trackY = h * 0.72f

                    // 1. Viaduct Rail Track
                    drawLine(
                        color = Color(0xFF334155),
                        start = Offset(0f, trackY),
                        end = Offset(w, trackY),
                        strokeWidth = 4f
                    )
                    drawLine(
                        color = lineColor.copy(alpha = 0.6f),
                        start = Offset(0f, trackY + 3f),
                        end = Offset(w, trackY + 3f),
                        strokeWidth = 2f
                    )

                    // Track sleeper ties
                    val tieSpacing = 24f
                    var tieX = 0f
                    while (tieX < w) {
                        drawLine(
                            color = Color(0xFF1E293B),
                            start = Offset(tieX, trackY - 2f),
                            end = Offset(tieX, trackY + 6f),
                            strokeWidth = 2f
                        )
                        tieX += tieSpacing
                    }

                    // 2. Metro Train Coordinates
                    val trainWidth = 140f
                    val trainHeight = 26f
                    val trainX = w * trainProgress
                    val trainY = trackY - trainHeight - 2f

                    // Headlight beam casting forward
                    drawPath(
                        path = Path().apply {
                            moveTo(trainX + trainWidth, trainY + trainHeight * 0.5f)
                            lineTo(trainX + trainWidth + 120f, trainY - 8f)
                            lineTo(trainX + trainWidth + 120f, trackY + 12f)
                            close()
                        },
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFDE047).copy(alpha = 0.35f),
                                Color.Transparent
                            ),
                            startX = trainX + trainWidth,
                            endX = trainX + trainWidth + 120f
                        )
                    )

                    // Train Body (Aerodynamic Metallic Car)
                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFE2E8F0),
                                Color(0xFF94A3B8),
                                Color(0xFF475569)
                            )
                        ),
                        topLeft = Offset(trainX, trainY),
                        size = Size(trainWidth, trainHeight),
                        cornerRadius = CornerRadius(6f, 6f)
                    )

                    // Aerodynamic Front Nose (Sloped)
                    drawPath(
                        path = Path().apply {
                            moveTo(trainX + trainWidth - 10f, trainY)
                            lineTo(trainX + trainWidth + 14f, trainY + trainHeight * 0.6f)
                            lineTo(trainX + trainWidth - 4f, trainY + trainHeight)
                            lineTo(trainX + trainWidth - 10f, trainY + trainHeight)
                            close()
                        },
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFFE2E8F0), lineColor, Color(0xFF334155))
                        )
                    )

                    // Metro Line Colored Speed Stripe
                    drawRect(
                        color = lineColor,
                        topLeft = Offset(trainX, trainY + trainHeight * 0.65f),
                        size = Size(trainWidth + 8f, 4f)
                    )

                    // Passenger Windows (Glowing warm yellow interior)
                    val windowY = trainY + 4f
                    val windowH = 10f
                    val windowW = 16f
                    for (i in 0 until 5) {
                        val winX = trainX + 10f + (i * 24f)
                        drawRoundRect(
                            color = Color(0xFFFEF08A),
                            topLeft = Offset(winX, windowY),
                            size = Size(windowW, windowH),
                            cornerRadius = CornerRadius(2f, 2f)
                        )
                    }

                    // Front Windshield (Dark Blue Tint)
                    drawRoundRect(
                        color = Color(0xFF0F172A),
                        topLeft = Offset(trainX + trainWidth - 8f, windowY),
                        size = Size(14f, windowH),
                        cornerRadius = CornerRadius(2f, 2f)
                    )

                    // Bright LED Headlight
                    drawCircle(
                        color = Color(0xFFFEF08A),
                        radius = 3f,
                        center = Offset(trainX + trainWidth + 10f, trainY + trainHeight * 0.65f)
                    )

                    // Wheels / Bogies
                    drawCircle(color = Color(0xFF334155), radius = 3.5f, center = Offset(trainX + 22f, trackY - 1f))
                    drawCircle(color = Color(0xFF334155), radius = 3.5f, center = Offset(trainX + 38f, trackY - 1f))
                    drawCircle(color = Color(0xFF334155), radius = 3.5f, center = Offset(trainX + trainWidth - 38f, trackY - 1f))
                    drawCircle(color = Color(0xFF334155), radius = 3.5f, center = Offset(trainX + trainWidth - 22f, trackY - 1f))

                    // Roof Pantograph
                    drawLine(
                        color = Color(0xFF94A3B8),
                        start = Offset(trainX + trainWidth * 0.45f, trainY),
                        end = Offset(trainX + trainWidth * 0.48f, trainY - 6f),
                        strokeWidth = 1.5f
                    )
                    drawLine(
                        color = Color(0xFF94A3B8),
                        start = Offset(trainX + trainWidth * 0.48f, trainY - 6f),
                        end = Offset(trainX + trainWidth * 0.54f, trainY - 6f),
                        strokeWidth = 2f
                    )
                }
            }
        }
    }
}

/**
 * Animated High-Fidelity Chennai MTC Bus.
 * Renders an animated MTC bus driving along Chennai roads with turning wheels, headlamp glow, and suspension rumble.
 */
@Composable
fun MtcBusAnimation(
    busNumber: String = "21G",
    destination: String = "Broadway ↔ Tambaram",
    isVetriPayanam: Boolean = true,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mtcBus")
    val busProgress by infiniteTransition.animateFloat(
        initialValue = -0.35f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(4800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "busProgress"
    )

    // Subtle road suspension vibration
    val suspensionBounce by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(120, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "suspensionBounce"
    )

    // Wheel rotation angle
    val wheelRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wheelRotation"
    )

    val busColor = if (isVetriPayanam) ChennaiColors.MtcPinkWomen else ChennaiColors.MtcRed

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = ChennaiColors.DeepCanvas,
        border = androidx.compose.foundation.BorderStroke(1.dp, busColor.copy(alpha = 0.35f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(busColor, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "MTC BUS ROUTE $busNumber • $destination",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                }

                Surface(
                    color = busColor.copy(alpha = 0.18f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = if (isVetriPayanam) "🌸 VETRI PAYANAM" else "LIVE GPS ACTIVE",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = busColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Canvas Scene: Asphalt Road + Animated MTC Bus
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0F172A))
            ) {
                Canvas(modifier = Modifier.fillMaxWidth().height(64.dp)) {
                    val w = size.width
                    val h = size.height
                    val roadY = h * 0.76f

                    // 1. Asphalt Road
                    drawRect(
                        color = Color(0xFF1E293B),
                        topLeft = Offset(0f, roadY),
                        size = Size(w, h - roadY)
                    )

                    // Moving White Dashed Center Line
                    val dashW = 20f
                    val dashGap = 16f
                    val offsetShift = (busProgress * w * 1.5f) % (dashW + dashGap)
                    var lineX = -offsetShift
                    while (lineX < w) {
                        drawLine(
                            color = Color(0xFFCBD5E1),
                            start = Offset(lineX, roadY + 8f),
                            end = Offset(lineX + dashW, roadY + 8f),
                            strokeWidth = 2.5f
                        )
                        lineX += (dashW + dashGap)
                    }

                    // 2. Bus Dimensions and Position
                    val busWidth = 115f
                    val busHeight = 32f
                    val busX = w * busProgress
                    val busY = roadY - busHeight + suspensionBounce

                    // Forward Headlight Beam
                    drawPath(
                        path = Path().apply {
                            moveTo(busX + busWidth, busY + busHeight * 0.7f)
                            lineTo(busX + busWidth + 90f, busY + 5f)
                            lineTo(busX + busWidth + 90f, roadY + 10f)
                            close()
                        },
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFEF08A).copy(alpha = 0.35f),
                                Color.Transparent
                            ),
                            startX = busX + busWidth,
                            endX = busX + busWidth + 90f
                        )
                    )

                    // Bus Body Base (Red / Pink lower section)
                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                busColor,
                                busColor.copy(alpha = 0.85f)
                            )
                        ),
                        topLeft = Offset(busX, busY),
                        size = Size(busWidth, busHeight),
                        cornerRadius = CornerRadius(6f, 6f)
                    )

                    // Cream / White Roof & Upper Band
                    drawRoundRect(
                        color = Color(0xFFF8FAFC),
                        topLeft = Offset(busX, busY),
                        size = Size(busWidth, 12f),
                        cornerRadius = CornerRadius(5f, 5f)
                    )

                    // LED Destination Display (Top Front Amber Box)
                    drawRoundRect(
                        color = Color(0xFF0F172A),
                        topLeft = Offset(busX + busWidth - 28f, busY + 2f),
                        size = Size(24f, 7f),
                        cornerRadius = CornerRadius(2f, 2f)
                    )
                    // Amber Glow in Display
                    drawRect(
                        color = Color(0xFFF59E0B),
                        topLeft = Offset(busX + busWidth - 26f, busY + 3.5f),
                        size = Size(20f, 4f)
                    )

                    // Windows Row
                    val winY = busY + 11f
                    val winH = 9f
                    val winW = 14f
                    for (i in 0 until 4) {
                        val wx = busX + 10f + (i * 20f)
                        drawRoundRect(
                            color = Color(0xFF93C5FD).copy(alpha = 0.8f),
                            topLeft = Offset(wx, winY),
                            size = Size(winW, winH),
                            cornerRadius = CornerRadius(2f, 2f)
                        )
                    }

                    // Front Windshield
                    drawRoundRect(
                        color = Color(0xFF1E3A8A),
                        topLeft = Offset(busX + busWidth - 18f, winY),
                        size = Size(14f, winH),
                        cornerRadius = CornerRadius(3f, 3f)
                    )

                    // Headlight (Dual LEDs)
                    drawCircle(
                        color = Color(0xFFFEF08A),
                        radius = 2.5f,
                        center = Offset(busX + busWidth - 1f, busY + busHeight * 0.75f)
                    )

                    // Rear Taillight (Red)
                    drawRect(
                        color = Color(0xFFEF4444),
                        topLeft = Offset(busX - 1.5f, busY + busHeight * 0.65f),
                        size = Size(2.5f, 6f)
                    )

                    // Rolling Rubber Wheels
                    val wheelRadius = 5.5f
                    val frontWheelCenter = Offset(busX + busWidth - 22f, roadY)
                    val rearWheelCenter = Offset(busX + 22f, roadY)

                    // Wheels with spinning spoke rims
                    drawCircle(color = Color(0xFF0F172A), radius = wheelRadius, center = frontWheelCenter)
                    drawCircle(color = Color(0xFFCBD5E1), radius = 2.5f, center = frontWheelCenter)
                    val rad = Math.toRadians(wheelRotation.toDouble())
                    val dx = (kotlin.math.cos(rad) * 2.0).toFloat()
                    val dy = (kotlin.math.sin(rad) * 2.0).toFloat()
                    drawLine(color = Color(0xFF0F172A), start = Offset(frontWheelCenter.x - dx, frontWheelCenter.y - dy), end = Offset(frontWheelCenter.x + dx, frontWheelCenter.y + dy), strokeWidth = 1f)

                    drawCircle(color = Color(0xFF0F172A), radius = wheelRadius, center = rearWheelCenter)
                    drawCircle(color = Color(0xFFCBD5E1), radius = 2.5f, center = rearWheelCenter)
                    drawLine(color = Color(0xFF0F172A), start = Offset(rearWheelCenter.x - dx, rearWheelCenter.y - dy), end = Offset(rearWheelCenter.x + dx, rearWheelCenter.y + dy), strokeWidth = 1f)
                }
            }
        }
    }
}

/**
 * Animated CMRL Corridor 4 Phase 2 GoA4 Driverless Metro Train.
 * Features Poonamallee ↔ Vadapalani operational styling, bright yellow livery, and GoA4 automated cockpit.
 */
@Composable
fun YellowLineMetroAnimation(
    modifier: Modifier = Modifier
) {
    MetroTrainAnimation(
        lineColor = ChennaiColors.MetroYellow,
        lineName = "Corridor 4 • Poonamallee ↔ Vadapalani (Driverless GoA4)",
        modifier = modifier
    )
}

/**
 * Animated MTC Switch EV Low-Floor Zero-Emission Electric Bus.
 * Features sleek emerald green & white livery, battery pack roof pod, and zero-emission status.
 */
@Composable
fun MtcElectricBusAnimation(
    busNumber: String = "570 EV",
    destination: String = "CMBT ↔ Kelambakkam OMR",
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "electricBus")
    val busProgress by infiniteTransition.animateFloat(
        initialValue = -0.35f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(4500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "electricProgress"
    )

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = ChennaiColors.DeepCanvas,
        border = androidx.compose.foundation.BorderStroke(1.dp, ChennaiColors.ElectricGreen.copy(alpha = 0.4f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(ChennaiColors.ElectricGreen, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "MTC SWITCH EV • $busNumber",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                        if (destination.isNotBlank()) {
                            Text(
                                text = destination,
                                fontSize = 10.sp,
                                color = ChennaiColors.TextSecondary
                            )
                        }
                    }
                }

                Surface(
                    color = ChennaiColors.ElectricGreen.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ChennaiColors.ElectricGreen.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "🌿 ZERO EMISSION • LOW-FLOOR",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = ChennaiColors.ElectricGreenLight,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF06141D))
            ) {
                Canvas(modifier = Modifier.fillMaxWidth().height(64.dp)) {
                    val w = size.width
                    val h = size.height
                    val roadY = h * 0.76f

                    // Road
                    drawRect(color = Color(0xFF0F1E29), topLeft = Offset(0f, roadY), size = Size(w, h - roadY))

                    // Road dashes
                    val dashW = 20f
                    val dashGap = 16f
                    val offsetShift = (busProgress * w * 1.5f) % (dashW + dashGap)
                    var lineX = -offsetShift
                    while (lineX < w) {
                        drawLine(
                            color = Color(0xFF38BDF8).copy(alpha = 0.5f),
                            start = Offset(lineX, roadY + 8f),
                            end = Offset(lineX + dashW, roadY + 8f),
                            strokeWidth = 2f
                        )
                        lineX += (dashW + dashGap)
                    }

                    // Bus Coordinates
                    val busWidth = 120f
                    val busHeight = 33f
                    val busX = w * busProgress
                    val busY = roadY - busHeight

                    // Cyan underglow
                    drawRoundRect(
                        brush = Brush.horizontalGradient(
                            listOf(Color.Transparent, ChennaiColors.ElectricGreen.copy(alpha = 0.4f), Color.Transparent)
                        ),
                        topLeft = Offset(busX - 10f, roadY - 4f),
                        size = Size(busWidth + 20f, 8f)
                    )

                    // Bus Body (Clean White & Emerald)
                    drawRoundRect(
                        color = Color(0xFFF8FAFC),
                        topLeft = Offset(busX, busY),
                        size = Size(busWidth, busHeight),
                        cornerRadius = CornerRadius(6f, 6f)
                    )

                    // Lower Emerald Green Skirt
                    drawRoundRect(
                        color = ChennaiColors.ElectricGreen,
                        topLeft = Offset(busX, busY + busHeight * 0.62f),
                        size = Size(busWidth, busHeight * 0.38f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )

                    // Roof Battery Pod (EV Pack)
                    drawRoundRect(
                        color = Color(0xFF334155),
                        topLeft = Offset(busX + 24f, busY - 4f),
                        size = Size(65f, 5f),
                        cornerRadius = CornerRadius(2f, 2f)
                    )

                    // LED Destination Display (Top Front Matrix)
                    drawRoundRect(
                        color = Color(0xFF022C22),
                        topLeft = Offset(busX + busWidth - 28f, busY + 2f),
                        size = Size(24f, 6f),
                        cornerRadius = CornerRadius(2f, 2f)
                    )
                    drawRect(
                        color = Color(0xFF34D399),
                        topLeft = Offset(busX + busWidth - 26f, busY + 3.5f),
                        size = Size(20f, 3f)
                    )

                    // Wide Panoramic Windows
                    val winY = busY + 9f
                    val winH = 10f
                    for (i in 0 until 4) {
                        val wx = busX + 10f + (i * 22f)
                        drawRoundRect(
                            color = Color(0xFF0F172A),
                            topLeft = Offset(wx, winY),
                            size = Size(16f, winH),
                            cornerRadius = CornerRadius(2f, 2f)
                        )
                    }

                    // Dual Headlights
                    drawCircle(color = Color(0xFFE0F2FE), radius = 2.5f, center = Offset(busX + busWidth - 1f, busY + busHeight * 0.75f))

                    // Alloy Wheels
                    drawCircle(color = Color(0xFF0F172A), radius = 5.5f, center = Offset(busX + busWidth - 22f, roadY))
                    drawCircle(color = Color(0xFF38BDF8), radius = 2f, center = Offset(busX + busWidth - 22f, roadY))

                    drawCircle(color = Color(0xFF0F172A), radius = 5.5f, center = Offset(busX + 22f, roadY))
                    drawCircle(color = Color(0xFF38BDF8), radius = 2f, center = Offset(busX + 22f, roadY))
                }
            }
        }
    }
}

/**
 * Widescreen Cinematic Motion Picture Banner of Chennai Urban Transit.
 * Features an atmospheric city skyline (Central Station dome, Napier bridge arches, elevated viaduct)
 * with a high-speed CMRL train and MTC electric bus moving in real-time parallax.
 */
@Composable
fun TransitCinematicMotionPicture(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "cinematicTransit")

    // Train movement
    val trainProgress by infiniteTransition.animateFloat(
        initialValue = -0.3f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(5200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "cinematicTrain"
    )

    // Bus movement in opposite direction
    val busProgress by infiniteTransition.animateFloat(
        initialValue = 1.3f,
        targetValue = -0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(6800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "cinematicBus"
    )

    // Twinkling stars / city pulse
    val cityGlow by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cityGlow"
    )

    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color(0xFF070B14),
        border = androidx.compose.foundation.BorderStroke(
            1.2.dp,
            Brush.horizontalGradient(
                listOf(
                    ChennaiColors.MetroBlue.copy(alpha = 0.6f),
                    ChennaiColors.MetroYellow.copy(alpha = 0.6f),
                    ChennaiColors.ElectricGreen.copy(alpha = 0.5f)
                )
            )
        ),
        shadowElevation = 8.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(ChennaiColors.NeonCyan, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "CHENNAI TRANSIT CINEMATIC PULSE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = 1.1.sp
                    )
                }

                Surface(
                    color = Color(0xFF0F172A),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("⚡", fontSize = 10.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "METRO + MTC LIVE",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = ChennaiColors.MetroBlueLight
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Canvas Scene
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF0B132B),
                                Color(0xFF1C2541),
                                Color(0xFF0B1021)
                            )
                        )
                    )
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // 1. Distant Skyline Silhouettes (Ripon Building dome, Central Station clock, IT Towers)
                    val skylineBaseY = h * 0.62f
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF152238).copy(alpha = 0.6f * cityGlow), Color(0xFF0B1021))
                        ),
                        topLeft = Offset(0f, skylineBaseY - 35f),
                        size = Size(w, 35f)
                    )

                    // Central Station Clock Tower Silhouette
                    val towerX = w * 0.45f
                    drawRect(
                        color = Color(0xFF1E2D4A).copy(alpha = 0.8f),
                        topLeft = Offset(towerX - 12f, skylineBaseY - 48f),
                        size = Size(24f, 48f)
                    )
                    // Tower Dome & Spire
                    drawCircle(
                        color = Color(0xFF1E2D4A).copy(alpha = 0.8f),
                        radius = 10f,
                        center = Offset(towerX, skylineBaseY - 50f)
                    )
                    drawCircle(
                        color = Color(0xFFFDE047).copy(alpha = 0.7f * cityGlow),
                        radius = 2.5f,
                        center = Offset(towerX, skylineBaseY - 38f)
                    )

                    // Napier Bridge Arches on right
                    val archStartX = w * 0.7f
                    for (a in 0 until 3) {
                        val ax = archStartX + (a * 38f)
                        drawArc(
                            color = Color(0xFF38BDF8).copy(alpha = 0.35f * cityGlow),
                            startAngle = 180f,
                            sweepAngle = 180f,
                            useCenter = false,
                            topLeft = Offset(ax, skylineBaseY - 24f),
                            size = Size(34f, 40f),
                            style = Stroke(width = 2.5f)
                        )
                    }

                    // 2. High Elevated Viaduct (Upper Tier: Metro Train)
                    val viaductY = h * 0.48f

                    // Concrete Viaduct Pillars
                    for (i in 0 until 8) {
                        val px = (w / 7f) * i
                        drawLine(
                            color = Color(0xFF334155),
                            start = Offset(px, viaductY + 4f),
                            end = Offset(px, h * 0.84f),
                            strokeWidth = 3f
                        )
                    }

                    // Viaduct Deck Girder
                    drawRect(
                        brush = Brush.verticalGradient(listOf(Color(0xFF475569), Color(0xFF1E293B))),
                        topLeft = Offset(0f, viaductY),
                        size = Size(w, 5f)
                    )
                    // Safety railing glow
                    drawLine(
                        color = ChennaiColors.MetroBlueLight.copy(alpha = 0.7f),
                        start = Offset(0f, viaductY - 1f),
                        end = Offset(w, viaductY - 1f),
                        strokeWidth = 1.5f
                    )

                    // Moving CMRL Metro Train (Upper Tier)
                    val trainW = 110f
                    val trainH = 18f
                    val trainX = w * trainProgress
                    val trainY = viaductY - trainH - 1f

                    // Headlight Cone
                    drawPath(
                        path = Path().apply {
                            moveTo(trainX + trainW, trainY + trainH * 0.5f)
                            lineTo(trainX + trainW + 80f, trainY - 5f)
                            lineTo(trainX + trainW + 80f, viaductY + 10f)
                            close()
                        },
                        brush = Brush.horizontalGradient(
                            listOf(Color(0xFFFEF08A).copy(alpha = 0.4f), Color.Transparent),
                            startX = trainX + trainW,
                            endX = trainX + trainW + 80f
                        )
                    )

                    // Train Body
                    drawRoundRect(
                        brush = Brush.verticalGradient(listOf(Color(0xFFE2E8F0), Color(0xFF64748B))),
                        topLeft = Offset(trainX, trainY),
                        size = Size(trainW, trainH),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                    // Speed stripe (Cyan & Yellow)
                    drawRect(
                        brush = Brush.horizontalGradient(listOf(ChennaiColors.MetroBlue, ChennaiColors.MetroYellow)),
                        topLeft = Offset(trainX, trainY + trainH * 0.65f),
                        size = Size(trainW, 3f)
                    )
                    // Glowing Windows
                    for (win in 0 until 4) {
                        drawRect(
                            color = Color(0xFFFEF08A).copy(alpha = 0.9f),
                            topLeft = Offset(trainX + 8f + (win * 22f), trainY + 3f),
                            size = Size(14f, 7f)
                        )
                    }

                    // 3. Lower Tier Roadway (MTC Bus Expressway)
                    val groundRoadY = h * 0.86f
                    drawRect(
                        color = Color(0xFF0F172A),
                        topLeft = Offset(0f, groundRoadY),
                        size = Size(w, h - groundRoadY)
                    )

                    // Moving MTC Electric Bus (Lower Tier - cruising right to left)
                    val busW = 85f
                    val busH = 22f
                    val busX = w * busProgress
                    val busY = groundRoadY - busH

                    // Taillight trail moving left
                    drawRect(
                        brush = Brush.horizontalGradient(
                            listOf(Color.Transparent, Color(0xFFEF4444).copy(alpha = 0.6f)),
                            startX = busX + busW,
                            endX = busX + busW + 40f
                        ),
                        topLeft = Offset(busX + busW, busY + 6f),
                        size = Size(40f, 4f)
                    )

                    // Bus Body (MTC Pink/Red with White top)
                    drawRoundRect(
                        color = Color(0xFFF8FAFC),
                        topLeft = Offset(busX, busY),
                        size = Size(busW, busH),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                    drawRoundRect(
                        color = ChennaiColors.MtcPinkWomen,
                        topLeft = Offset(busX, busY + busH * 0.5f),
                        size = Size(busW, busH * 0.5f),
                        cornerRadius = CornerRadius(3f, 3f)
                    )
                    // Bus Windows
                    for (bw in 0 until 3) {
                        drawRect(
                            color = Color(0xFF93C5FD).copy(alpha = 0.85f),
                            topLeft = Offset(busX + 8f + (bw * 20f), busY + 4f),
                            size = Size(13f, 6f)
                        )
                    }
                    // Headlight
                    drawCircle(color = Color(0xFFFEF08A), radius = 2f, center = Offset(busX, busY + busH * 0.65f))
                    // Wheels
                    drawCircle(color = Color(0xFF020617), radius = 4f, center = Offset(busX + 16f, groundRoadY))
                    drawCircle(color = Color(0xFF020617), radius = 4f, center = Offset(busX + busW - 16f, groundRoadY))
                }
            }
        }
    }
}
