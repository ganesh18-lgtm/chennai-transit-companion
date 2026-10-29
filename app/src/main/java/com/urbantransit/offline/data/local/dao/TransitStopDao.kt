package com.urbantransit.offline.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.urbantransit.offline.data.local.entity.TransitStop
import kotlinx.coroutines.flow.Flow

@Dao
interface TransitStopDao {

    @Query("SELECT * FROM transit_stops ORDER BY name ASC")
    fun getAllStops(): Flow<List<TransitStop>>

    @Query("SELECT * FROM transit_stops WHERE id = :stopId LIMIT 1")
    fun getStopById(stopId: String): Flow<TransitStop?>

    @Query("SELECT * FROM transit_stops WHERE id = :stopId LIMIT 1")
    suspend fun getStopByIdSync(stopId: String): TransitStop?

    @Query("""
        SELECT DISTINCT s.* FROM transit_stops s
        INNER JOIN schedules sch ON s.id = sch.stop_id
        WHERE sch.route_id = :routeId
        ORDER BY sch.stop_sequence ASC
    """)
    fun getStopsForRoute(routeId: String): Flow<List<TransitStop>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStops(stops: List<TransitStop>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStop(stop: TransitStop)

    @Query("DELETE FROM transit_stops")
    suspend fun deleteAllStops()
}
