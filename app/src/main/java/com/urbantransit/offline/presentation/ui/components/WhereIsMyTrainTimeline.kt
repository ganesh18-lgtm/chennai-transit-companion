package com.urbantransit.offline.presentation.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AlarmOn
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsSubway
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urbantransit.offline.data.local.entity.TransitStop
import com.urbantransit.offline.presentation.ui.theme.ChennaiColors

data class TimelineStationItem(
    val stop: TransitStop,
    val sequenceNumber: Int,
    val scheduledTime: String,
    val distanceKm: Double,
    val isOrigin: Boolean,
    val isDestination: Boolean,
    val isCurrentVehiclePosition: Boolean,
    val hasPassed: Boolean,
    val platformNumber: String = "Platform 1",
    val interchangeNote: String? = null
)

@Composable
fun WhereIsMyTrainTimeline(
    stations: List<TimelineStationItem>,
    lineColor: Color,
    activeGeofenceStopId: String?,
    onToggleGeofence: (TransitStop) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ChennaiColors.SurfaceCard, RoundedCornerShape(20.dp))
            .border(1.dp, ChennaiColors.SurfaceBorder, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(lineColor, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "LIVE ROUTE PROGRESSION",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = ChennaiColors.TextSecondary,
                    letterSpacing = 1.2.sp
                )
            }

            Surface(
                color = ChennaiColors.LiveOnlineGreen.copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(ChennaiColors.LiveOnlineGreen, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "OFFLINE GPS ACTIVE",
                        color = ChennaiColors.LiveOnlineGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        stations.forEachIndexed { index, item ->
            val isLast = index == stations.lastIndex
            TimelineNodeRow(
                item = item,
                lineColor = lineColor,
                isLast = isLast,
                isGeofenceActive = activeGeofenceStopId == item.stop.id,
                onToggleGeofence = { onToggleGeofence(item.stop) }
            )
        }
    }
}

@Composable
private fun CurrentVehicleMarker(
    isBus: Boolean,
    lineColor: Color,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "vehiclePulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isBus) ChennaiColors.MtcRed else lineColor,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.White),
        modifier = modifier
            .size(28.dp)
            .graphicsLayer {
                scaleX = pulseScale
                scaleY = pulseScale
            }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = if (isBus) Icons.Default.DirectionsBus else Icons.Default.DirectionsSubway,
                contentDescription = if (isBus) "MTC Bus" else "Metro Train",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun TimelineNodeRow(
    item: TimelineStationItem,
    lineColor: Color,
    isLast: Boolean,
    isGeofenceActive: Boolean,
    onToggleGeofence: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Left Column: Departure / Arrival Time & Platform
        Column(
            modifier = Modifier.width(64.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = item.scheduledTime,
                fontSize = 12.sp,
                fontWeight = if (item.isCurrentVehiclePosition) FontWeight.ExtraBold else FontWeight.Medium,
                color = if (item.isCurrentVehiclePosition) lineColor else ChennaiColors.TextPrimary
            )
            Text(
                text = String.format("%.1f km", item.distanceKm),
                fontSize = 10.sp,
                color = ChennaiColors.TextMuted
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Center Column: Track & Node Dot
        val isBus = item.stop.id.startsWith("mtc_")
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(30.dp)
        ) {
            // Station Dot or Animated Vehicle Marker
            if (item.isCurrentVehiclePosition) {
                CurrentVehicleMarker(isBus = isBus, lineColor = lineColor)
            } else if (item.isOrigin) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(ChennaiColors.MetroGreen, CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                )
            } else if (item.isDestination) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(ChennaiColors.AlertRed, CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(
                            if (item.hasPassed) lineColor else ChennaiColors.SurfaceBorder,
                            CircleShape
                        )
                )
            }

            // Connecting Vertical Track Line
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(if (item.hasPassed) 3.dp else 2.dp)
                        .height(38.dp)
                        .padding(vertical = 2.dp)
                        .background(
                            if (item.hasPassed) lineColor else ChennaiColors.SurfaceBorder
                        )
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Right Column: Station Name, Tamil subtext, Badges & Alarm action
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = if (isLast) 0.dp else 22.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.stop.name,
                        fontSize = 14.sp,
                        fontWeight = if (item.isOrigin || item.isDestination || item.isCurrentVehiclePosition) FontWeight.Bold else FontWeight.Normal,
                        color = if (item.isCurrentVehiclePosition) Color.White else ChennaiColors.TextPrimary
                    )

                    // Platform and status
                    Text(
                        text = "${item.platformNumber} • Stop #${item.sequenceNumber}",
                        fontSize = 11.sp,
                        color = ChennaiColors.TextSecondary
                    )
                }

                // Geofence Proximity Alarm Toggle
                if (item.isDestination || !item.hasPassed) {
                    IconButton(
                        onClick = onToggleGeofence,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (isGeofenceActive) Icons.Filled.AlarmOn else Icons.Filled.Alarm,
                            contentDescription = "Set Proximity Alert",
                            tint = if (isGeofenceActive) ChennaiColors.MtcAmber else ChennaiColors.TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Interchange Indicator Tag
            if (item.stop.name.contains("Central", ignoreCase = true) || item.stop.name.contains("Alandur", ignoreCase = true)) {
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    color = ChennaiColors.MetroBlue.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "🔄 CMRL Multi-Level Interchange",
                        color = ChennaiColors.MetroBlueLight,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            } else if (item.stop.name.contains("Airport", ignoreCase = true)) {
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    color = Color(0xFF6366F1).copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "✈️ Direct Airport Terminal Skywalk",
                        color = Color(0xFFA5B4FC),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
