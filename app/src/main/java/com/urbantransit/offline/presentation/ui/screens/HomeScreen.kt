package com.urbantransit.offline.presentation.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsSubway
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.urbantransit.offline.data.local.entity.TransitRoute
import com.urbantransit.offline.data.local.entity.TransitStop
import com.urbantransit.offline.data.model.TransitMode
import com.urbantransit.offline.domain.model.UserSession
import com.urbantransit.offline.presentation.TransitUiState
import com.urbantransit.offline.presentation.TransitViewModel
import com.urbantransit.offline.presentation.ui.components.MetroTrainAnimation
import com.urbantransit.offline.presentation.ui.components.MtcBusAnimation
import com.urbantransit.offline.presentation.ui.components.MtcElectricBusAnimation
import com.urbantransit.offline.presentation.ui.components.OfflineBanner
import com.urbantransit.offline.presentation.ui.components.PulseRadarBeacon
import com.urbantransit.offline.presentation.ui.components.StationPickerDialog
import com.urbantransit.offline.presentation.ui.components.TimelineStationItem
import com.urbantransit.offline.presentation.ui.components.TransitCinematicMotionPicture
import com.urbantransit.offline.presentation.ui.components.TransitStickersRibbon
import com.urbantransit.offline.presentation.ui.components.WhereIsMyTrainTimeline
import com.urbantransit.offline.presentation.ui.components.YellowLineMetroAnimation
import com.urbantransit.offline.presentation.ui.components.animatedSpecularSheen
import com.urbantransit.offline.presentation.ui.components.bounceClick
import com.urbantransit.offline.presentation.ui.components.interactive3DTilt
import com.urbantransit.offline.presentation.ui.theme.ChennaiColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: TransitViewModel,
    uiState: TransitUiState,
    session: UserSession,
    onNavigateToFareDetails: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToMap: () -> Unit,
    onNavigateToMtcDirectory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedTransportMode by remember { mutableStateOf(TransitMode.SUBWAY) }
    var showCalculatedResults by remember { mutableStateOf(true) } // Show calculated route immediately for instant responsiveness

    var showOriginPicker by remember { mutableStateOf(false) }
    var showDestinationPicker by remember { mutableStateOf(false) }

    val allAvailableStops = if (uiState.allStops.isNotEmpty()) {
        uiState.allStops
    } else {
        com.urbantransit.offline.data.local.ChennaiTransitSeed.stops
    }

    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearUserMessage()
        }
    }

    // Origin Station Picker Modal
    if (showOriginPicker) {
        StationPickerDialog(
            title = "Select Starting Point (Origin)",
            allStops = allAvailableStops,
            selectedStop = uiState.selectedOriginStop,
            initialFilter = if (selectedTransportMode == TransitMode.SUBWAY) "All Metro" else "MTC Buses",
            onStopSelected = { stop ->
                viewModel.onOriginStopSelected(stop)
            },
            onDismissRequest = { showOriginPicker = false }
        )
    }

    // Destination Station Picker Modal
    if (showDestinationPicker) {
        StationPickerDialog(
            title = "Select Ending Point (Destination)",
            allStops = allAvailableStops,
            selectedStop = uiState.selectedDestinationStop,
            initialFilter = if (selectedTransportMode == TransitMode.SUBWAY) "All Metro" else "MTC Buses",
            onStopSelected = { stop ->
                viewModel.onDestinationStopSelected(stop)
            },
            onDismissRequest = { showDestinationPicker = false }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = ChennaiColors.DeepCanvas,
                drawerContentColor = Color.White,
                modifier = Modifier.width(320.dp)
            ) {
                TransitSidebarDrawerContent(
                    session = session,
                    onEditProfileClick = {
                        coroutineScope.launch { drawerState.close() }
                        onNavigateToProfile()
                    },
                    onFareDetailsClick = {
                        coroutineScope.launch { drawerState.close() }
                        onNavigateToFareDetails()
                    },
                    onMapClick = {
                        coroutineScope.launch { drawerState.close() }
                        onNavigateToMap()
                    },
                    onMtcDirectoryClick = {
                        coroutineScope.launch { drawerState.close() }
                        onNavigateToMtcDirectory()
                    },
                    onLogoutClick = {
                        coroutineScope.launch { drawerState.close() }
                        viewModel.logout()
                    }
                )
            }
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = ChennaiColors.DeepCanvas,
            modifier = modifier.fillMaxSize()
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Top App Bar
                TopHeaderSection(
                    session = session,
                    isOnline = uiState.isOnline,
                    lastSyncFormatted = uiState.lastSyncFormatted,
                    onRefreshClick = { viewModel.manualRefresh() },
                    onProfileClick = {
                        coroutineScope.launch { drawerState.open() }
                    }
                )

                // Offline alert strip
                OfflineBanner(
                    isOnline = uiState.isOnline,
                    lastSyncedFormatted = uiState.lastSyncFormatted
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }

                // 0. Cinematic Motion Picture: High-speed Chennai Skyline & Viaduct Animation
                item {
                    TransitCinematicMotionPicture()
                }

                // Interactive Chennai Transit Badges & Welfare Schemes Ribbon
                item {
                    TransitStickersRibbon(
                        onSelectSticker = { stickerKey ->
                            when (stickerKey) {
                                "vidiyal_payanam" -> {
                                    onNavigateToMtcDirectory()
                                }
                                "phase2_yellow" -> {
                                    val yellowRoute = uiState.routes.find { it.id == "cmrl_yellow" }
                                    if (yellowRoute != null) {
                                        selectedTransportMode = TransitMode.SUBWAY
                                        viewModel.onRouteSelected(yellowRoute)
                                        val poonamallee = allAvailableStops.find { it.id == "cmrl_y_poonamallee_bypass" }
                                        val vadapalani = allAvailableStops.find { it.id == "cmrl_vadapalani" }
                                        if (poonamallee != null) viewModel.onOriginStopSelected(poonamallee)
                                        if (vadapalani != null) viewModel.onDestinationStopSelected(vadapalani)
                                    }
                                }
                                "singara_ncmc" -> {
                                    onNavigateToFareDetails()
                                }
                                "electric_bus" -> {
                                    val bus570 = uiState.routes.find { it.id == "mtc_570" }
                                    if (bus570 != null) {
                                        selectedTransportMode = TransitMode.BUS
                                        viewModel.onRouteSelected(bus570)
                                    } else {
                                        onNavigateToMtcDirectory()
                                    }
                                }
                                "offline_sync" -> {
                                    viewModel.manualRefresh()
                                }
                                "crowd_status" -> {
                                    // Visual indicator
                                }
                            }
                        }
                    )
                }

                // 1. Choose Mode of Transport: Metro vs MTC Bus
                item {
                    TransportModeSelector(
                        selectedMode = selectedTransportMode,
                        onModeSelected = { mode ->
                            selectedTransportMode = mode
                            val matchingRoute = uiState.routes.firstOrNull { it.mode == mode }
                            matchingRoute?.let { viewModel.onRouteSelected(it) }
                        }
                    )
                }

                // 2. Select Line (Blue Line, Green Line, Yellow Line, or MTC Bus Routes)
                item {
                    LineSelectorChips(
                        selectedMode = selectedTransportMode,
                        routes = uiState.routes.filter { it.mode == selectedTransportMode },
                        selectedRoute = uiState.selectedRoute,
                        onRouteSelected = {
                            viewModel.onRouteSelected(it)
                        }
                    )
                }

                // Live Vehicle Animation: Yellow Line GoA4 / Metro Train for Subway, Switch EV / MTC Bus for Bus Mode
                item {
                    if (selectedTransportMode == TransitMode.SUBWAY) {
                        if (uiState.selectedRoute?.id == "cmrl_yellow") {
                            YellowLineMetroAnimation()
                        } else {
                            val metroColor = runCatching {
                                Color(android.graphics.Color.parseColor(uiState.selectedRoute?.colorHex ?: "#2563EB"))
                            }.getOrDefault(ChennaiColors.MetroBlue)
                            MetroTrainAnimation(
                                lineColor = metroColor,
                                lineName = uiState.selectedRoute?.shortName ?: "Chennai Metro Rail"
                            )
                        }
                    } else {
                        val busShort = uiState.selectedRoute?.shortName ?: "21G Deluxe"
                        val busNum = busShort.substringBefore(" ")
                        val busDest = uiState.selectedRoute?.longName ?: "Broadway ↔ Tambaram West"
                        val isVetri = busShort.contains("Ordinary", ignoreCase = true) || busDest.contains("Vetri Payanam", ignoreCase = true)
                        val isElectric = busShort.contains("AC", ignoreCase = true) || busShort.contains("570", ignoreCase = true)

                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            if (isElectric) {
                                MtcElectricBusAnimation(
                                    busNumber = busNum,
                                    destination = busDest
                                )
                            } else {
                                MtcBusAnimation(
                                    busNumber = busNum,
                                    destination = busDest,
                                    isVetriPayanam = isVetri
                                )
                            }

                            // Direct entrypoint banner to 25+ MTC routes and schemes
                            Surface(
                                onClick = onNavigateToMtcDirectory,
                                shape = RoundedCornerShape(16.dp),
                                color = ChennaiColors.SurfaceCardElevated,
                                border = androidx.compose.foundation.BorderStroke(1.dp, ChennaiColors.MtcRed.copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(38.dp)
                                                .background(ChennaiColors.MtcRed.copy(alpha = 0.18f), CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.DirectionsBus,
                                                contentDescription = null,
                                                tint = ChennaiColors.MtcRed,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = "Explore 25+ MTC Bus Routes & Schemes",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = Color.White
                                            )
                                            Text(
                                                text = "Vetri Payanam Free Travel, Senior Tokens, TAYP Passes",
                                                fontSize = 11.sp,
                                                color = ChennaiColors.TextSecondary
                                            )
                                        }
                                    }
                                    Surface(
                                        color = ChennaiColors.MtcRed,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = "Directory →",
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 3. Starting Point & Ending Point Selection Card with Tap to Pick & Swap
                item {
                    OriginDestinationInteractiveCard(
                        selectedOrigin = uiState.selectedOriginStop,
                        selectedDestination = uiState.selectedDestinationStop,
                        onOpenOriginPicker = { showOriginPicker = true },
                        onOpenDestinationPicker = { showDestinationPicker = true },
                        onSwapStations = {
                            val orig = uiState.selectedOriginStop
                            val dest = uiState.selectedDestinationStop
                            if (orig != null && dest != null) {
                                viewModel.onOriginStopSelected(dest)
                                viewModel.onDestinationStopSelected(orig)
                            }
                        }
                    )
                }

                // Quick Station Shortcuts Row
                item {
                    QuickStationShortcuts(
                        mode = selectedTransportMode,
                        allStops = allAvailableStops,
                        onSelectStation = { stop ->
                            viewModel.onDestinationStopSelected(stop)
                        }
                    )
                }

                // 4. Calculate Fare Button (Clean, 100% Responsive)
                item {
                    Button(
                        onClick = {
                            showCalculatedResults = true
                            val orig = uiState.selectedOriginStop ?: allAvailableStops.firstOrNull()
                            val dest = uiState.selectedDestinationStop ?: allAvailableStops.getOrNull(1)
                            if (orig != null) viewModel.onOriginStopSelected(orig)
                            if (dest != null) viewModel.onDestinationStopSelected(dest)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedTransportMode == TransitMode.SUBWAY) ChennaiColors.MetroBlue else ChennaiColors.MtcRed
                        ),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                    ) {
                        Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(19.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Calculate Fare & Find Route",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // 5. Results Section: Fare Summary Pill, Map, and "Where is my train" Route Timeline
                if (showCalculatedResults && uiState.calculatedFare != null) {
                    item {
                        FareSummaryBanner(
                            fare = uiState.calculatedFare,
                            onViewBreakdownClick = onNavigateToFareDetails
                        )
                    }

                    // Color-Coded Network Map Card (Navigates to dedicated network map page with station names)
                    item {
                        Surface(
                            onClick = onNavigateToMap,
                            shape = RoundedCornerShape(18.dp),
                            color = ChennaiColors.SurfaceCardElevated,
                            border = androidx.compose.foundation.BorderStroke(1.dp, ChennaiColors.MetroBlue.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .background(ChennaiColors.MetroBlue.copy(alpha = 0.2f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DirectionsSubway,
                                            contentDescription = null,
                                            tint = ChennaiColors.MetroBlueLight,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "View Color-Coded Network Map",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Blue, Green & Yellow lines with every stop name",
                                            fontSize = 11.sp,
                                            color = ChennaiColors.TextSecondary
                                        )
                                    }
                                }

                                Surface(
                                    color = ChennaiColors.MetroBlue,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "Open Map →",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Below: Show Route with Intermediate Stations ("Where Is My Train" signature design)
                    item {
                        val routeStops = if (uiState.stopsForSelectedRoute.isNotEmpty()) {
                            uiState.stopsForSelectedRoute
                        } else {
                            allAvailableStops.filter { it.id.startsWith("cmrl_") }
                        }

                        val originIndex = routeStops.indexOfFirst { it.id == uiState.selectedOriginStop?.id }
                        val destIndex = routeStops.indexOfFirst { it.id == uiState.selectedDestinationStop?.id }

                        val intermediateStops = if (originIndex != -1 && destIndex != -1 && originIndex <= destIndex) {
                            routeStops.subList(originIndex, destIndex + 1)
                        } else if (originIndex != -1 && destIndex != -1) {
                            routeStops.subList(destIndex, originIndex + 1).reversed()
                        } else {
                            routeStops.take(8)
                        }

                        val timelineItems = intermediateStops.mapIndexed { idx, stop ->
                            TimelineStationItem(
                                stop = stop,
                                sequenceNumber = idx + 1,
                                scheduledTime = String.format("0%d:%02d AM", 6 + (idx * 4) / 60, (idx * 4) % 60),
                                distanceKm = idx * 1.8,
                                isOrigin = stop.id == uiState.selectedOriginStop?.id,
                                isDestination = stop.id == uiState.selectedDestinationStop?.id,
                                isCurrentVehiclePosition = idx == 1,
                                hasPassed = idx == 0,
                                platformNumber = if (selectedTransportMode == TransitMode.SUBWAY) "Platform ${1 + (idx % 2)}" else "Bay #${idx + 1}"
                            )
                        }

                        val currentLineColor = runCatching {
                            Color(android.graphics.Color.parseColor(uiState.selectedRoute?.colorHex ?: "#2563EB"))
                        }.getOrDefault(ChennaiColors.MetroBlue)

                        WhereIsMyTrainTimeline(
                            stations = timelineItems,
                            lineColor = currentLineColor,
                            activeGeofenceStopId = uiState.activeGeofenceStopId,
                            onToggleGeofence = { stop ->
                                viewModel.onDestinationStopSelected(stop)
                                viewModel.toggleGeofenceForDestination()
                            }
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(30.dp)) }
            }
        }
    }
}
}

@Composable
private fun TopHeaderSection(
    session: UserSession,
    isOnline: Boolean,
    lastSyncFormatted: String,
    onRefreshClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Top-left profile icon & greeting: Clicking here opens the sidebar!
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .clickable { onProfileClick() }
                .padding(vertical = 4.dp, horizontal = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(ChennaiColors.SurfaceCardElevated, CircleShape)
                    .border(1.5.dp, ChennaiColors.MetroBlueLight, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = session.avatarEmoji.ifBlank { "🚇" },
                    fontSize = 22.sp
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Vanakkam, ${session.displayName.take(14)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Open Sidebar Navigation",
                        tint = ChennaiColors.TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(if (isOnline) ChennaiColors.LiveOnlineGreen else ChennaiColors.OfflineAmber, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (isOnline) "Live Sync • Chennai Network" else "Offline Cache • $lastSyncFormatted",
                        fontSize = 11.sp,
                        color = ChennaiColors.TextSecondary
                    )
                }
            }
        }

        IconButton(onClick = onRefreshClick) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Sync",
                tint = ChennaiColors.TextSecondary
            )
        }
    }
}

@Composable
private fun TransitSidebarDrawerContent(
    session: UserSession,
    onEditProfileClick: () -> Unit,
    onFareDetailsClick: () -> Unit,
    onMapClick: () -> Unit,
    onMtcDirectoryClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Profile Picture & Info Banner (CLICKING PICTURE OR CARD TAKES USER TO EDIT PROFILE)
        Surface(
            onClick = onEditProfileClick,
            shape = RoundedCornerShape(22.dp),
            color = ChennaiColors.SurfaceCardElevated,
            border = androidx.compose.foundation.BorderStroke(1.dp, ChennaiColors.MetroBlue.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Large Profile Avatar with Edit Icon Badge
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(78.dp)
                            .background(ChennaiColors.DeepCanvas, CircleShape)
                            .border(2.dp, ChennaiColors.MetroBlueLight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = session.avatarEmoji.ifBlank { "🚇" },
                            fontSize = 38.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .background(ChennaiColors.MetroBlue, CircleShape)
                            .border(1.5.dp, ChennaiColors.SurfaceCardElevated, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile Picture",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = session.displayName,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = session.phoneNumber.ifBlank { session.email },
                    fontSize = 12.sp,
                    color = ChennaiColors.TextSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    color = ChennaiColors.MetroBlue.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = ChennaiColors.MetroBlueLight,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Tap to edit personal info & photo",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ChennaiColors.MetroBlueLight
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // CMRL Smartcard Wallet Status Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = ChennaiColors.DeepCanvas,
            border = androidx.compose.foundation.BorderStroke(1.dp, ChennaiColors.SurfaceBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(ChennaiColors.MtcAmber.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CreditCard,
                            contentDescription = null,
                            tint = ChennaiColors.MtcAmber,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = session.metroCardNumber ?: "CMRL-8829-4102",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "CMRL Smartcard Pass",
                            fontSize = 10.sp,
                            color = ChennaiColors.TextSecondary
                        )
                    }
                }

                Text(
                    text = "₹${session.walletBalanceCents / 100}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ChennaiColors.LiveOnlineGreen
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(color = ChennaiColors.SurfaceBorder)

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "MENU & ACTIONS",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = ChennaiColors.TextSecondary,
            letterSpacing = 1.2.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Menu 1: Edit Profile (Personal info, address, phone, photo)
        SidebarMenuItem(
            icon = Icons.Default.Person,
            title = "Edit Profile & Address",
            subtitle = "Update photo, phone, email & address",
            accentColor = ChennaiColors.MetroBlueLight,
            onClick = onEditProfileClick
        )

        // Menu 2: Chennai Metro & Bus Network Map
        SidebarMenuItem(
            icon = Icons.Default.DirectionsSubway,
            title = "Network Map with Stations",
            subtitle = "Blue, Green & Yellow lines with stop names",
            accentColor = ChennaiColors.MtcAmber,
            onClick = onMapClick
        )

        // Menu 2B: MTC Bus Routes & Schemes Directory
        SidebarMenuItem(
            icon = Icons.Default.DirectionsBus,
            title = "MTC Bus Routes & Schemes Hub",
            subtitle = "25+ Chennai routes, stops & Vetri Payanam passes",
            accentColor = ChennaiColors.MtcRed,
            onClick = onMtcDirectoryClick
        )

        // Menu 3: My Offline Passes & Tickets
        SidebarMenuItem(
            icon = Icons.Default.QrCode,
            title = "Offline Passes & QR Tickets",
            subtitle = "Saved journey tickets and QR codes",
            accentColor = ChennaiColors.LiveOnlineGreen,
            onClick = onFareDetailsClick
        )

        // Menu 3: Station Proximity Alarm
        SidebarMenuItem(
            icon = Icons.Default.Notifications,
            title = "500m Station Proximity Alert",
            subtitle = "Zero-API arrival alerts via device GPS",
            accentColor = ChennaiColors.MtcAmber,
            onClick = onFareDetailsClick
        )

        // Menu 4: Vetri Payanam Scheme
        SidebarMenuItem(
            icon = Icons.Default.Star,
            title = "Vetri Payanam Thittam",
            subtitle = "Zero-ticket free bus travel for women on MTC buses",
            accentColor = Color(0xFFF472B6),
            onClick = onMtcDirectoryClick
        )

        // Menu 5: 24/7 Helplines
        SidebarMenuItem(
            icon = Icons.Default.Phone,
            title = "Transit Helplines",
            subtitle = "CMRL: 1800-425-1515 • MTC: 149",
            accentColor = ChennaiColors.MtcRed,
            onClick = {}
        )

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = ChennaiColors.SurfaceBorder)
        Spacer(modifier = Modifier.height(12.dp))

        // Sign Out Menu
        SidebarMenuItem(
            icon = Icons.AutoMirrored.Filled.Logout,
            title = "Sign Out",
            subtitle = "Switch commuter profile",
            accentColor = ChennaiColors.MtcRed,
            onClick = onLogoutClick
        )

        Spacer(modifier = Modifier.weight(1f, fill = false))
        Spacer(modifier = Modifier.height(24.dp))

        // Drawer Footer
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Chennai Transit Companion v2.4",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = ChennaiColors.TextSecondary
            )
            Text(
                text = "CMRL Metro & MTC Offline Engine",
                fontSize = 10.sp,
                color = ChennaiColors.TextSecondary.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun SidebarMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(accentColor.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    color = ChennaiColors.TextSecondary
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = ChennaiColors.TextSecondary.copy(alpha = 0.5f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun TransportModeSelector(
    selectedMode: TransitMode,
    onModeSelected: (TransitMode) -> Unit
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(ChennaiColors.MetroBlue, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("1", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "STEP 1: CHOOSE YOUR MODE OF TRANSPORT",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.ExtraBold,
                color = ChennaiColors.TextPrimary,
                letterSpacing = 1.1.sp
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Please choose whether you are traveling by Chennai Metro or MTC Bus:",
            fontSize = 12.sp,
            color = ChennaiColors.TextSecondary
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Chennai Metro Option (3D Tilt & Specular Sheen)
            val isMetroSelected = selectedMode == TransitMode.SUBWAY
            Surface(
                onClick = { onModeSelected(TransitMode.SUBWAY) },
                shape = RoundedCornerShape(20.dp),
                color = if (isMetroSelected) ChennaiColors.MetroBlue.copy(alpha = 0.18f) else ChennaiColors.SurfaceCard,
                border = androidx.compose.foundation.BorderStroke(
                    if (isMetroSelected) 2.dp else 1.dp,
                    if (isMetroSelected) ChennaiColors.MetroBlue else ChennaiColors.SurfaceBorder
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(108.dp)
                    .interactive3DTilt(maxRotationDegrees = 7f)
                    .then(if (isMetroSelected) Modifier.animatedSpecularSheen() else Modifier)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(if (isMetroSelected) ChennaiColors.MetroBlue else ChennaiColors.SurfaceBorder, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.DirectionsSubway, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        if (isMetroSelected) {
                            Surface(color = ChennaiColors.MetroBlue, shape = RoundedCornerShape(6.dp)) {
                                Text("SELECTED", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                    }

                    Column {
                        Text("Chennai Metro", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
                        Text("Blue • Green • Yellow Lines", fontSize = 11.sp, color = ChennaiColors.TextSecondary)
                    }
                }
            }

            // MTC City Buses Option (3D Tilt & Specular Sheen)
            val isBusSelected = selectedMode == TransitMode.BUS
            Surface(
                onClick = { onModeSelected(TransitMode.BUS) },
                shape = RoundedCornerShape(20.dp),
                color = if (isBusSelected) ChennaiColors.MtcRed.copy(alpha = 0.18f) else ChennaiColors.SurfaceCard,
                border = androidx.compose.foundation.BorderStroke(
                    if (isBusSelected) 2.dp else 1.dp,
                    if (isBusSelected) ChennaiColors.MtcRed else ChennaiColors.SurfaceBorder
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(108.dp)
                    .interactive3DTilt(maxRotationDegrees = 7f)
                    .then(if (isBusSelected) Modifier.animatedSpecularSheen() else Modifier)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(if (isBusSelected) ChennaiColors.MtcRed else ChennaiColors.SurfaceBorder, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.DirectionsBus, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        if (isBusSelected) {
                            Surface(color = ChennaiColors.MtcRed, shape = RoundedCornerShape(6.dp)) {
                                Text("SELECTED", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                    }

                    Column {
                        Text("MTC City Buses", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
                        Text("21G • 570 • 102 • 29C & more", fontSize = 11.sp, color = ChennaiColors.TextSecondary)
                    }
                }
            }
        }
    }
}

@Composable
private fun LineSelectorChips(
    selectedMode: TransitMode,
    routes: List<TransitRoute>,
    selectedRoute: TransitRoute?,
    onRouteSelected: (TransitRoute) -> Unit
) {
    if (routes.isEmpty()) return

    val isMetro = selectedMode == TransitMode.SUBWAY

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(if (isMetro) ChennaiColors.MetroBlueLight else ChennaiColors.MtcAmber, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("2", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isMetro) "STEP 2: CHOOSE CHENNAI METRO LINE (CORRIDOR)" else "STEP 2: CHOOSE MTC BUS ROUTE CORRIDOR",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.ExtraBold,
                color = ChennaiColors.TextPrimary,
                letterSpacing = 1.1.sp
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (isMetro)
                "Choose from Chennai Metro's 3 major operational corridors:"
            else
                "Select one of Chennai's high-frequency bus corridors:",
            fontSize = 12.sp,
            color = ChennaiColors.TextSecondary
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Display the lines with high-fidelity badges
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(routes) { route ->
                val isSelected = selectedRoute?.id == route.id
                val routeColor = runCatching {
                    Color(android.graphics.Color.parseColor(route.colorHex))
                }.getOrDefault(ChennaiColors.MetroBlue)

                Surface(
                    onClick = { onRouteSelected(route) },
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSelected) routeColor else ChennaiColors.SurfaceCard,
                    border = androidx.compose.foundation.BorderStroke(
                        if (isSelected) 1.5.dp else 1.dp,
                        if (isSelected) routeColor else ChennaiColors.SurfaceBorder
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(if (isSelected) Color.White else routeColor, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = route.shortName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (isSelected) (if (route.id == "cmrl_yellow") Color.Black else Color.White) else ChennaiColors.TextPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = route.longName.take(32) + if (route.longName.length > 32) "…" else "",
                            fontSize = 10.sp,
                            color = if (isSelected) (if (route.id == "cmrl_yellow") Color.DarkGray else Color.White.copy(alpha = 0.8f)) else ChennaiColors.TextSecondary
                        )
                    }
                }
            }
        }

        // Contextual Operational Status Callouts
        if (isMetro && selectedRoute?.id == "cmrl_yellow") {
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = ChennaiColors.MtcAmber.copy(alpha = 0.12f),
                border = androidx.compose.foundation.BorderStroke(1.dp, ChennaiColors.MtcAmber.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("⚡", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Yellow Line Status: Open up to Vadapalani",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• Poonamallee Bypass ↔ Porur: Train stops at ALL 10 intermediate stations\n• Porur ↔ Vadapalani: Express non-stop service (no intermediate stops)\n• Vadapalani Metro: Operational terminal & Interchange with Green Line\n• Vadapalani ↔ Lighthouse: Phase 2 extension under construction",
                        fontSize = 11.sp,
                        color = ChennaiColors.TextSecondary,
                        lineHeight = 16.sp
                    )
                }
            }
        } else if (!isMetro) {
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = ChennaiColors.MtcPinkWomen.copy(alpha = 0.12f),
                border = androidx.compose.foundation.BorderStroke(1.dp, ChennaiColors.MtcPinkWomen.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🌸", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Vetri Payanam Thittam (TN Govt)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Free zero-ticket travel for all women, girls and students across MTC Ordinary, Express and Deluxe buses.",
                            fontSize = 11.sp,
                            color = ChennaiColors.TextSecondary,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OriginDestinationInteractiveCard(
    selectedOrigin: TransitStop?,
    selectedDestination: TransitStop?,
    onOpenOriginPicker: () -> Unit,
    onOpenDestinationPicker: () -> Unit,
    onSwapStations: () -> Unit
) {
    var rotationAngle by remember { mutableFloatStateOf(0f) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .interactive3DTilt(maxRotationDegrees = 6f),
        shape = RoundedCornerShape(22.dp),
        color = ChennaiColors.SurfaceCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, ChennaiColors.SurfaceBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // Origin Station Tap Card
                    Surface(
                        onClick = onOpenOriginPicker,
                        shape = RoundedCornerShape(14.dp),
                        color = ChennaiColors.DeepCanvas,
                        border = androidx.compose.foundation.BorderStroke(1.dp, ChennaiColors.SurfaceBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                PulseRadarBeacon(color = ChennaiColors.LiveOnlineGreen, sizeDp = 28)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Starting Point", fontSize = 10.sp, color = ChennaiColors.LiveOnlineGreen, fontWeight = FontWeight.Bold)
                                    Text(
                                        text = selectedOrigin?.name ?: "Tap to choose starting stop",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Color.White
                                    )
                                }
                            }
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = ChennaiColors.TextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Destination Station Tap Card
                    Surface(
                        onClick = onOpenDestinationPicker,
                        shape = RoundedCornerShape(14.dp),
                        color = ChennaiColors.DeepCanvas,
                        border = androidx.compose.foundation.BorderStroke(1.dp, ChennaiColors.SurfaceBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(ChennaiColors.AlertRed.copy(alpha = 0.2f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = ChennaiColors.AlertRed, modifier = Modifier.size(16.dp))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Ending Point", fontSize = 10.sp, color = ChennaiColors.AlertRed, fontWeight = FontWeight.Bold)
                                    Text(
                                        text = selectedDestination?.name ?: "Tap to choose destination stop",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Color.White
                                    )
                                }
                            }
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = ChennaiColors.TextMuted)
                        }
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Swap Button
                IconButton(
                    onClick = {
                        rotationAngle += 180f
                        onSwapStations()
                    },
                    modifier = Modifier
                        .size(46.dp)
                        .background(ChennaiColors.DeepCanvas, CircleShape)
                        .border(1.dp, ChennaiColors.SurfaceBorder, CircleShape)
                ) {
                    val animatedRotation by animateFloatAsState(targetValue = rotationAngle, label = "swapRot")
                    Icon(
                        imageVector = Icons.Default.SwapVert,
                        contentDescription = "Swap Origin & Destination",
                        tint = ChennaiColors.MetroBlueLight,
                        modifier = Modifier.rotate(animatedRotation)
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickStationShortcuts(
    mode: TransitMode,
    allStops: List<TransitStop>,
    onSelectStation: (TransitStop) -> Unit
) {
    val shortcuts = if (mode == TransitMode.SUBWAY) {
        listOf(
            "Poonamallee Bypass (L4)" to "cmrl_y_poonamallee_bypass",
            "Porur Junction (L4)" to "cmrl_y_porur",
            "Vadapalani Metro (L4/L2)" to "cmrl_vadapalani",
            "Central Metro" to "cmrl_central",
            "Airport" to "cmrl_airport",
            "Koyambedu CMBT" to "cmrl_cmbt",
            "Thousand Lights" to "cmrl_thousand_lights",
            "Alandur Interchange" to "cmrl_alandur",
            "Guindy Metro" to "cmrl_guindy"
        )
    } else {
        listOf(
            "Broadway" to "mtc_broadway",
            "Adyar" to "mtc_adyar",
            "Tidel Park" to "mtc_tidel_park",
            "Tambaram" to "mtc_tambaram",
            "Sholinganallur" to "mtc_sholinganallur",
            "CMBT" to "mtc_cmbt",
            "T. Nagar" to "mtc_t_nagar",
            "Perambur" to "mtc_perambur",
            "Kelambakkam" to "mtc_kelambakkam",
            "Poonamallee" to "mtc_poonamallee"
        )
    }

    Column {
        Text(
            text = if (mode == TransitMode.SUBWAY) "POPULAR CHENNAI METRO DESTINATIONS" else "POPULAR MTC BUS DESTINATIONS",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = ChennaiColors.TextMuted,
            fontSize = 10.sp,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(shortcuts) { (label, stopId) ->
                val matchingStop = allStops.firstOrNull { it.id == stopId }
                Surface(
                    onClick = {
                        matchingStop?.let { onSelectStation(it) }
                    },
                    shape = RoundedCornerShape(8.dp),
                    color = ChennaiColors.SurfaceCardElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, ChennaiColors.SurfaceBorder)
                ) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = ChennaiColors.TextSecondary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun FareSummaryBanner(
    fare: com.urbantransit.offline.domain.fare.CalculatedFare,
    onViewBreakdownClick: () -> Unit
) {
    Surface(
        onClick = onViewBreakdownClick,
        shape = RoundedCornerShape(18.dp),
        color = ChennaiColors.SurfaceCardElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, ChennaiColors.MetroBlue.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = fare.formattedTotal,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = ChennaiColors.LiveOnlineGreen.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "${fare.formattedDiscounted} WITH SMARTCARD / QR",
                            color = ChennaiColors.LiveOnlineGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${fare.formattedDistance} • ${fare.transitType}",
                    fontSize = 12.sp,
                    color = ChennaiColors.TextSecondary
                )
            }

            Surface(
                color = ChennaiColors.MetroBlue,
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.ConfirmationNumber, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Fare Details", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
