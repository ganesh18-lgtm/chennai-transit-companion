package com.urbantransit.offline.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.urbantransit.offline.data.local.entity.TransitRoute
import kotlinx.coroutines.flow.Flow

@Dao
interface TransitRouteDao {

    @Query("SELECT * FROM transit_routes ORDER BY short_name ASC")
    fun getAllRoutes(): Flow<List<TransitRoute>>

    @Query("SELECT * FROM transit_routes WHERE id = :routeId LIMIT 1")
    fun getRouteById(routeId: String): Flow<TransitRoute?>

    @Query("SELECT * FROM transit_routes WHERE id = :routeId LIMIT 1")
    suspend fun getRouteByIdSync(routeId: String): TransitRoute?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutes(routes: List<TransitRoute>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoute(route: TransitRoute)

    @Query("DELETE FROM transit_routes")
    suspend fun deleteAllRoutes()
}
