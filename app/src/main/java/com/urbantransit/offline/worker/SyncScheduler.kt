package com.urbantransit.offline.worker

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

interface SyncScheduler {
    fun schedulePeriodicScheduleSync()
    fun triggerImmediateScheduleSync()
    fun triggerImmediateTicketSync()
    fun getScheduleSyncWorkInfo(): Flow<List<WorkInfo>>
    fun getTicketSyncWorkInfo(): Flow<List<WorkInfo>>
}

@Singleton
class WorkManagerSyncScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val workManager: WorkManager
) : SyncScheduler {

    override fun schedulePeriodicScheduleSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED) // Mandatory Wi-Fi constraint
            .setRequiresBatteryNotLow(true)
            .setRequiresStorageNotLow(true)
            .build()

        val periodicSyncRequest = PeriodicWorkRequestBuilder<ScheduleSyncWorker>(
            repeatInterval = 12,
            repeatIntervalTimeUnit = TimeUnit.HOURS,
            flexTimeInterval = 1,
            flexTimeIntervalUnit = TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                15,
                TimeUnit.MINUTES
            )
            .addTag(TAG_SCHEDULE_SYNC)
            .build()

        workManager.enqueueUniquePeriodicWork(
            ScheduleSyncWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicSyncRequest
        )
    }

    override fun triggerImmediateScheduleSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val oneTimeScheduleRequest = OneTimeWorkRequestBuilder<ScheduleSyncWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                30,
                TimeUnit.SECONDS
            )
            .addTag(TAG_SCHEDULE_SYNC_EXPEDITED)
            .build()

        workManager.enqueueUniqueWork(
            "${ScheduleSyncWorker.WORK_NAME}_immediate",
            ExistingWorkPolicy.REPLACE,
            oneTimeScheduleRequest
        )
    }

    override fun triggerImmediateTicketSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // Mandatory active network connection
            .build()

        val ticketSyncRequest = OneTimeWorkRequestBuilder<TicketSyncWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                15,
                TimeUnit.SECONDS
            )
            .addTag(TAG_TICKET_SYNC)
            .build()

        workManager.enqueueUniqueWork(
            TicketSyncWorker.WORK_NAME,
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            ticketSyncRequest
        )
    }

    override fun getScheduleSyncWorkInfo(): Flow<List<WorkInfo>> =
        workManager.getWorkInfosForUniqueWorkFlow(ScheduleSyncWorker.WORK_NAME)

    override fun getTicketSyncWorkInfo(): Flow<List<WorkInfo>> =
        workManager.getWorkInfosForUniqueWorkFlow(TicketSyncWorker.WORK_NAME)

    companion object {
        const val TAG_SCHEDULE_SYNC = "tag_schedule_sync"
        const val TAG_SCHEDULE_SYNC_EXPEDITED = "tag_schedule_sync_expedited"
        const val TAG_TICKET_SYNC = "tag_ticket_sync"
    }
}
