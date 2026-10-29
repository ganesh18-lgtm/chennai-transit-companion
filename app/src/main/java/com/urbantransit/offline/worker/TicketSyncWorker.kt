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
 * Background worker responsible for immediate one-time syncing of offline ticket purchases
 * whenever a network connection (CONNECTED) becomes available.
 */
@HiltWorker
class TicketSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: TransitRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            when (val result = repository.syncPendingTickets()) {
                is Resource.Success -> {
                    Result.success(
                        workDataOf(
                            KEY_SYNCED_COUNT to result.data,
                            KEY_STATUS_MESSAGE to "Successfully synchronized ${result.data} offline ticket(s)"
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
                    workDataOf(KEY_ERROR_MESSAGE to (ioException.localizedMessage ?: "Network connection error"))
                )
            }
        } catch (e: Exception) {
            Result.failure(
                workDataOf(KEY_ERROR_MESSAGE to (e.localizedMessage ?: "Unexpected ticket sync failure"))
            )
        }
    }

    companion object {
        const val WORK_NAME = "one_time_ticket_sync_worker"
        const val KEY_SYNCED_COUNT = "key_synced_count"
        const val KEY_STATUS_MESSAGE = "key_status_message"
        const val KEY_ERROR_MESSAGE = "key_error_message"
        private const val MAX_RETRY_COUNT = 5
    }
}
