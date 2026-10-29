package com.urbantransit.offline.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.urbantransit.offline.data.local.dao.FareRuleDao
import com.urbantransit.offline.data.local.dao.ScheduleDao
import com.urbantransit.offline.data.local.dao.SyncMetadataDao
import com.urbantransit.offline.data.local.dao.TicketDao
import com.urbantransit.offline.data.local.dao.TransitRouteDao
import com.urbantransit.offline.data.local.dao.TransitStopDao
import com.urbantransit.offline.data.local.entity.FareRule
import com.urbantransit.offline.data.local.entity.Schedule
import com.urbantransit.offline.data.local.entity.SyncMetadata
import com.urbantransit.offline.data.local.entity.TicketOrder
import com.urbantransit.offline.data.local.entity.TransitRoute
import com.urbantransit.offline.data.local.entity.TransitStop

@Database(
    entities = [
        TransitRoute::class,
        TransitStop::class,
        Schedule::class,
        FareRule::class,
        TicketOrder::class,
        SyncMetadata::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(TransitTypeConverters::class)
abstract class TransitDatabase : RoomDatabase() {

    abstract fun transitRouteDao(): TransitRouteDao
    abstract fun transitStopDao(): TransitStopDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun fareRuleDao(): FareRuleDao
    abstract fun ticketDao(): TicketDao
    abstract fun syncMetadataDao(): SyncMetadataDao

    companion object {
        const val DATABASE_NAME = "urban_transit.db"
        const val KEY_SCHEDULE_LAST_SYNC = "schedule_last_sync_timestamp"
    }
}
