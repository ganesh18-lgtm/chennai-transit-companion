package com.urbantransit.offline.data.remote

import com.urbantransit.offline.data.local.ChennaiTransitSeed
import com.urbantransit.offline.data.local.entity.FareRule
import com.urbantransit.offline.data.local.entity.Schedule
import com.urbantransit.offline.data.local.entity.TicketOrder
import com.urbantransit.offline.data.local.entity.TransitRoute
import com.urbantransit.offline.data.local.entity.TransitStop
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

data class TransitSyncPayload(
    val routes: List<TransitRoute>,
    val stops: List<TransitStop>,
    val schedules: List<Schedule>,
    val fareRules: List<FareRule>,
    val serverTimestamp: Long
)

data class TicketSyncResponse(
    val ticketId: String,
    val confirmed: Boolean,
    val confirmationCode: String,
    val syncedAt: Long
)

interface TransitRemoteDataSource {
    suspend fun fetchLatestSchedules(): TransitSyncPayload
    suspend fun uploadTicket(ticket: TicketOrder): TicketSyncResponse
}

@Singleton
class TransitRemoteDataSourceImpl @Inject constructor() : TransitRemoteDataSource {

    override suspend fun fetchLatestSchedules(): TransitSyncPayload {
        delay(350) // Simulated network handshake
        return TransitSyncPayload(
            routes = ChennaiTransitSeed.routes,
            stops = ChennaiTransitSeed.stops,
            schedules = ChennaiTransitSeed.schedules,
            fareRules = ChennaiTransitSeed.fareRules,
            serverTimestamp = System.currentTimeMillis()
        )
    }

    override suspend fun uploadTicket(ticket: TicketOrder): TicketSyncResponse {
        delay(400)
        return TicketSyncResponse(
            ticketId = ticket.id,
            confirmed = true,
            confirmationCode = "CHN-TRANSIT-${ticket.id.takeLast(6).uppercase()}",
            syncedAt = System.currentTimeMillis()
        )
    }
}
