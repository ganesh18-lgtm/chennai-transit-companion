package com.urbantransit.offline.domain.repository

import com.urbantransit.offline.data.local.entity.FareRule
import com.urbantransit.offline.data.local.entity.Schedule
import com.urbantransit.offline.data.local.entity.TicketOrder
import com.urbantransit.offline.data.local.entity.TransitRoute
import com.urbantransit.offline.data.local.entity.TransitStop
import com.urbantransit.offline.data.local.model.ScheduleWithDetails
import com.urbantransit.offline.data.model.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Domain repository contract enforcing offline-first architecture.
 *
 * All reading operations return cold Kotlin [Flow]s originating strictly
 * from the local Room database (Single Source of Truth).
 * Remote operations write to Room; the UI only observes changes reactively.
 */
interface TransitRepository {

    /**
     * Observes all available transit routes sorted by short name.
     */
    fun getRoutes(): Flow<List<TransitRoute>>

    /**
     * Observes a specific transit route by ID.
     */
    fun getRoute(routeId: String): Flow<TransitRoute?>

    /**
     * Observes all stops registered across all transit lines.
     */
    fun getStops(): Flow<List<TransitStop>>

    /**
     * Observes stops traversed by a specific route in sequence.
     */
    fun getStopsForRoute(routeId: String): Flow<List<TransitStop>>

    /**
     * Observes a single transit stop by ID.
     */
    fun getStop(stopId: String): Flow<TransitStop?>

    /**
     * Directly queries a stop for synchronous use cases (e.g. Geofence setup).
     */
    suspend fun getStopById(stopId: String): TransitStop?

    /**
     * Observes schedules and departing routes for a particular stop.
     */
    fun getSchedulesForStop(stopId: String): Flow<List<ScheduleWithDetails>>

    /**
     * Observes fare matrix calculation rules.
     */
    fun getFareRules(): Flow<List<FareRule>>

    /**
     * Observes user ticket purchases and synchronization statuses.
     */
    fun getTickets(): Flow<List<TicketOrder>>

    /**
     * Observes the timestamp (epoch ms) of the last successful schedule sync.
     */
    fun getLastScheduleSyncTime(): Flow<Long?>

    /**
     * Synchronizes routes, stops, schedules, and fare rules from the remote endpoint,
     * writing them directly to Room with [OnConflictStrategy.REPLACE].
     */
    suspend fun refreshSchedules(): Resource<Unit>

    /**
     * Stores a new ticket purchase locally with [SyncStatus.PENDING], immediately available offline.
     */
    suspend fun purchaseTicket(
        routeId: String,
        originStopId: String,
        destinationStopId: String,
        fareCents: Long
    ): Resource<TicketOrder>

    /**
     * Uploads all tickets with PENDING or FAILED statuses to the backend.
     * Updates each ticket's state to SYNCED upon confirmation.
     */
    suspend fun syncPendingTickets(): Resource<Int>
}
