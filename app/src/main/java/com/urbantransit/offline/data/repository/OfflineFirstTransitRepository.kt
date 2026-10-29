package com.urbantransit.offline.data.repository

import androidx.room.withTransaction
import com.urbantransit.offline.data.local.TransitDatabase
import com.urbantransit.offline.data.local.dao.FareRuleDao
import com.urbantransit.offline.data.local.dao.ScheduleDao
import com.urbantransit.offline.data.local.dao.SyncMetadataDao
import com.urbantransit.offline.data.local.dao.TicketDao
import com.urbantransit.offline.data.local.dao.TransitRouteDao
import com.urbantransit.offline.data.local.dao.TransitStopDao
import com.urbantransit.offline.data.local.entity.FareRule
import com.urbantransit.offline.data.local.entity.SyncMetadata
import com.urbantransit.offline.data.local.entity.TicketOrder
import com.urbantransit.offline.data.local.entity.TransitRoute
import com.urbantransit.offline.data.local.entity.TransitStop
import com.urbantransit.offline.data.local.model.ScheduleWithDetails
import com.urbantransit.offline.data.model.Resource
import com.urbantransit.offline.data.model.SyncStatus
import com.urbantransit.offline.data.remote.TransitRemoteDataSource
import com.urbantransit.offline.domain.repository.TransitRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OfflineFirstTransitRepository @Inject constructor(
    private val database: TransitDatabase,
    private val transitRouteDao: TransitRouteDao,
    private val transitStopDao: TransitStopDao,
    private val scheduleDao: ScheduleDao,
    private val fareRuleDao: FareRuleDao,
    private val ticketDao: TicketDao,
    private val syncMetadataDao: SyncMetadataDao,
    private val remoteDataSource: TransitRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TransitRepository {

    override fun getRoutes(): Flow<List<TransitRoute>> =
        transitRouteDao.getAllRoutes()

    override fun getRoute(routeId: String): Flow<TransitRoute?> =
        transitRouteDao.getRouteById(routeId)

    override fun getStops(): Flow<List<TransitStop>> =
        transitStopDao.getAllStops()

    override fun getStopsForRoute(routeId: String): Flow<List<TransitStop>> =
        transitStopDao.getStopsForRoute(routeId)

    override fun getStop(stopId: String): Flow<TransitStop?> =
        transitStopDao.getStopById(stopId)

    override suspend fun getStopById(stopId: String): TransitStop? = withContext(ioDispatcher) {
        try {
            transitStopDao.getStopByIdSync(stopId)
        } catch (e: Exception) {
            null
        }
    }

    override fun getSchedulesForStop(stopId: String): Flow<List<ScheduleWithDetails>> =
        scheduleDao.getSchedulesWithDetailsForStop(stopId)

    override fun getFareRules(): Flow<List<FareRule>> =
        fareRuleDao.getAllFareRules()

    override fun getTickets(): Flow<List<TicketOrder>> =
        ticketDao.getAllTickets()

    override fun getLastScheduleSyncTime(): Flow<Long?> =
        syncMetadataDao.getTimestamp(TransitDatabase.KEY_SCHEDULE_LAST_SYNC)

    init {
        // Guarantee immediate offline availability with Chennai transit seed data
        kotlinx.coroutines.CoroutineScope(ioDispatcher).launch {
            try {
                database.withTransaction {
                    transitRouteDao.insertRoutes(com.urbantransit.offline.data.local.ChennaiTransitSeed.routes)
                    transitStopDao.insertStops(com.urbantransit.offline.data.local.ChennaiTransitSeed.stops)
                    scheduleDao.deleteSchedulesForRoute("cmrl_yellow")
                    scheduleDao.insertSchedules(com.urbantransit.offline.data.local.ChennaiTransitSeed.schedules)
                    fareRuleDao.insertFareRules(com.urbantransit.offline.data.local.ChennaiTransitSeed.fareRules)
                    syncMetadataDao.setTimestamp(
                        SyncMetadata(
                            key = TransitDatabase.KEY_SCHEDULE_LAST_SYNC,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                }
            } catch (_: Exception) {}
        }
    }

    override suspend fun refreshSchedules(): Resource<Unit> = withContext(ioDispatcher) {
        try {
            val payload = remoteDataSource.fetchLatestSchedules()

            // Run database insertions inside an atomic transaction
            database.withTransaction {
                transitRouteDao.insertRoutes(payload.routes)
                transitStopDao.insertStops(payload.stops)
                scheduleDao.deleteSchedulesForRoute("cmrl_yellow")
                scheduleDao.insertSchedules(payload.schedules)
                fareRuleDao.insertFareRules(payload.fareRules)
                syncMetadataDao.setTimestamp(
                    SyncMetadata(
                        key = TransitDatabase.KEY_SCHEDULE_LAST_SYNC,
                        timestamp = payload.serverTimestamp
                    )
                )
            }

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(
                message = "Failed to synchronize transit schedules: ${e.localizedMessage ?: "Unknown error"}",
                cause = e
            )
        }
    }

    override suspend fun purchaseTicket(
        routeId: String,
        originStopId: String,
        destinationStopId: String,
        fareCents: Long
    ): Resource<TicketOrder> = withContext(ioDispatcher) {
        try {
            val route = transitRouteDao.getRouteByIdSync(routeId)
            val origin = transitStopDao.getStopByIdSync(originStopId)
            val destination = transitStopDao.getStopByIdSync(destinationStopId)

            val ticket = TicketOrder(
                id = UUID.randomUUID().toString(),
                routeId = routeId,
                routeName = route?.shortName ?: "Route $routeId",
                originStopId = originStopId,
                originStopName = origin?.name ?: "Stop $originStopId",
                destinationStopId = destinationStopId,
                destinationStopName = destination?.name ?: "Stop $destinationStopId",
                fareCents = fareCents,
                purchasedAtTimestamp = System.currentTimeMillis(),
                syncStatus = SyncStatus.PENDING,
                syncAttemptCount = 0
            )

            ticketDao.insertTicket(ticket)
            Resource.Success(ticket)
        } catch (e: Exception) {
            Resource.Error(
                message = "Failed to store offline ticket purchase: ${e.localizedMessage ?: "Unknown error"}",
                cause = e
            )
        }
    }

    override suspend fun syncPendingTickets(): Resource<Int> = withContext(ioDispatcher) {
        try {
            val pendingTickets = ticketDao.getPendingTicketsSync()
            var successfullySyncedCount = 0

            for (ticket in pendingTickets) {
                ticketDao.updateTicketStatus(ticket.id, SyncStatus.SYNCING)
                try {
                    val response = remoteDataSource.uploadTicket(ticket)
                    if (response.confirmed) {
                        ticketDao.updateTicketStatus(ticket.id, SyncStatus.SYNCED)
                        successfullySyncedCount++
                    } else {
                        ticketDao.incrementAttemptCount(ticket.id)
                        ticketDao.updateTicketStatus(ticket.id, SyncStatus.FAILED, "Backend rejected confirmation")
                    }
                } catch (networkEx: Exception) {
                    ticketDao.incrementAttemptCount(ticket.id)
                    ticketDao.updateTicketStatus(
                        ticket.id,
                        SyncStatus.FAILED,
                        networkEx.localizedMessage ?: "Network error during ticket sync"
                    )
                }
            }

            Resource.Success(successfullySyncedCount)
        } catch (e: Exception) {
            Resource.Error(
                message = "Error during pending ticket synchronization: ${e.localizedMessage ?: "Unknown error"}",
                cause = e
            )
        }
    }
}
