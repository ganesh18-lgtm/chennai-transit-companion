package com.urbantransit.offline.data.local.model

import androidx.room.Embedded
import androidx.room.Relation
import com.urbantransit.offline.data.local.entity.Schedule
import com.urbantransit.offline.data.local.entity.TransitRoute
import com.urbantransit.offline.data.local.entity.TransitStop

data class ScheduleWithDetails(
    @Embedded
    val schedule: Schedule,

    @Relation(
        parentColumn = "route_id",
        entityColumn = "id"
    )
    val route: TransitRoute,

    @Relation(
        parentColumn = "stop_id",
        entityColumn = "id"
    )
    val stop: TransitStop
)
