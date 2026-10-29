package com.urbantransit.offline.presentation.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsSubway
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urbantransit.offline.data.local.entity.TransitRoute
import com.urbantransit.offline.data.local.entity.TransitStop
import com.urbantransit.offline.presentation.ui.components.ChennaiTransitSchematicMap
import com.urbantransit.offline.presentation.ui.components.animatedSpecularSheen
import com.urbantransit.offline.presentation.ui.components.bounceClick
import com.urbantransit.offline.presentation.ui.components.interactive3DTilt
import com.urbantransit.offline.presentation.ui.theme.ChennaiColors

sealed class MapLineTab(val id: String, val name: String, val color: Color, val corridor: String) {
    data object All : MapLineTab("all", "All Lines", ChennaiColors.MetroBlueLight, "Full Network Map")
    data object Blue : MapLineTab("cmrl_blue", "Blue Line", ChennaiColors.MetroBlue, "Wimco Nagar ↔ Airport")
    data object Green : MapLineTab("cmrl_green", "Green Line", ChennaiColors.MetroGreen, "Central ↔ St. Thomas Mount")
    data object Yellow : MapLineTab("cmrl_yellow", "Yellow Line", ChennaiColors.MtcAmber, "Poonamallee ↔ Vadapalani (Open)")
    data object Bus : MapLineTab("bus", "MTC Bus Corridors", ChennaiColors.MtcRed, "City & OMR Express")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkMapScreen(
    @Suppress("UNUSED_PARAMETER") routes: List<TransitRoute>,
    allStops: List<TransitStop>,
    onBackClick: () -> Unit,
    onSelectStopForJourney: (TransitStop, isOrigin: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf<MapLineTab>(MapLineTab.All) }

    // Blue Line stations
    val blueLineStops = remember(allStops) {
        val ids = listOf(
            "cmrl_wimco", "cmrl_tollgate", "cmrl_washermanpet", "cmrl_central",
            "cmrl_lic", "cmrl_thousand_lights", "cmrl_teynampet", "cmrl_nandanam",
            "cmrl_saidapet", "cmrl_guindy", "cmrl_alandur", "cmrl_meenambakkam", "cmrl_airport"
        )
        ids.mapNotNull { id -> allStops.find { it.id == id } }
    }

    // Green Line stations
    val greenLineStops = remember(allStops) {
        val ids = listOf(
            "cmrl_central", "cmrl_egmore", "cmrl_nehru_park", "cmrl_kilpauk",
            "cmrl_shenoy_nagar", "cmrl_anna_nagar_tower", "cmrl_thirumangalam",
            "cmrl_cmbt", "cmrl_vadapalani", "cmrl_ashok_nagar", "cmrl_alandur", "cmrl_st_thomas_mount"
        )
        ids.mapNotNull { id -> allStops.find { it.id == id } }
    }

    // Yellow Line stations (Operational section: Poonamallee Bypass to Porur & Vadapalani)
    val yellowLineStops = remember(allStops) {
        val ids = listOf(
            "cmrl_y_poonamallee_bypass", "cmrl_y_poonamallee_bus", "cmrl_y_poonamallee_gh",
            "cmrl_y_karayanchavadi", "cmrl_y_kumananchavadi", "cmrl_y_kattupakkam",
            "cmrl_y_iyyappanthangal", "cmrl_y_thelliyaragaram", "cmrl_y_porur_bypass",
            "cmrl_y_porur", "cmrl_vadapalani"
        )
        ids.mapNotNull { id -> allStops.find { it.id == id } }
    }

    // Key MTC stops
    val mtcKeyStops = remember(allStops) {
        allStops.filter { it.id.startsWith("mtc_") }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ChennaiColors.DeepCanvas,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Chennai Transit Network Map",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Text(
                            text = "Official CMRL Metro & MTC Line Diagram",
                            color = ChennaiColors.TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ChennaiColors.SurfaceCardElevated
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            // 1. Line Selector Filter Chips
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val tabs = listOf(
                        MapLineTab.All,
                        MapLineTab.Blue,
                        MapLineTab.Green,
                        MapLineTab.Yellow,
                        MapLineTab.Bus
                    )
                    tabs.forEach { tab ->
                        val isSelected = selectedTab == tab
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedTab = tab },
                            label = { Text(tab.name, fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = tab.color,
                                selectedLabelColor = if (tab == MapLineTab.Yellow) Color.Black else Color.White,
                                containerColor = ChennaiColors.SurfaceCard,
                                labelColor = ChennaiColors.TextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) tab.color else ChennaiColors.SurfaceBorder
                            )
                        )
                    }
                }
            }

            // 2. Schematic Color Map Diagram (Canvas)
            item {
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = ChennaiColors.SurfaceCardElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, ChennaiColors.SurfaceBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = selectedTab.name.uppercase(),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = selectedTab.color,
                                    letterSpacing = 1.2.sp
                                )
                                Text(
                                    text = selectedTab.corridor,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = selectedTab.color.copy(alpha = 0.15f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Map,
                                        contentDescription = null,
                                        tint = selectedTab.color,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Interactive",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = selectedTab.color
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Visual schematic diagram
                        ChennaiTransitSchematicMap(
                            originStop = null,
                            destinationStop = null,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Color Indicator Legend
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            LegendItem(color = ChennaiColors.MetroBlue, label = "Blue Line", desc = "Corridor 1")
                            LegendItem(color = ChennaiColors.MetroGreen, label = "Green Line", desc = "Corridor 2")
                            LegendItem(color = ChennaiColors.MtcAmber, label = "Yellow Line", desc = "Corridor 4")
                            LegendItem(color = Color.White, label = "Interchange", desc = "Central/Alandur")
                        }
                    }
                }
            }

            // 3. Station Directory with Stop Names by Color Line
            item {
                Text(
                    text = "STATIONS & STOPS ALONG THE CORRIDOR",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = ChennaiColors.TextSecondary,
                    letterSpacing = 1.2.sp
                )
            }

            when (selectedTab) {
                MapLineTab.All -> {
                    item {
                        LineSectionCard(
                            lineTitle = "Blue Line (Corridor 1)",
                            subtitle = "Wimco Nagar Depot ↔ Chennai Airport (23 km • 13 Stations)",
                            lineColor = ChennaiColors.MetroBlue,
                            stations = blueLineStops,
                            onSelectStop = onSelectStopForJourney
                        )
                    }
                    item {
                        LineSectionCard(
                            lineTitle = "Green Line (Corridor 2)",
                            subtitle = "Central Metro ↔ St. Thomas Mount (22 km • 12 Stations)",
                            lineColor = ChennaiColors.MetroGreen,
                            stations = greenLineStops,
                            onSelectStop = onSelectStopForJourney
                        )
                    }
                    item {
                        LineSectionCard(
                            lineTitle = "Yellow Line (Corridor 4 - Phase 2 Operational)",
                            subtitle = "Poonamallee Bypass ↔ Vadapalani (Open to Vadapalani • Stops at all stations to Porur • Non-stop to Vadapalani • 11 Stations)",
                            lineColor = ChennaiColors.MtcAmber,
                            stations = yellowLineStops,
                            onSelectStop = onSelectStopForJourney
                        )
                    }
                    item {
                        LineSectionCard(
                            lineTitle = "MTC Bus Corridors",
                            subtitle = "City & OMR Bus Terminals & Major Hubs (${mtcKeyStops.size} Stops)",
                            lineColor = ChennaiColors.MtcRed,
                            stations = mtcKeyStops,
                            onSelectStop = onSelectStopForJourney
                        )
                    }
                }
                MapLineTab.Blue -> {
                    item {
                        LineSectionCard(
                            lineTitle = "Blue Line (Corridor 1)",
                            subtitle = "Wimco Nagar Depot ↔ Chennai Airport (23 km • 13 Stations)",
                            lineColor = ChennaiColors.MetroBlue,
                            stations = blueLineStops,
                            onSelectStop = onSelectStopForJourney
                        )
                    }
                }
                MapLineTab.Green -> {
                    item {
                        LineSectionCard(
                            lineTitle = "Green Line (Corridor 2)",
                            subtitle = "Central Metro ↔ St. Thomas Mount (22 km • 12 Stations)",
                            lineColor = ChennaiColors.MetroGreen,
                            stations = greenLineStops,
                            onSelectStop = onSelectStopForJourney
                        )
                    }
                }
                MapLineTab.Yellow -> {
                    item {
                        LineSectionCard(
                            lineTitle = "Yellow Line (Corridor 4 - Phase 2 Operational)",
                            subtitle = "Poonamallee Bypass ↔ Vadapalani (Open to Vadapalani • Stops at all stations to Porur • Non-stop to Vadapalani • 11 Stations)",
                            lineColor = ChennaiColors.MtcAmber,
                            stations = yellowLineStops,
                            onSelectStop = onSelectStopForJourney
                        )
                    }
                }
                MapLineTab.Bus -> {
                    item {
                        LineSectionCard(
                            lineTitle = "MTC Bus Network Stops",
                            subtitle = "Broadway, OMR IT Corridor, Guindy, Tambaram & Suburban (${mtcKeyStops.size} Stops)",
                            lineColor = ChennaiColors.MtcRed,
                            stations = mtcKeyStops,
                            onSelectStop = onSelectStopForJourney
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(30.dp)) }
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String, desc: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Column {
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(desc, fontSize = 9.sp, color = ChennaiColors.TextSecondary)
        }
    }
}

@Composable
private fun LineSectionCard(
    lineTitle: String,
    subtitle: String,
    lineColor: Color,
    stations: List<TransitStop>,
    onSelectStop: (TransitStop, isOrigin: Boolean) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = ChennaiColors.SurfaceCardElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, lineColor.copy(alpha = 0.4f)),
        modifier = Modifier
            .fillMaxWidth()
            .interactive3DTilt(maxRotationDegrees = 5f)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .background(lineColor, CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = lineTitle,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = ChennaiColors.TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = ChennaiColors.SurfaceBorder)
            Spacer(modifier = Modifier.height(10.dp))

            // Stations list
            stations.forEachIndexed { index, stop ->
                val isInterchange = stop.id in listOf(
                    "cmrl_central", "cmrl_alandur", "cmrl_vadapalani", "cmrl_thousand_lights"
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Step number badge
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(lineColor.copy(alpha = 0.2f), CircleShape)
                                .border(1.dp, lineColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = lineColor
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = stop.name,
                                    fontSize = 13.sp,
                                    fontWeight = if (isInterchange) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isInterchange) Color.White else ChennaiColors.TextPrimary
                                )
                                if (isInterchange) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = ChennaiColors.LiveOnlineGreen.copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            text = "INTERCHANGE",
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = ChennaiColors.LiveOnlineGreen,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "Zone: ${stop.zoneId.replace("zone_", "").uppercase()} • Lat: ${String.format("%.3f", stop.latitude)}, Lon: ${String.format("%.3f", stop.longitude)}",
                                fontSize = 10.sp,
                                color = ChennaiColors.TextSecondary
                            )
                        }
                    }

                    // Origin / Destination Quick Pickers
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(
                            onClick = { onSelectStop(stop, true) },
                            shape = RoundedCornerShape(6.dp),
                            color = ChennaiColors.DeepCanvas,
                            border = androidx.compose.foundation.BorderStroke(1.dp, ChennaiColors.SurfaceBorder)
                        ) {
                            Text(
                                text = "From",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = ChennaiColors.MetroBlueLight,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            onClick = { onSelectStop(stop, false) },
                            shape = RoundedCornerShape(6.dp),
                            color = ChennaiColors.DeepCanvas,
                            border = androidx.compose.foundation.BorderStroke(1.dp, ChennaiColors.SurfaceBorder)
                        ) {
                            Text(
                                text = "To",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = ChennaiColors.LiveOnlineGreen,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                if (index < stations.size - 1) {
                    Box(
                        modifier = Modifier
                            .padding(start = 11.dp)
                            .height(10.dp)
                            .width(2.dp)
                            .background(lineColor.copy(alpha = 0.3f))
                    )
                }
            }
        }
    }
}
