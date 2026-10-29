package com.urbantransit.offline.domain.fare

import com.urbantransit.offline.data.local.ChennaiTransitSeed
import com.urbantransit.offline.data.local.entity.TransitRoute
import com.urbantransit.offline.data.local.entity.TransitStop
import com.urbantransit.offline.data.model.TransitMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FareCalculatorTest {

    private lateinit var fareCalculator: FareCalculator

    @Before
    fun setUp() {
        fareCalculator = FareCalculator()
    }

    @Test
    fun calculateFare_cmrlMetroShortDistance_computesCorrectSlabAndDiscount() {
        val blueRoute = TransitRoute(
            id = "cmrl_blue",
            shortName = "Blue Line",
            longName = "Wimco Nagar ↔ Airport",
            mode = TransitMode.SUBWAY,
            colorHex = "#2563EB",
            textColorHex = "#FFFFFF",
            agencyName = "CMRL"
        )

        // Central to LIC: ~2 km
        val central = TransitStop("cmrl_central", "Central Metro", 13.0827, 80.2755, "zone_central", true)
        val lic = TransitStop("cmrl_lic", "LIC Metro", 13.0645, 80.2678, "zone_central", true)

        val fare = fareCalculator.calculateFare(blueRoute, central, lic)

        // Verify total and 20% discount
        assertTrue(fare.distanceKm > 0)
        assertTrue(fare.discountedQrFareCents < fare.totalCents)
        assertEquals("₹20", fare.formattedTotal)
        assertEquals("₹16", fare.formattedDiscounted)
        assertFalse(fare.isWomenFreeEligible)
    }

    @Test
    fun calculateFare_cmrlMetroPhase2YellowLine_computesPoonamalleeToVadapalani() {
        val yellowRoute = TransitRoute(
            id = "cmrl_yellow",
            shortName = "Yellow Line",
            longName = "Poonamallee Bypass ↔ Vadapalani",
            mode = TransitMode.SUBWAY,
            colorHex = "#EAB308",
            textColorHex = "#000000",
            agencyName = "CMRL Phase 2"
        )

        val poonamallee = TransitStop("cmrl_y_poonamallee_bypass", "Poonamallee Bypass", 13.0485, 80.0920, "zone_west", true)
        val vadapalani = TransitStop("cmrl_vadapalani", "Vadapalani Metro", 13.0512, 80.2115, "zone_west", true)

        val fare = fareCalculator.calculateFare(yellowRoute, poonamallee, vadapalani)

        // Distance is ~13-15 km -> ₹40 slab, with 20% off -> ₹32
        assertTrue(fare.distanceKm in 11.0..16.0)
        assertEquals("₹40", fare.formattedTotal)
        assertEquals("₹32", fare.formattedDiscounted)
    }

    @Test
    fun calculateFare_mtcOrdinaryBus_womenFreeEligible() {
        val ordinary29C = TransitRoute(
            id = "mtc_29c",
            shortName = "29C Ordinary",
            longName = "Perambur ↔ Besant Nagar (Vetri Payanam)",
            mode = TransitMode.BUS,
            colorHex = "#EC4899",
            textColorHex = "#FFFFFF",
            agencyName = "MTC Chennai"
        )

        val perambur = TransitStop("mtc_perambur", "Perambur", 13.1090, 80.2395, "zone_north", true)
        val besantNagar = TransitStop("mtc_besant_nagar", "Besant Nagar", 12.9998, 80.2685, "zone_south", true)

        val fare = fareCalculator.calculateFare(ordinary29C, perambur, besantNagar)

        assertTrue(fare.isWomenFreeEligible)
    }

    @Test
    fun calculateFare_mtcAcVolvoBus_notFreeForWomen() {
        val bus570 = TransitRoute(
            id = "mtc_570",
            shortName = "570 AC",
            longName = "CMBT ↔ Kelambakkam",
            mode = TransitMode.BUS,
            colorHex = "#0284C7",
            textColorHex = "#FFFFFF",
            agencyName = "MTC Chennai"
        )

        val cmbt = TransitStop("mtc_cmbt", "CMBT", 13.0694, 80.1948, "zone_west", true)
        val kelambakkam = TransitStop("mtc_kelambakkam", "Kelambakkam", 12.7844, 80.2206, "zone_omr", true)

        val fare = fareCalculator.calculateFare(bus570, cmbt, kelambakkam)

        assertFalse(fare.isWomenFreeEligible)
        assertTrue(fare.totalCents > 0)
    }

    @Test
    fun verifySeedDataIntegrity_routesAndStopsConsistent() {
        assertTrue(ChennaiTransitSeed.routes.isNotEmpty())
        assertTrue(ChennaiTransitSeed.stops.isNotEmpty())
        assertTrue(ChennaiTransitSeed.schedules.isNotEmpty())

        // Check Phase 2 Yellow Line existence
        val yellowLine = ChennaiTransitSeed.routes.find { it.id == "cmrl_yellow" }
        assertTrue(yellowLine != null)
        assertEquals(TransitMode.SUBWAY, yellowLine?.mode)

        // Check Phase 2 11 operational stops
        val yellowStops = ChennaiTransitSeed.schedules.filter { it.routeId == "cmrl_yellow" }
        assertEquals(11, yellowStops.size)
    }
}
