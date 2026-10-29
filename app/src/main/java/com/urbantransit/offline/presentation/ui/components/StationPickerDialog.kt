package com.urbantransit.offline.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsSubway
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.urbantransit.offline.data.local.entity.TransitStop
import com.urbantransit.offline.presentation.ui.theme.ChennaiColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationPickerDialog(
    title: String,
    allStops: List<TransitStop>,
    selectedStop: TransitStop?,
    initialFilter: String = "All",
    onStopSelected: (TransitStop) -> Unit,
    onDismissRequest: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(initialFilter) } // "All", "Blue Line", "Green Line", "Yellow Line", "MTC Buses"

    val filteredStops = allStops.filter { stop ->
        val matchesQuery = stop.name.contains(searchQuery, ignoreCase = true) ||
                stop.id.contains(searchQuery, ignoreCase = true) ||
                stop.zoneId.contains(searchQuery, ignoreCase = true)

        val matchesFilter = when (selectedFilter) {
            "Blue Line" -> stop.id.startsWith("cmrl_") && !stop.id.contains("_g_") && !stop.id.startsWith("cmrl_y_") &&
                    (stop.id in listOf("cmrl_wimco", "cmrl_tollgate", "cmrl_washermanpet", "cmrl_central", "cmrl_lic", "cmrl_thousand_lights", "cmrl_teynampet", "cmrl_nandanam", "cmrl_saidapet", "cmrl_guindy", "cmrl_alandur", "cmrl_meenambakkam", "cmrl_airport"))
            "Green Line" -> stop.id in listOf("cmrl_central", "cmrl_egmore", "cmrl_nehru_park", "cmrl_kilpauk", "cmrl_shenoy_nagar", "cmrl_anna_nagar_tower", "cmrl_thirumangalam", "cmrl_cmbt", "cmrl_vadapalani", "cmrl_ashok_nagar", "cmrl_alandur", "cmrl_st_thomas_mount")
            "Yellow Line" -> stop.id.startsWith("cmrl_y_") || stop.id in listOf("cmrl_vadapalani", "cmrl_thousand_lights")
            "MTC Buses" -> stop.id.startsWith("mtc_")
            "All Metro" -> stop.id.startsWith("cmrl_")
            else -> true
        }

        matchesQuery && matchesFilter
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = ChennaiColors.SurfaceCardElevated,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "Choose from ${allStops.size} Chennai transit stations",
                        fontSize = 12.sp,
                        color = ChennaiColors.TextSecondary
                    )
                }

                IconButton(
                    onClick = onDismissRequest,
                    modifier = Modifier
                        .size(36.dp)
                        .background(ChennaiColors.DeepCanvas, CircleShape)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search by station or area name...", color = ChennaiColors.TextMuted, fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = ChennaiColors.MetroBlueLight) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ChennaiColors.MetroBlue,
                    unfocusedBorderColor = ChennaiColors.SurfaceBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = ChennaiColors.DeepCanvas,
                    unfocusedContainerColor = ChennaiColors.DeepCanvas
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Corridor Filter Chips
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val filters = listOf("All", "Blue Line", "Green Line", "Yellow Line", "MTC Buses")
                items(filters) { filter ->
                    val isSelected = selectedFilter == filter
                    val filterColor = when (filter) {
                        "Blue Line" -> ChennaiColors.MetroBlue
                        "Green Line" -> ChennaiColors.MetroGreen
                        "Yellow Line" -> ChennaiColors.MtcAmber
                        "MTC Buses" -> ChennaiColors.MtcRed
                        else -> ChennaiColors.MetroBlueLight
                    }

                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter, fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = filterColor,
                            selectedLabelColor = Color.White,
                            containerColor = ChennaiColors.DeepCanvas,
                            labelColor = ChennaiColors.TextSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) filterColor else ChennaiColors.SurfaceBorder
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Stations List
            if (filteredStops.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No stations match '$searchQuery'",
                        color = ChennaiColors.TextMuted,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredStops) { stop ->
                        val isSelected = selectedStop?.id == stop.id

                        val (lineBadgeColor, lineLabel) = when {
                            stop.id.startsWith("cmrl_y_") -> ChennaiColors.MtcAmber to "Yellow Line"
                            stop.id in listOf("cmrl_central", "cmrl_alandur") -> ChennaiColors.MetroBlue to "Interchange (Blue/Green)"
                            stop.id in listOf("cmrl_vadapalani") -> ChennaiColors.MetroGreen to "Interchange (Green/Yellow)"
                            stop.id in listOf("cmrl_thousand_lights") -> ChennaiColors.MetroBlue to "Interchange (Blue/Yellow)"
                            stop.id in listOf("cmrl_egmore", "cmrl_nehru_park", "cmrl_kilpauk", "cmrl_shenoy_nagar", "cmrl_anna_nagar_tower", "cmrl_thirumangalam", "cmrl_cmbt", "cmrl_ashok_nagar", "cmrl_st_thomas_mount") -> ChennaiColors.MetroGreen to "Green Line"
                            stop.id.startsWith("cmrl_") -> ChennaiColors.MetroBlue to "Blue Line"
                            else -> ChennaiColors.MtcRed to "MTC Bus Terminus"
                        }

                        Surface(
                            onClick = {
                                onStopSelected(stop)
                                onDismissRequest()
                            },
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) lineBadgeColor.copy(alpha = 0.2f) else ChennaiColors.DeepCanvas,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) lineBadgeColor else ChennaiColors.SurfaceBorder
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(lineBadgeColor.copy(alpha = 0.2f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (stop.id.startsWith("mtc_")) Icons.Default.DirectionsBus else Icons.Default.DirectionsSubway,
                                            contentDescription = null,
                                            tint = lineBadgeColor,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column {
                                        Text(
                                            text = stop.name,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                            fontSize = 14.sp,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "$lineLabel • ${stop.zoneId.replace('_', ' ').uppercase()}",
                                            fontSize = 11.sp,
                                            color = ChennaiColors.TextSecondary
                                        )
                                    }
                                }

                                if (isSelected) {
                                    Surface(
                                        color = lineBadgeColor,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "SELECTED",
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
