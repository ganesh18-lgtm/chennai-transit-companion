package com.urbantransit.offline.location

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.urbantransit.offline.data.local.entity.TransitStop
import com.urbantransit.offline.data.model.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class GeofenceManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val geofencingClient: GeofencingClient
) {

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        PendingIntent.getBroadcast(context, GEOFENCE_PENDING_INTENT_REQUEST_CODE, intent, flags)
    }

    /**
     * Checks whether the app has the necessary location permissions.
     */
    fun hasForegroundLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Checks whether the app has background location access (Android 10+ / API 29+).
     */
    fun hasBackgroundLocationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // Background location is implicitly granted with FINE_LOCATION on Android 9 and lower
            hasForegroundLocationPermission()
        }
    }

    /**
     * Checks whether the app has notification posting permission (Android 13+ / API 33+).
     */
    fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    /**
     * Returns the array of required location permissions for the host activity to request.
     * Note: Android 11+ requires requesting foreground permissions first before background.
     */
    fun getRequiredForegroundPermissions(): Array<String> {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        return permissions.toTypedArray()
    }

    fun getBackgroundLocationPermission(): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        } else {
            null
        }
    }

    /**
     * Registers a 500-meter circular geofence around the specified [TransitStop].
     */
    suspend fun addStopGeofence(
        stop: TransitStop,
        radiusMeters: Float = DEFAULT_GEOFENCE_RADIUS_METERS
    ): Resource<Unit> {
        if (!hasForegroundLocationPermission()) {
            return Resource.Error("Foreground location permission (ACCESS_FINE_LOCATION) is not granted.")
        }

        if (!hasBackgroundLocationPermission()) {
            return Resource.Error("Background location permission (ACCESS_BACKGROUND_LOCATION) is required for proximity notifications.")
        }

        val geofenceRequestId = "$GEOFENCE_ID_PREFIX${stop.id}"

        val geofence = Geofence.Builder()
            .setRequestId(geofenceRequestId)
            .setCircularRegion(stop.latitude, stop.longitude, radiusMeters)
            .setExpirationDuration(TimeUnit.HOURS.toMillis(GEOFENCE_EXPIRATION_HOURS))
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_DWELL)
            .setLoiteringDelay(GEOFENCE_LOITERING_DELAY_MS)
            .build()

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        return suspendCancellableCoroutine { continuation ->
            try {
                geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent)
                    .addOnSuccessListener {
                        if (continuation.isActive) {
                            continuation.resume(Resource.Success(Unit))
                        }
                    }
                    .addOnFailureListener { exception ->
                        if (continuation.isActive) {
                            continuation.resume(
                                Resource.Error(
                                    message = "Failed to register geofence for stop ${stop.name}: ${exception.localizedMessage}",
                                    cause = exception
                                )
                            )
                        }
                    }
            } catch (securityEx: SecurityException) {
                if (continuation.isActive) {
                    continuation.resume(
                        Resource.Error(
                            message = "SecurityException adding geofence: ${securityEx.localizedMessage}",
                            cause = securityEx
                        )
                    )
                }
            }
        }
    }

    /**
     * Removes the active geofence for a specific transit stop.
     */
    suspend fun removeStopGeofence(stopId: String): Resource<Unit> {
        val geofenceRequestId = "$GEOFENCE_ID_PREFIX$stopId"
        return suspendCancellableCoroutine { continuation ->
            geofencingClient.removeGeofences(listOf(geofenceRequestId))
                .addOnSuccessListener {
                    if (continuation.isActive) {
                        continuation.resume(Resource.Success(Unit))
                    }
                }
                .addOnFailureListener { exception ->
                    if (continuation.isActive) {
                        continuation.resume(
                            Resource.Error(
                                message = "Failed to remove geofence for stop $stopId: ${exception.localizedMessage}",
                                cause = exception
                            )
                        )
                    }
                }
        }
    }

    /**
     * Clears all registered geofences.
     */
    suspend fun removeAllGeofences(): Resource<Unit> {
        return suspendCancellableCoroutine { continuation ->
            geofencingClient.removeGeofences(geofencePendingIntent)
                .addOnSuccessListener {
                    if (continuation.isActive) {
                        continuation.resume(Resource.Success(Unit))
                    }
                }
                .addOnFailureListener { exception ->
                    if (continuation.isActive) {
                        continuation.resume(
                            Resource.Error(
                                message = "Failed to remove all geofences: ${exception.localizedMessage}",
                                cause = exception
                            )
                        )
                    }
                }
        }
    }

    companion object {
        const val GEOFENCE_ID_PREFIX = "transit_stop_geofence_"
        const val DEFAULT_GEOFENCE_RADIUS_METERS = 500f
        const val GEOFENCE_LOITERING_DELAY_MS = 10_000 // 10 seconds
        const val GEOFENCE_EXPIRATION_HOURS = 24L
        private const val GEOFENCE_PENDING_INTENT_REQUEST_CODE = 4001
    }
}
