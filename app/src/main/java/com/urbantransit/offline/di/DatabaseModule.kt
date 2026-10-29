package com.urbantransit.offline.di

import android.content.Context
import androidx.room.Room
import com.urbantransit.offline.data.local.TransitDatabase
import com.urbantransit.offline.data.local.dao.FareRuleDao
import com.urbantransit.offline.data.local.dao.ScheduleDao
import com.urbantransit.offline.data.local.dao.SyncMetadataDao
import com.urbantransit.offline.data.local.dao.TicketDao
import com.urbantransit.offline.data.local.dao.TransitRouteDao
import com.urbantransit.offline.data.local.dao.TransitStopDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTransitDatabase(
        @ApplicationContext context: Context
    ): TransitDatabase {
        return Room.databaseBuilder(
            context,
            TransitDatabase::class.java,
            TransitDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideTransitRouteDao(db: TransitDatabase): TransitRouteDao = db.transitRouteDao()

    @Provides
    fun provideTransitStopDao(db: TransitDatabase): TransitStopDao = db.transitStopDao()

    @Provides
    fun provideScheduleDao(db: TransitDatabase): ScheduleDao = db.scheduleDao()

    @Provides
    fun provideFareRuleDao(db: TransitDatabase): FareRuleDao = db.fareRuleDao()

    @Provides
    fun provideTicketDao(db: TransitDatabase): TicketDao = db.ticketDao()

    @Provides
    fun provideSyncMetadataDao(db: TransitDatabase): SyncMetadataDao = db.syncMetadataDao()

    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
