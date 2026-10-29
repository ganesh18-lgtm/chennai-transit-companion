package com.urbantransit.offline.data.local

import androidx.room.TypeConverter
import com.urbantransit.offline.data.model.SyncStatus
import com.urbantransit.offline.data.model.TransitMode

class TransitTypeConverters {

    @TypeConverter
    fun fromTransitMode(mode: TransitMode?): String? = mode?.name

    @TypeConverter
    fun toTransitMode(value: String?): TransitMode? {
        return value?.let {
            runCatching { TransitMode.valueOf(it) }.getOrDefault(TransitMode.BUS)
        }
    }

    @TypeConverter
    fun fromSyncStatus(status: SyncStatus?): String? = status?.name

    @TypeConverter
    fun toSyncStatus(value: String?): SyncStatus? {
        return value?.let {
            runCatching { SyncStatus.valueOf(it) }.getOrDefault(SyncStatus.PENDING)
        }
    }
}
