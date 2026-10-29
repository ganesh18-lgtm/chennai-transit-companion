package com.urbantransit.offline.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.urbantransit.offline.data.auth.AuthManager
import com.urbantransit.offline.data.local.entity.TransitRoute
import com.urbantransit.offline.data.local.entity.TransitStop
import com.urbantransit.offline.data.model.Resource
import com.urbantransit.offline.domain.fare.FareCalculator
import com.urbantransit.offline.domain.model.UserSession
import com.urbantransit.offline.domain.repository.TransitRepository
import com.urbantransit.offline.location.GeofenceManager
import com.urbantransit.offline.util.ConnectivityObserver
import com.urbantransit.offline.worker.SyncScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

enum class AppScreen {
    AUTH,
    HOME,
    FARE_DETAILS,
    PROFILE,
    NETWORK_MAP,
    MTC_DIRECTORY
}

@HiltViewModel
class TransitViewModel @Inject constructor(
    private val repository: TransitRepository,
    private val connectivityObserver: ConnectivityObserver,
    private val fareCalculator: FareCalculator,
    private val geofenceManager: GeofenceManager,
    private val syncScheduler: SyncScheduler,
    private val authManager: AuthManager
) : ViewModel() {

    val session: StateFlow<UserSession> = authManager.session

    private val _currentScreen = MutableStateFlow(
        if (authManager.session.value.isLoggedIn) AppScreen.HOME else AppScreen.AUTH
    )
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _uiState = MutableStateFlow(TransitUiState())
    val uiState: StateFlow<TransitUiState> = _uiState.asStateFlow()

    private val timeFormatter = SimpleDateFormat("h:mm a, MMM d", Locale.getDefault())

    init {
        observeNetworkConnectivity()
        observeDatabaseData()
        observeSyncTimestamp()
        observeTickets()

        // Seed initial schedules if local database is empty
        viewModelScope.launch {
            repository.refreshSchedules()
        }
    }

    private fun observeNetworkConnectivity() {
        viewModelScope.launch {
            connectivityObserver.observe().collectLatest { status ->
                val isOnline = status == ConnectivityObserver.Status.AVAILABLE
                _uiState.update { currentState ->
                    currentState.copy(isOnline = isOnline)
                }

                if (isOnline) {
                    syncScheduler.triggerImmediateTicketSync()
                }
            }
        }
    }

    private fun observeDatabaseData() {
        viewModelScope.launch {
            combine(
                repository.getRoutes(),
                repository.getStops(),
                repository.getFareRules()
            ) { routes, stops, rules ->
                Triple(routes, stops, rules)
            }.collectLatest { (routes, stops, rules) ->
                _uiState.update { current ->
                    val selectedRoute = current.selectedRoute ?: routes.firstOrNull()
                    val selectedOrigin = current.selectedOriginStop ?: stops.firstOrNull { it.id == "cmrl_central" } ?: stops.firstOrNull()
                    val selectedDestination = current.selectedDestinationStop ?: stops.firstOrNull { it.id == "cmrl_airport" } ?: stops.getOrNull(1)

                    current.copy(
                        isLoading = false,
                        routes = routes,
                        allStops = stops,
                        fareRules = rules,
                        selectedRoute = selectedRoute,
                        selectedOriginStop = selectedOrigin,
                        selectedDestinationStop = selectedDestination
                    )
                }
                updateRouteStopsAndSchedules()
                recalculateFare()
            }
        }
    }

    private fun observeSyncTimestamp() {
        viewModelScope.launch {
            repository.getLastScheduleSyncTime().collectLatest { timestamp ->
                val formatted = timestamp?.let {
                    timeFormatter.format(Date(it))
                } ?: "Never"
                _uiState.update { it.copy(lastSyncTimestamp = timestamp, lastSyncFormatted = formatted) }
            }
        }
    }

    private fun observeTickets() {
        viewModelScope.launch {
            repository.getTickets().collectLatest { tickets ->
                _uiState.update { it.copy(recentTickets = tickets) }
            }
        }
    }

    fun login(identifier: String) {
        if (authManager.loginWithMobileOrEmail(identifier)) {
            _currentScreen.value = AppScreen.HOME
        }
    }

    fun loginAsGuest() {
        authManager.loginAsGuest()
        _currentScreen.value = AppScreen.HOME
    }

    fun logout() {
        authManager.logout()
        _currentScreen.value = AppScreen.AUTH
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun updateUserProfile(
        name: String,
        email: String,
        phoneNumber: String,
        address: String,
        metroCardNumber: String,
        emergencyContact: String,
        avatarEmoji: String,
        concessionCategory: String,
        photoUri: String? = null
    ) {
        authManager.updateProfile(
            name = name,
            email = email,
            phoneNumber = phoneNumber,
            address = address,
            metroCardNumber = metroCardNumber,
            emergencyContact = emergencyContact,
            avatarEmoji = avatarEmoji,
            concessionCategory = concessionCategory,
            photoUri = photoUri
        )
        _uiState.update {
            it.copy(userMessage = "Profile updated for $name! Changes saved offline.")
        }
    }

    fun onRouteSelected(route: TransitRoute) {
        _uiState.update { it.copy(selectedRoute = route) }
        updateRouteStopsAndSchedules()
        recalculateFare()
    }

    fun onOriginStopSelected(stop: TransitStop) {
        _uiState.update { it.copy(selectedOriginStop = stop) }
        loadSchedulesForStop(stop.id)
        recalculateFare()
    }

    fun onDestinationStopSelected(stop: TransitStop) {
        _uiState.update { it.copy(selectedDestinationStop = stop) }
        recalculateFare()
    }

    private fun updateRouteStopsAndSchedules() {
        val currentRoute = _uiState.value.selectedRoute ?: return
        viewModelScope.launch {
            repository.getStopsForRoute(currentRoute.id).collectLatest { stops ->
                _uiState.update { current ->
                    val orig = if (stops.any { it.id == current.selectedOriginStop?.id }) {
                        current.selectedOriginStop
                    } else {
                        stops.firstOrNull()
                    }
                    val dest = if (stops.any { it.id == current.selectedDestinationStop?.id }) {
                        current.selectedDestinationStop
                    } else {
                        stops.lastOrNull()
                    }
                    current.copy(
                        stopsForSelectedRoute = stops,
                        selectedOriginStop = orig,
                        selectedDestinationStop = dest
                    )
                }
                val origin = _uiState.value.selectedOriginStop
                if (origin != null) {
                    loadSchedulesForStop(origin.id)
                }
                recalculateFare()
            }
        }
    }

    private fun loadSchedulesForStop(stopId: String) {
        viewModelScope.launch {
            repository.getSchedulesForStop(stopId).collectLatest { schedules ->
                _uiState.update { it.copy(upcomingSchedules = schedules) }
            }
        }
    }

    private fun recalculateFare() {
        val state = _uiState.value
        val route = state.selectedRoute ?: return
        val origin = state.selectedOriginStop ?: return
        val destination = state.selectedDestinationStop ?: return

        if (origin.id == destination.id) {
            _uiState.update { it.copy(calculatedFare = null) }
            return
        }

        val calculated = fareCalculator.calculateFare(
            route = route,
            originStop = origin,
            destinationStop = destination,
            rules = state.fareRules
        )

        _uiState.update { it.copy(calculatedFare = calculated) }
    }

    fun purchaseOfflineTicket() {
        val state = _uiState.value
        val route = state.selectedRoute ?: return
        val origin = state.selectedOriginStop ?: return
        val destination = state.selectedDestinationStop ?: return
        val fare = state.calculatedFare ?: return

        viewModelScope.launch {
            when (val result = repository.purchaseTicket(
                routeId = route.id,
                originStopId = origin.id,
                destinationStopId = destination.id,
                fareCents = fare.totalCents
            )) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(userMessage = "Chennai Transit ticket saved to offline wallet! Sync will occur automatically.")
                    }
                    if (state.isOnline) {
                        syncScheduler.triggerImmediateTicketSync()
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(userMessage = "Failed to store ticket: ${result.message}")
                    }
                }
                is Resource.Loading -> Unit
            }
        }
    }

    fun toggleGeofenceForDestination() {
        val destination = _uiState.value.selectedDestinationStop ?: return
        val isCurrentlyActive = _uiState.value.activeGeofenceStopId == destination.id

        viewModelScope.launch {
            if (isCurrentlyActive) {
                when (val result = geofenceManager.removeStopGeofence(destination.id)) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                activeGeofenceStopId = null,
                                userMessage = "Arrival alert removed for ${destination.name}"
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(userMessage = result.message) }
                    }
                    is Resource.Loading -> Unit
                }
            } else {
                when (val result = geofenceManager.addStopGeofence(destination, 500f)) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                activeGeofenceStopId = destination.id,
                                userMessage = "500m Arrival Alert Set! You will be alerted before reaching ${destination.name}."
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(userMessage = result.message) }
                    }
                    is Resource.Loading -> Unit
                }
            }
        }
    }

    fun manualRefresh() {
        _uiState.update { it.copy(isSyncing = true) }
        viewModelScope.launch {
            when (val refreshResult = repository.refreshSchedules()) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isSyncing = false,
                            userMessage = "Chennai Metro & MTC schedules refreshed from network"
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isSyncing = false,
                            userMessage = "Offline Mode: ${refreshResult.message}"
                        )
                    }
                }
                is Resource.Loading -> Unit
            }
        }
    }

    fun clearUserMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }
}
