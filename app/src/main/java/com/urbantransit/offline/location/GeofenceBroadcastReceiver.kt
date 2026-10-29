package com.urbantransit.offline.location

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import com.urbantransit.offline.domain.repository.TransitRepository
import com.urbantransit.offline.notification.TransitNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class GeofenceBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: TransitRepository

    @Inject
    lateinit var notificationManager: TransitNotificationManager

    private val receiverScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent) ?: return

        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
            android.util.Log.e("GeofenceReceiver", "Geofence event error: $errorMessage")
            return
        }

        val transitionType = geofencingEvent.geofenceTransition
        if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER ||
            transitionType == Geofence.GEOFENCE_TRANSITION_DWELL
        ) {
            val triggeringGeofences = geofencingEvent.triggeringGeofences ?: return

            // Use goAsync() so coroutine operations do not block the broadcast lifecycle
            val pendingResult = goAsync()

            receiverScope.launch {
                try {
                    for (geofence in triggeringGeofences) {
                        val requestId = geofence.requestId
                        val stopId = requestId.removePrefix(GeofenceManager.GEOFENCE_ID_PREFIX)

                        // Query the target stop from local Room DB
                        val stop = repository.getStopById(stopId)
                        val stopName = stop?.name ?: "Destination Transit Stop"

                        // Trigger high-priority notification
                        notificationManager.showStopApproachingNotification(
                            stopId = stopId,
                            stopName = stopName,
                            distanceMeters = 500
                        )
                    }
                } catch (e: Exception) {
                    android.util.Log.e("GeofenceReceiver", "Failed processing geofence trigger", e)
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}
