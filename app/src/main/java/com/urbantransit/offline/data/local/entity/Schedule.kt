package com.urbantransit.offline.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "schedules",
    foreignKeys = [
        ForeignKey(
            entity = TransitRoute::class,
            parentColumns = ["id"],
            childColumns = ["route_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TransitStop::class,
            parentColumns = ["id"],
            childColumns = ["stop_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["route_id"]),
        Index(value = ["stop_id"]),
        Index(value = ["route_id", "stop_id"]),
        Index(value = ["departure_time"])
    ]
)
data class Schedule(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "route_id")
    val routeId: String,

    @ColumnInfo(name = "stop_id")
    val stopId: String,

    @ColumnInfo(name = "arrival_time")
    val arrivalTime: String, // e.g. "08:15"

    @ColumnInfo(name = "departure_time")
    val departureTime: String, // e.g. "08:17"

    @ColumnInfo(name = "stop_sequence")
    val stopSequence: Int,

    @ColumnInfo(name = "days_of_week")
    val daysOfWeek: String = "MON,TUE,WED,THU,FRI,SAT,SUN"
)
