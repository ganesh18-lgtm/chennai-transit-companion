package com.urbantransit.offline.domain.fare

import com.urbantransit.offline.data.local.entity.FareRule
import com.urbantransit.offline.data.local.entity.TransitRoute
import com.urbantransit.offline.data.local.entity.TransitStop
import com.urbantransit.offline.data.model.TransitMode
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class CalculatedFare(
    val totalCents: Long, // in Paisa (₹40 = 4000)
    val baseFareCents: Long,
    val distanceFareCents: Long,
    val peakSurchargeCents: Long,
    val discountedQrFareCents: Long, // CMRL 20% discount on Smartcard / QR
    val distanceKm: Double,
    val isPeakHour: Boolean,
    val isWomenFreeEligible: Boolean, // MTC Ordinary bus zero fare policy in Tamil Nadu
    val transitType: String,
    val appliedRuleDescription: String,
    val formattedTotal: String,
    val formattedDiscounted: String,
    val formattedDistance: String
)

@Singleton
class FareCalculator @Inject constructor() {

    /**
     * Calculates authentic Chennai transit fares based on official CMRL Metro and MTC Bus tariffs.
     */
    fun calculateFare(
        route: TransitRoute,
        originStop: TransitStop,
        destinationStop: TransitStop,
        @Suppress("UNUSED_PARAMETER") rules: List<FareRule> = emptyList(),
        calendar: Calendar = Calendar.getInstance()
    ): CalculatedFare {
        val distanceKm = calculateHaversineDistanceKm(
            originStop.latitude,
            originStop.longitude,
            destinationStop.latitude,
            destinationStop.longitude
        )

        val isPeak = isPeakHour(calendar)

        val (rawFareRupees, transitLabel, ruleDescription, isWomenFree) = when (route.mode) {
            TransitMode.SUBWAY -> calculateCmrlMetroFare(distanceKm)
            TransitMode.BUS -> calculateMtcBusFare(route, distanceKm)
            else -> calculateCmrlMetroFare(distanceKm)
        }

        val totalPaisa = rawFareRupees * 100L
        val discountedQrPaisa = if (route.mode == TransitMode.SUBWAY) {
            // CMRL offers 20% flat discount on Smartcards & National Common Mobility Cards (NCMC)
            (totalPaisa * 0.80).toLong()
        } else {
            totalPaisa
        }

        return CalculatedFare(
            totalCents = totalPaisa,
            baseFareCents = totalPaisa,
            distanceFareCents = 0L,
            peakSurchargeCents = 0L,
            discountedQrFareCents = discountedQrPaisa,
            distanceKm = distanceKm,
            isPeakHour = isPeak,
            isWomenFreeEligible = isWomenFree,
            transitType = transitLabel,
            appliedRuleDescription = ruleDescription,
            formattedTotal = "₹$rawFareRupees",
            formattedDiscounted = "₹${discountedQrPaisa / 100}",
            formattedDistance = String.format(Locale.US, "%.1f km", distanceKm)
        )
    }

    /**
     * Official Chennai Metro Rail Limited (CMRL) Distance Slabs:
     * 0 - 2 km   -> ₹10
     * 2 - 5 km   -> ₹20
     * 5 - 12 km  -> ₹30
     * 12 - 21 km -> ₹40
     * > 21 km    -> ₹50 (Max ₹60 without card)
     */
    private fun calculateCmrlMetroFare(distanceKm: Double): Quadruple<Long, String, String, Boolean> {
        val fare = when {
            distanceKm <= 2.0 -> 10L
            distanceKm <= 5.0 -> 20L
            distanceKm <= 12.0 -> 30L
            distanceKm <= 21.0 -> 40L
            else -> 50L
        }
        return Quadruple(
            fare,
            "Chennai Metro Rail (CMRL)",
            "CMRL Official Distance Slab (20% off with Metro Smart Card / NCMC)",
            false
        )
    }

    /**
     * Metropolitan Transport Corporation (MTC Chennai) Bus Fares:
     * - Ordinary (29C, white/pink board): Base ₹5, max ₹18. Women travel FREE under Vetri Payanam Thittam.
     * - Express (102, 54, blue/red board): Base ₹7, ~₹1.3/km, max ₹35. Women travel FREE under expanded Vetri Payanam.
     * - Deluxe (21G, 11G, semi-low floor): Base ₹11, ~₹1.8/km, max ₹45. Women travel FREE under expanded Vetri Payanam.
     * - AC Electric / Volvo (570 AC): Base ₹25, ~₹3.5/km, max ₹85. Standard fare for all.
     */
    private fun calculateMtcBusFare(route: TransitRoute, distanceKm: Double): Quadruple<Long, String, String, Boolean> {
        return when {
            route.shortName.contains("Ordinary", ignoreCase = true) || route.shortName.contains("29C") -> {
                val fare = (5L + (distanceKm * 0.8).toLong()).coerceIn(5L, 18L)
                Quadruple(
                    fare,
                    "MTC Ordinary (Vetri Payanam Thittam)",
                    "Free for Women (Vetri Payanam Scheme) • ₹$fare for General Commuters",
                    true
                )
            }
            route.shortName.contains("Express", ignoreCase = true) || route.shortName.contains("102") || route.shortName.contains("54") -> {
                val fare = (7L + (distanceKm * 1.3).toLong()).coerceIn(7L, 35L)
                Quadruple(
                    fare,
                    "MTC Express (Vetri Payanam Scheme)",
                    "Free for Women (Vetri Payanam Scheme) • ₹$fare for General Commuters",
                    true
                )
            }
            route.shortName.contains("Deluxe", ignoreCase = true) || route.shortName.contains("21G") || route.shortName.contains("11G") -> {
                val fare = (11L + (distanceKm * 1.8).toLong()).coerceIn(11L, 45L)
                Quadruple(
                    fare,
                    "MTC Deluxe (Vetri Payanam Scheme)",
                    "Free for Women (Vetri Payanam Scheme) • ₹$fare for General Commuters",
                    true
                )
            }
            route.shortName.contains("AC", ignoreCase = true) || route.shortName.contains("570") -> {
                val fare = (25L + (distanceKm * 3.5).toLong()).coerceIn(25L, 85L)
                Quadruple(
                    fare,
                    "MTC AC Low-Floor Volvo / EV",
                    "Climate controlled express bus along OMR IT Corridor • ₹$fare",
                    false
                )
            }
            else -> {
                val fare = (10L + (distanceKm * 1.5).toLong()).coerceIn(10L, 40L)
                Quadruple(fare, "MTC Bus Service", "Standard City Route", false)
            }
        }
    }

    fun calculateHaversineDistanceKm(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val earthRadiusKm = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val rLat1 = Math.toRadians(lat1)
        val rLat2 = Math.toRadians(lat2)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(rLat1) * cos(rLat2) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadiusKm * c
    }

    fun isPeakHour(calendar: Calendar): Boolean {
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        if (dayOfWeek == Calendar.SUNDAY) return false

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val timeMinutes = hour * 60 + minute

        val morningPeak = (8 * 60 + 30)..(11 * 60)
        val eveningPeak = (17 * 60)..(20 * 60 + 30)

        return (timeMinutes in morningPeak) || (timeMinutes in eveningPeak)
    }

    private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
}
