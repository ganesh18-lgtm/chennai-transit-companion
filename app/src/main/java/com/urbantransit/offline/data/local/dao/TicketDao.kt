package com.urbantransit.offline.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.urbantransit.offline.data.local.entity.TicketOrder
import com.urbantransit.offline.data.model.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDao {

    @Query("SELECT * FROM ticket_orders ORDER BY purchased_at_timestamp DESC")
    fun getAllTickets(): Flow<List<TicketOrder>>

    @Query("SELECT * FROM ticket_orders WHERE sync_status = :status ORDER BY purchased_at_timestamp ASC")
    suspend fun getTicketsByStatusSync(status: SyncStatus): List<TicketOrder>

    @Query("SELECT * FROM ticket_orders WHERE sync_status IN ('PENDING', 'FAILED') ORDER BY purchased_at_timestamp ASC")
    fun getPendingTickets(): Flow<List<TicketOrder>>

    @Query("SELECT * FROM ticket_orders WHERE sync_status IN ('PENDING', 'FAILED') ORDER BY purchased_at_timestamp ASC")
    suspend fun getPendingTicketsSync(): List<TicketOrder>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTicket(ticket: TicketOrder)

    @Query("UPDATE ticket_orders SET sync_status = :status, last_sync_error = :error WHERE id = :ticketId")
    suspend fun updateTicketStatus(ticketId: String, status: SyncStatus, error: String? = null)

    @Query("UPDATE ticket_orders SET sync_attempt_count = sync_attempt_count + 1 WHERE id = :ticketId")
    suspend fun incrementAttemptCount(ticketId: String)
}
