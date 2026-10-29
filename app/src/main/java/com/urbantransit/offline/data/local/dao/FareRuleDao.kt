package com.urbantransit.offline.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.urbantransit.offline.data.local.entity.FareRule
import com.urbantransit.offline.data.model.TransitMode
import kotlinx.coroutines.flow.Flow

@Dao
interface FareRuleDao {

    @Query("SELECT * FROM fare_rules")
    fun getAllFareRules(): Flow<List<FareRule>>

    @Query("SELECT * FROM fare_rules")
    suspend fun getAllFareRulesSync(): List<FareRule>

    @Query("SELECT * FROM fare_rules WHERE mode = :mode")
    fun getFareRulesForMode(mode: TransitMode): Flow<List<FareRule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFareRules(rules: List<FareRule>)

    @Query("DELETE FROM fare_rules")
    suspend fun deleteAllFareRules()
}
