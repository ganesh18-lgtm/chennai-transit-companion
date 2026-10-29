package com.urbantransit.offline.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.urbantransit.offline.data.local.entity.SyncMetadata
import kotlinx.coroutines.flow.Flow

@Dao
interface SyncMetadataDao {

    @Query("SELECT timestamp FROM sync_metadata WHERE `key` = :key LIMIT 1")
    fun getTimestamp(key: String): Flow<Long?>

    @Query("SELECT timestamp FROM sync_metadata WHERE `key` = :key LIMIT 1")
    suspend fun getTimestampSync(key: String): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setTimestamp(metadata: SyncMetadata)
}
