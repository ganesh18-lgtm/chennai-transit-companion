package com.urbantransit.offline.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.urbantransit.offline.data.local.entity.TicketOrder
import com.urbantransit.offline.data.local.entity.TransitRoute
import com.urbantransit.offline.data.local.entity.TransitStop
import com.urbantransit.offline.data.model.SyncStatus
import com.urbantransit.offline.presentation.TransitUiState
import com.urbantransit.offline.presentation.TransitViewModel
import com.urbantransit.offline.presentation.ui.components.FareBreakdownCard
import com.urbantransit.offline.presentation.ui.components.OfflineBanner
import com.urbantransit.offline.presentation.ui.components.ScheduleCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransitAppScreen(
    viewModel: TransitViewModel,
    uiState: TransitUiState,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearUserMessage()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Urban Transit",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        // Online / Offline Status Pill
                        Surface(
                            shape = CircleShape,
                            color = if (uiState.isOnline) Color(0xFF2E7D32) else Color(0xFFD84315)
                        ) {
                            Text(
                                text = if (uiState.isOnline) "ONLINE" else "OFFLINE",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.manualRefresh() },
                        enabled = !uiState.isSyncing
                    ) {
                        if (uiState.isSyncing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh Schedules"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Offline banner showing last sync timestamp
            OfflineBanner(
                isOnline = uiState.isOnline,
                lastSyncedFormatted = uiState.lastSyncFormatted
            )

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Route Selection Row
                    item {
                        RouteSelectionSection(
                            routes = uiState.routes,
                            selectedRoute = uiState.selectedRoute,
                            onRouteSelected = { viewModel.onRouteSelected(it) }
                        )
                    }

                    // Stop Pickers (Origin & Destination)
                    item {
                        StopPickersSection(
                            stops = if (uiState.stopsForSelectedRoute.isNotEmpty()) {
                                uiState.stopsForSelectedRoute
                            } else {
                                uiState.allStops
                            },
                            selectedOrigin = uiState.selectedOriginStop,
                            selectedDestination = uiState.selectedDestinationStop,
                            onOriginSelected = { viewModel.onOriginStopSelected(it) },
                            onDestinationSelected = { viewModel.onDestinationStopSelected(it) }
                        )
                    }

                    // Geofence Notification Action
                    item {
                        GeofenceActionCard(
                            destinationStop = uiState.selectedDestinationStop,
                            isGeofenceActive = uiState.activeGeofenceStopId == uiState.selectedDestinationStop?.id,
                            onToggleGeofence = { viewModel.toggleGeofenceForDestination() }
                        )
                    }

                    // Multi-Modal Fare Breakdown & Offline Purchase Card
                    item {
                        FareBreakdownCard(
                            route = uiState.selectedRoute,
                            originStop = uiState.selectedOriginStop,
                            destinationStop = uiState.selectedDestinationStop,
                            fare = uiState.calculatedFare,
                            onPurchaseTicketClick = { viewModel.purchaseOfflineTicket() }
                        )
                    }

                    // Upcoming Schedules Section Header
                    item {
                        Text(
                            text = "Upcoming Departures at ${uiState.selectedOriginStop?.name ?: "Origin"}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Schedules List from Room
                    if (uiState.upcomingSchedules.isEmpty()) {
                        item {
                            Text(
                                text = "No cached schedules available for this stop.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        items(uiState.upcomingSchedules) { schedule ->
                            ScheduleCard(scheduleDetails = schedule)
                        }
                    }

                    // Offline Purchased Tickets Section
                    if (uiState.recentTickets.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Your Transit Tickets (Offline Staged)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        items(uiState.recentTickets) { ticket ->
                            TicketItemCard(ticket = ticket)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RouteSelectionSection(
    routes: List<TransitRoute>,
    selectedRoute: TransitRoute?,
    onRouteSelected: (TransitRoute) -> Unit
) {
    Column {
        Text(
            text = "Select Transit Line",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            routes.forEach { route ->
                val isSelected = selectedRoute?.id == route.id
                val routeColor = runCatching {
                    Color(android.graphics.Color.parseColor(route.colorHex))
                }.getOrDefault(MaterialTheme.colorScheme.primary)

                FilterChip(
                    selected = isSelected,
                    onClick = { onRouteSelected(route) },
                    label = { Text(route.shortName, fontWeight = FontWeight.Bold) },
                    leadingIcon = {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(routeColor, CircleShape)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = routeColor.copy(alpha = 0.2f),
                        selectedLabelColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StopPickersSection(
    stops: List<TransitStop>,
    selectedOrigin: TransitStop?,
    selectedDestination: TransitStop?,
    onOriginSelected: (TransitStop) -> Unit,
    onDestinationSelected: (TransitStop) -> Unit
) {
    var originExpanded by remember { mutableStateOf(false) }
    var destinationExpanded by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Origin Picker
        ExposedDropdownMenuBox(
            expanded = originExpanded,
            onExpandedChange = { originExpanded = !originExpanded }
        ) {
            OutlinedTextField(
                value = selectedOrigin?.name ?: "Select Origin Stop",
                onValueChange = {},
                readOnly = true,
                label = { Text("Origin Stop") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = originExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = originExpanded,
                onDismissRequest = { originExpanded = false }
            ) {
                stops.forEach { stop ->
                    DropdownMenuItem(
                        text = { Text(stop.name) },
                        onClick = {
                            onOriginSelected(stop)
                            originExpanded = false
                        }
                    )
                }
            }
        }

        // Destination Picker
        ExposedDropdownMenuBox(
            expanded = destinationExpanded,
            onExpandedChange = { destinationExpanded = !destinationExpanded }
        ) {
            OutlinedTextField(
                value = selectedDestination?.name ?: "Select Destination Stop",
                onValueChange = {},
                readOnly = true,
                label = { Text("Destination Stop") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = destinationExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = destinationExpanded,
                onDismissRequest = { destinationExpanded = false }
            ) {
                stops.forEach { stop ->
                    DropdownMenuItem(
                        text = { Text(stop.name) },
                        onClick = {
                            onDestinationSelected(stop)
                            destinationExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun GeofenceActionCard(
    destinationStop: TransitStop?,
    isGeofenceActive: Boolean,
    onToggleGeofence: () -> Unit
) {
    if (destinationStop == null) return

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isGeofenceActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = if (isGeofenceActive) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                    contentDescription = "Geofence Alert",
                    tint = if (isGeofenceActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (isGeofenceActive) "500m Arrival Alert Active" else "Arrival Alert (500m)",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Notifies you when entering ${destinationStop.name}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Button(
                onClick = onToggleGeofence,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isGeofenceActive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(if (isGeofenceActive) "Cancel" else "Enable")
            }
        }
    }
}

@Composable
private fun TicketItemCard(ticket: TicketOrder) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "${ticket.routeName}: ${ticket.originStopName} → ${ticket.destinationStopName}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$${String.format("%.2f", ticket.fareCents / 100.0)}",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            val (chipColor, label) = when (ticket.syncStatus) {
                SyncStatus.SYNCED -> Color(0xFF2E7D32) to "SYNCED"
                SyncStatus.SYNCING -> Color(0xFF1976D2) to "SYNCING"
                SyncStatus.PENDING -> Color(0xFFF57C00) to "PENDING SYNC"
                SyncStatus.FAILED -> Color(0xFFD32F2F) to "RETRYING"
            }

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = chipColor.copy(alpha = 0.15f)
            ) {
                Text(
                    text = label,
                    color = chipColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}
