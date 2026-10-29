package com.urbantransit.offline.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transit_stops",
    indices = [
        Index(value = ["zone_id"]),
        Index(value = ["latitude", "longitude"])
    ]
)
data class TransitStop(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "latitude")
    val latitude: Double,

    @ColumnInfo(name = "longitude")
    val longitude: Double,

    @ColumnInfo(name = "zone_id")
    val zoneId: String,

    @ColumnInfo(name = "wheelchair_accessible")
    val wheelchairAccessible: Boolean = true
)
