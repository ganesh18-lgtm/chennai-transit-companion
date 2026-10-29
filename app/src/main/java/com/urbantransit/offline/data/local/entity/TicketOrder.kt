package com.urbantransit.offline.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.urbantransit.offline.data.model.SyncStatus

@Entity(
    tableName = "ticket_orders",
    indices = [
        Index(value = ["sync_status"]),
        Index(value = ["purchased_at_timestamp"])
    ]
)
data class TicketOrder(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "route_id")
    val routeId: String,

    @ColumnInfo(name = "route_name")
    val routeName: String,

    @ColumnInfo(name = "origin_stop_id")
    val originStopId: String,

    @ColumnInfo(name = "origin_stop_name")
    val originStopName: String,

    @ColumnInfo(name = "destination_stop_id")
    val destinationStopId: String,

    @ColumnInfo(name = "destination_stop_name")
    val destinationStopName: String,

    @ColumnInfo(name = "fare_cents")
    val fareCents: Long,

    @ColumnInfo(name = "purchased_at_timestamp")
    val purchasedAtTimestamp: Long,

    @ColumnInfo(name = "sync_status")
    val syncStatus: SyncStatus = SyncStatus.PENDING,

    @ColumnInfo(name = "sync_attempt_count")
    val syncAttemptCount: Int = 0,

    @ColumnInfo(name = "last_sync_error")
    val lastSyncError: String? = null
)
