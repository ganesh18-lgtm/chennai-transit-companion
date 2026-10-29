package com.urbantransit.offline.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.urbantransit.offline.data.model.Resource
import com.urbantransit.offline.domain.repository.TransitRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.IOException

/**
 * Background worker responsible for periodically synchronizing transit schedules, routes, stops,
 * and fare rules over an UNMETERED network connection (Wi-Fi).
 *
 * Updates Room database atomically using OnConflictStrategy.REPLACE.
 */
@HiltWorker
class ScheduleSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: TransitRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            when (val result = repository.refreshSchedules()) {
                is Resource.Success -> {
                    Result.success(
                        workDataOf(
                            KEY_SYNC_TIMESTAMP to System.currentTimeMillis(),
                            KEY_STATUS_MESSAGE to "Transit schedules successfully refreshed"
                        )
                    )
                }
                is Resource.Error -> {
                    if (runAttemptCount < MAX_RETRY_COUNT) {
                        Result.retry()
                    } else {
                        Result.failure(
                            workDataOf(
                                KEY_ERROR_MESSAGE to result.message
                            )
                        )
                    }
                }
                is Resource.Loading -> Result.retry()
            }
        } catch (ioException: IOException) {
            if (runAttemptCount < MAX_RETRY_COUNT) {
                Result.retry()
            } else {
                Result.failure(
                    workDataOf(KEY_ERROR_MESSAGE to (ioException.localizedMessage ?: "Network I/O error"))
                )
            }
        } catch (e: Exception) {
            Result.failure(
                workDataOf(KEY_ERROR_MESSAGE to (e.localizedMessage ?: "Unexpected synchronization failure"))
            )
        }
    }

    companion object {
        const val WORK_NAME = "periodic_schedule_sync_worker"
        const val KEY_SYNC_TIMESTAMP = "key_sync_timestamp"
        const val KEY_STATUS_MESSAGE = "key_status_message"
        const val KEY_ERROR_MESSAGE = "key_error_message"
        private const val MAX_RETRY_COUNT = 3
    }
}
