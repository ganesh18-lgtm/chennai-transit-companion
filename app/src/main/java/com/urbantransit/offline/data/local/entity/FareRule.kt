package com.urbantransit.offline.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.urbantransit.offline.data.model.TransitMode

@Entity(
    tableName = "fare_rules",
    indices = [
        Index(value = ["mode"]),
        Index(value = ["origin_zone_id", "destination_zone_id"])
    ]
)
data class FareRule(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "mode")
    val mode: TransitMode,

    @ColumnInfo(name = "origin_zone_id")
    val originZoneId: String? = null,

    @ColumnInfo(name = "destination_zone_id")
    val destinationZoneId: String? = null,

    @ColumnInfo(name = "base_fare_cents")
    val baseFareCents: Long,

    @ColumnInfo(name = "per_km_fare_cents")
    val perKmFareCents: Long = 0L,

    @ColumnInfo(name = "peak_surcharge_cents")
    val peakSurchargeCents: Long = 0L,

    @ColumnInfo(name = "transfer_window_minutes")
    val transferWindowMinutes: Int = 90
)
