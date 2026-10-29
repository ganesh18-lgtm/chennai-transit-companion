package com.urbantransit.offline.presentation

import com.urbantransit.offline.data.local.entity.FareRule
import com.urbantransit.offline.data.local.entity.TicketOrder
import com.urbantransit.offline.data.local.entity.TransitRoute
import com.urbantransit.offline.data.local.entity.TransitStop
import com.urbantransit.offline.data.local.model.ScheduleWithDetails
import com.urbantransit.offline.domain.fare.CalculatedFare

data class TransitUiState(
    val isLoading: Boolean = true,
    val isOnline: Boolean = true,
    val lastSyncTimestamp: Long? = null,
    val lastSyncFormatted: String = "Never",
    val routes: List<TransitRoute> = emptyList(),
    val selectedRoute: TransitRoute? = null,
    val stopsForSelectedRoute: List<TransitStop> = emptyList(),
    val allStops: List<TransitStop> = emptyList(),
    val selectedOriginStop: TransitStop? = null,
    val selectedDestinationStop: TransitStop? = null,
    val upcomingSchedules: List<ScheduleWithDetails> = emptyList(),
    val fareRules: List<FareRule> = emptyList(),
    val calculatedFare: CalculatedFare? = null,
    val activeGeofenceStopId: String? = null,
    val recentTickets: List<TicketOrder> = emptyList(),
    val userMessage: String? = null,
    val isSyncing: Boolean = false
)
