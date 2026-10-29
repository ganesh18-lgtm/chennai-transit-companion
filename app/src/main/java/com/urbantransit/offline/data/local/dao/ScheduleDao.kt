package com.urbantransit.offline.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.urbantransit.offline.data.local.entity.Schedule
import com.urbantransit.offline.data.local.model.ScheduleWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Query("SELECT * FROM schedules ORDER BY departure_time ASC")
    fun getAllSchedules(): Flow<List<Schedule>>

    @Query("SELECT * FROM schedules WHERE route_id = :routeId ORDER BY stop_sequence ASC, departure_time ASC")
    fun getSchedulesForRoute(routeId: String): Flow<List<Schedule>>

    @Query("SELECT * FROM schedules WHERE stop_id = :stopId ORDER BY departure_time ASC")
    fun getSchedulesForStop(stopId: String): Flow<List<Schedule>>

    @Transaction
    @Query("SELECT * FROM schedules WHERE stop_id = :stopId ORDER BY departure_time ASC")
    fun getSchedulesWithDetailsForStop(stopId: String): Flow<List<ScheduleWithDetails>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedules(schedules: List<Schedule>)

    @Query("DELETE FROM schedules WHERE route_id = :routeId")
    suspend fun deleteSchedulesForRoute(routeId: String)

    @Query("DELETE FROM schedules")
    suspend fun deleteAllSchedules()
}
