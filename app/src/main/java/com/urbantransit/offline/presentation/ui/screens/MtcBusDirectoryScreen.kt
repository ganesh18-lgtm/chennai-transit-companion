package com.urbantransit.offline.presentation.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urbantransit.offline.presentation.ui.components.MtcBusAnimation
import com.urbantransit.offline.presentation.ui.components.MtcElectricBusAnimation
import com.urbantransit.offline.presentation.ui.components.TransitSticker
import com.urbantransit.offline.presentation.ui.theme.ChennaiColors

/**
 * Data Model for Chennai MTC Bus Routes.
 */
data class MtcBusRouteItem(
    val routeNumber: String,
    val origin: String,
    val destination: String,
    val serviceType: String,
    val isVetriPayanam: Boolean,
    val fareRange: String,
    val frequency: String,
    val operatingHours: String,
    val totalDistanceKm: Double,
    val keyStops: List<String>,
    val badgeColor: Color = ChennaiColors.MtcRed
)

/**
 * Data Model for Tamil Nadu Government Transit Welfare Schemes.
 */
data class MtcWelfareSchemeItem(
    val id: String,
    val title: String,
    val titleTamil: String,
    val emoji: String,
    val eligibility: String,
    val coverage: String,
    val fareDetails: String,
    val howToAvail: String,
    val accentColor: Color
)

/**
 * Comprehensive Directory of 25+ Chennai Metropolitan Transport Corporation (MTC) Bus Routes.
 */
val CHENNAI_MTC_BUS_ROUTES = listOf(
    MtcBusRouteItem(
        routeNumber = "21G",
        origin = "Broadway Bus Terminus",
        destination = "Tambaram West Bus Stand",
        serviceType = "DELUXE / EXPRESS",
        isVetriPayanam = true,
        fareRange = "FREE for Women (Ordinary) • ₹7 - ₹24",
        frequency = "Every 5-8 mins",
        operatingHours = "04:30 AM - 11:30 PM",
        totalDistanceKm = 31.4,
        keyStops = listOf(
            "Broadway", "Central Park", "Anna Square", "Marina Beach", "Santhome",
            "Mylapore Luz Corner", "Adyar Signal", "Saidapet", "Guindy Estate",
            "Meenambakkam", "Pallavaram", "Chromepet", "Tambaram Sanatorium", "Tambaram West"
        ),
        badgeColor = ChennaiColors.MtcRed
    ),
    MtcBusRouteItem(
        routeNumber = "570",
        origin = "CMBT Koyambedu",
        destination = "Kelambakkam Bus Stand",
        serviceType = "AC LOW-FLOOR VOLVO / DELUXE",
        isVetriPayanam = false,
        fareRange = "₹25 - ₹75 (AC) • ₹12 - ₹32 (Deluxe)",
        frequency = "Every 8-12 mins",
        operatingHours = "05:00 AM - 11:00 PM",
        totalDistanceKm = 39.8,
        keyStops = listOf(
            "CMBT Koyambedu", "Vadapalani", "Ashok Pillar", "Ekkattuthangal", "Guindy",
            "Velachery Bypass", "SRP Tools", "Perungudi", "Thoraipakkam", "Karapakkam",
            "Sholinganallur Junction", "Navalur Toll", "Siruseri SIPCOT IT Park", "Kelambakkam"
        ),
        badgeColor = Color(0xFF0284C7)
    ),
    MtcBusRouteItem(
        routeNumber = "102",
        origin = "Broadway Bus Terminus",
        destination = "Kelambakkam Bus Stand",
        serviceType = "EXPRESS / DELUXE",
        isVetriPayanam = true,
        fareRange = "FREE for Women (Ordinary) • ₹10 - ₹28",
        frequency = "Every 10 mins",
        operatingHours = "05:15 AM - 10:45 PM",
        totalDistanceKm = 36.5,
        keyStops = listOf(
            "Broadway", "Marina Beach", "Santhome", "Foreshore Estate", "Adyar Signal",
            "Tidel Park", "SRP Tools", "Perungudi", "Thoraipakkam", "Sholinganallur",
            "Navalur", "Siruseri IT Park", "Kelambakkam"
        ),
        badgeColor = Color(0xFFEA580C)
    ),
    MtcBusRouteItem(
        routeNumber = "29C",
        origin = "Perambur Bus Stand",
        destination = "Besant Nagar Beach Terminus",
        serviceType = "ORDINARY (WHITE BOARD)",
        isVetriPayanam = true,
        fareRange = "100% FREE for Women • ₹7 - ₹16",
        frequency = "Every 6-10 mins",
        operatingHours = "05:00 AM - 10:30 PM",
        totalDistanceKm = 19.2,
        keyStops = listOf(
            "Perambur", "Otteri Jamalia", "Doveton Vepery", "Chetpet Signal",
            "Sterling Road", "Gemini / Anna Flyover", "Stella Maris", "Alwarpet Luz",
            "Mandaveli Depot", "Adyar Gate", "Adyar Signal", "Besant Nagar Beach"
        ),
        badgeColor = ChennaiColors.MtcPinkWomen
    ),
    MtcBusRouteItem(
        routeNumber = "11G",
        origin = "Broadway Bus Terminus",
        destination = "K.K. Nagar Bus Terminus",
        serviceType = "ORDINARY & DELUXE",
        isVetriPayanam = true,
        fareRange = "FREE for Women (Ordinary) • ₹8 - ₹18",
        frequency = "Every 8 mins",
        operatingHours = "05:00 AM - 11:00 PM",
        totalDistanceKm = 15.6,
        keyStops = listOf(
            "Broadway", "Central Station", "LIC Metro", "Thousand Lights",
            "DMS", "Gemini", "Panagal Park T. Nagar", "T. Nagar Depot",
            "Ashok Pillar", "K.K. Nagar Terminus"
        ),
        badgeColor = Color(0xFF8B5CF6)
    ),
    MtcBusRouteItem(
        routeNumber = "54",
        origin = "Broadway Bus Terminus",
        destination = "Poonamallee Bus Terminus",
        serviceType = "DELUXE & EXPRESS",
        isVetriPayanam = true,
        fareRange = "FREE for Women (Ordinary) • ₹9 - ₹25",
        frequency = "Every 6 mins",
        operatingHours = "04:45 AM - 11:15 PM",
        totalDistanceKm = 24.8,
        keyStops = listOf(
            "Broadway", "Central Station", "LIC Anna Salai", "DMS", "Saidapet",
            "Guindy Kathipara", "Butt Road", "Porur Roundtana", "Sri Ramachandra Hospital",
            "Iyyappanthangal Depot", "Kattupakkam", "Kumananchavadi", "Poonamallee"
        ),
        badgeColor = Color(0xFF059669)
    ),
    MtcBusRouteItem(
        routeNumber = "27D",
        origin = "Villivakkam Bus Stand",
        destination = "Foreshore Estate Bus Stand",
        serviceType = "ORDINARY (WHITE BOARD)",
        isVetriPayanam = true,
        fareRange = "100% FREE for Women • ₹7 - ₹17",
        frequency = "Every 8-12 mins",
        operatingHours = "05:15 AM - 10:15 PM",
        totalDistanceKm = 18.0,
        keyStops = listOf(
            "Villivakkam", "Nathamuni", "Anna Nagar East", "Kilpauk Medical College",
            "Chetpet Signal", "Sterling Road", "Gemini Flyover", "Alwarpet",
            "Mylapore", "Santhome", "Foreshore Estate"
        ),
        badgeColor = ChennaiColors.MtcPinkWomen
    ),
    MtcBusRouteItem(
        routeNumber = "D70",
        origin = "Ambattur Industrial Estate",
        destination = "Velachery Bus Stand",
        serviceType = "DELUXE & EXPRESS",
        isVetriPayanam = false,
        fareRange = "₹10 - ₹26",
        frequency = "Every 7 mins",
        operatingHours = "05:00 AM - 11:00 PM",
        totalDistanceKm = 27.2,
        keyStops = listOf(
            "Ambattur Estate", "Mogappair Golden Flats", "Thirumangalam Metro",
            "Koyambedu CMBT", "Vadapalani", "Ashok Pillar", "Ekkattuthangal",
            "Guindy Race Course", "Velachery Railway Station", "Velachery Bus Stand"
        ),
        badgeColor = Color(0xFFD97706)
    ),
    MtcBusRouteItem(
        routeNumber = "70V",
        origin = "CMBT Koyambedu",
        destination = "Vandalur Zoo Terminus",
        serviceType = "DELUXE",
        isVetriPayanam = false,
        fareRange = "₹11 - ₹28",
        frequency = "Every 10-15 mins",
        operatingHours = "05:30 AM - 10:00 PM",
        totalDistanceKm = 33.5,
        keyStops = listOf(
            "Koyambedu CMBT", "Vadapalani", "Ashok Pillar", "Guindy", "Airport",
            "Pallavaram", "Chromepet", "Tambaram West", "Perungalathur", "Vandalur Zoo"
        ),
        badgeColor = ChennaiColors.MtcRed
    ),
    MtcBusRouteItem(
        routeNumber = "47",
        origin = "Villivakkam Bus Stand",
        destination = "Adyar Bus Depot",
        serviceType = "ORDINARY & EXPRESS",
        isVetriPayanam = true,
        fareRange = "FREE for Women (Ordinary) • ₹8 - ₹19",
        frequency = "Every 10 mins",
        operatingHours = "05:30 AM - 10:30 PM",
        totalDistanceKm = 20.5,
        keyStops = listOf(
            "Villivakkam", "ICF", "Kilpauk", "Chetpet", "Loyola Nungambakkam",
            "Panagal Park", "T. Nagar Depot", "Nandanam", "Saidapet", "Little Mount", "Adyar Signal"
        ),
        badgeColor = Color(0xFF6366F1)
    ),
    MtcBusRouteItem(
        routeNumber = "51",
        origin = "Broadway / High Court",
        destination = "Tambaram East Bus Stand",
        serviceType = "DELUXE & EXPRESS",
        isVetriPayanam = false,
        fareRange = "₹12 - ₹27",
        frequency = "Every 12 mins",
        operatingHours = "05:00 AM - 10:30 PM",
        totalDistanceKm = 32.0,
        keyStops = listOf(
            "Broadway", "Triplicane", "Mylapore", "Mandaveli", "Adyar",
            "Velachery Checkpost", "Medavakkam Koot Road", "Camp Road", "Tambaram East"
        ),
        badgeColor = Color(0xFF0284C7)
    ),
    MtcBusRouteItem(
        routeNumber = "1B",
        origin = "Thiruvottiyur Bus Stand",
        destination = "Tambaram West Bus Stand",
        serviceType = "EXPRESS",
        isVetriPayanam = false,
        fareRange = "₹13 - ₹30",
        frequency = "Every 15 mins",
        operatingHours = "05:00 AM - 10:00 PM",
        totalDistanceKm = 38.0,
        keyStops = listOf(
            "Thiruvottiyur", "Tollgate", "Washermanpet", "Broadway", "Central Station",
            "DMS", "Saidapet", "Guindy", "Airport", "Chromepet", "Tambaram West"
        ),
        badgeColor = Color(0xFFEA580C)
    ),
    MtcBusRouteItem(
        routeNumber = "19B",
        origin = "Saidapet Court Bus Stand",
        destination = "Kelambakkam Bus Stand",
        serviceType = "ORDINARY (WHITE BOARD)",
        isVetriPayanam = true,
        fareRange = "100% FREE for Women • ₹8 - ₹20",
        frequency = "Every 10 mins",
        operatingHours = "05:30 AM - 10:30 PM",
        totalDistanceKm = 30.5,
        keyStops = listOf(
            "Saidapet", "Guindy", "Velachery", "SRP Tools", "Perungudi",
            "Thoraipakkam", "Sholinganallur", "Navalur", "Siruseri", "Kelambakkam"
        ),
        badgeColor = ChennaiColors.MtcPinkWomen
    ),
    MtcBusRouteItem(
        routeNumber = "23C",
        origin = "Besant Nagar Beach Terminus",
        destination = "Ayanavaram Bus Depot",
        serviceType = "ORDINARY (WHITE BOARD)",
        isVetriPayanam = true,
        fareRange = "100% FREE for Women • ₹7 - ₹16",
        frequency = "Every 10 mins",
        operatingHours = "05:30 AM - 10:15 PM",
        totalDistanceKm = 17.5,
        keyStops = listOf(
            "Besant Nagar", "Adyar", "Mandaveli", "Royapettah", "LIC",
            "Chetpet", "Kilpauk", "Otteri", "Ayanavaram"
        ),
        badgeColor = ChennaiColors.MtcPinkWomen
    ),
    MtcBusRouteItem(
        routeNumber = "25G",
        origin = "Anna Square / Marina Beach",
        destination = "Poonamallee Bus Terminus",
        serviceType = "ORDINARY (WHITE BOARD)",
        isVetriPayanam = true,
        fareRange = "100% FREE for Women • ₹8 - ₹22",
        frequency = "Every 8-12 mins",
        operatingHours = "05:00 AM - 10:30 PM",
        totalDistanceKm = 23.5,
        keyStops = listOf(
            "Anna Square", "Marina Beach", "Triplicane", "DMS", "T. Nagar Panagal Park",
            "Vadapalani", "Porur", "Iyyappanthangal", "Kumananchavadi", "Poonamallee"
        ),
        badgeColor = ChennaiColors.MtcPinkWomen
    ),
    MtcBusRouteItem(
        routeNumber = "88K",
        origin = "Broadway Bus Terminus",
        destination = "Kundrathur Murugan Temple",
        serviceType = "DELUXE & EXPRESS",
        isVetriPayanam = false,
        fareRange = "₹10 - ₹24",
        frequency = "Every 12-15 mins",
        operatingHours = "05:30 AM - 10:00 PM",
        totalDistanceKm = 28.5,
        keyStops = listOf(
            "Broadway", "Central", "LIC", "Guindy", "Kathipara",
            "Porur Roundtana", "Kovur", "Kundrathur Murugan Temple"
        ),
        badgeColor = ChennaiColors.MtcRed
    ),
    MtcBusRouteItem(
        routeNumber = "49",
        origin = "T. Nagar Bus Terminus",
        destination = "Kovalam ECR Beach",
        serviceType = "DELUXE & EXPRESS",
        isVetriPayanam = false,
        fareRange = "₹10 - ₹25",
        frequency = "Every 15 mins",
        operatingHours = "05:45 AM - 10:00 PM",
        totalDistanceKm = 31.0,
        keyStops = listOf(
            "T. Nagar", "Saidapet", "Guindy", "Adyar", "Thiruvanmiyur",
            "Kottivakkam", "Neelankarai", "Injambakkam", "Akkarai", "Kovalam"
        ),
        badgeColor = Color(0xFF059669)
    ),
    MtcBusRouteItem(
        routeNumber = "588",
        origin = "Thiruvanmiyur Bus Stand",
        destination = "Mamallapuram (Mahabalipuram)",
        serviceType = "ECR SCENIC COASTAL EXPRESS",
        isVetriPayanam = false,
        fareRange = "₹20 - ₹50",
        frequency = "Every 20 mins",
        operatingHours = "06:00 AM - 09:30 PM",
        totalDistanceKm = 44.0,
        keyStops = listOf(
            "Thiruvanmiyur", "Kottivakkam", "Injambakkam", "Akkarai",
            "Muttukadu Boat House", "Kovalam", "Crocodile Bank", "Mamallapuram"
        ),
        badgeColor = Color(0xFF0284C7)
    ),
    MtcBusRouteItem(
        routeNumber = "159",
        origin = "Thiruvottiyur Bus Stand",
        destination = "CMBT Koyambedu",
        serviceType = "EXPRESS",
        isVetriPayanam = false,
        fareRange = "₹10 - ₹22",
        frequency = "Every 12 mins",
        operatingHours = "05:15 AM - 10:15 PM",
        totalDistanceKm = 22.0,
        keyStops = listOf(
            "Thiruvottiyur", "Tollgate", "Vyasarpadi", "Perambur",
            "ICF", "Anna Nagar West", "Thirumangalam", "CMBT Koyambedu"
        ),
        badgeColor = Color(0xFFEA580C)
    ),
    MtcBusRouteItem(
        routeNumber = "120",
        origin = "Broadway Bus Terminus",
        destination = "Avadi Bus Depot",
        serviceType = "DELUXE & EXPRESS",
        isVetriPayanam = false,
        fareRange = "₹10 - ₹25",
        frequency = "Every 10 mins",
        operatingHours = "05:00 AM - 11:00 PM",
        totalDistanceKm = 25.0,
        keyStops = listOf(
            "Broadway", "Central", "Vepery", "Kellys", "Kilpauk",
            "Villivakkam", "Padi", "Ambattur OT", "Avadi"
        ),
        badgeColor = Color(0xFF8B5CF6)
    ),
    MtcBusRouteItem(
        routeNumber = "M70",
        origin = "Thiruvanmiyur Bus Stand",
        destination = "CMBT Koyambedu",
        serviceType = "DELUXE & EXPRESS",
        isVetriPayanam = false,
        fareRange = "₹9 - ₹22",
        frequency = "Every 8 mins",
        operatingHours = "05:00 AM - 11:00 PM",
        totalDistanceKm = 21.0,
        keyStops = listOf(
            "Thiruvanmiyur", "SRP Tools", "Taramani", "Velachery",
            "Guindy", "Ashok Pillar", "Vadapalani", "CMBT Koyambedu"
        ),
        badgeColor = Color(0xFFD97706)
    ),
    MtcBusRouteItem(
        routeNumber = "52",
        origin = "Broadway Bus Terminus",
        destination = "Hasthinapuram Bus Stand",
        serviceType = "ORDINARY (WHITE BOARD)",
        isVetriPayanam = true,
        fareRange = "100% FREE for Women • ₹7 - ₹18",
        frequency = "Every 12 mins",
        operatingHours = "05:30 AM - 10:15 PM",
        totalDistanceKm = 26.5,
        keyStops = listOf(
            "Broadway", "Central", "DMS", "Saidapet", "Guindy",
            "Airport", "Chromepet", "Hasthinapuram"
        ),
        badgeColor = ChennaiColors.MtcPinkWomen
    ),
    MtcBusRouteItem(
        routeNumber = "6D",
        origin = "Tollgate Bus Stand",
        destination = "Thiruvanmiyur Bus Stand",
        serviceType = "ORDINARY (WHITE BOARD)",
        isVetriPayanam = true,
        fareRange = "100% FREE for Women • ₹7 - ₹17",
        frequency = "Every 10 mins",
        operatingHours = "05:00 AM - 10:30 PM",
        totalDistanceKm = 21.5,
        keyStops = listOf(
            "Tollgate", "Beach Road", "Broadway", "Marina Beach",
            "Santhome", "Adyar Signal", "Thiruvanmiyur"
        ),
        badgeColor = ChennaiColors.MtcPinkWomen
    ),
    MtcBusRouteItem(
        routeNumber = "72",
        origin = "High Court / Broadway",
        destination = "Pattabiram Bus Terminus",
        serviceType = "EXPRESS",
        isVetriPayanam = false,
        fareRange = "₹11 - ₹26",
        frequency = "Every 15 mins",
        operatingHours = "05:30 AM - 10:00 PM",
        totalDistanceKm = 29.0,
        keyStops = listOf(
            "High Court", "Central", "Kilpauk", "Anna Nagar West",
            "Ambattur OT", "Thirumullaivoyal", "Avadi", "Pattabiram"
        ),
        badgeColor = Color(0xFF6366F1)
    ),
    MtcBusRouteItem(
        routeNumber = "221H",
        origin = "Central Railway Station",
        destination = "Thiruporur Murugan Temple",
        serviceType = "DELUXE",
        isVetriPayanam = false,
        fareRange = "₹15 - ₹35",
        frequency = "Every 15-20 mins",
        operatingHours = "05:30 AM - 09:45 PM",
        totalDistanceKm = 48.0,
        keyStops = listOf(
            "Central", "Marina Beach", "Adyar", "SRP Tools", "Sholinganallur",
            "Navalur", "Siruseri IT Park", "Kelambakkam", "Thiruporur"
        ),
        badgeColor = Color(0xFF0284C7)
    ),
    MtcBusRouteItem(
        routeNumber = "570 EV",
        origin = "CMBT Koyambedu Terminus",
        destination = "Siruseri SIPCOT / Kelambakkam",
        serviceType = "100% ELECTRIC LOW-FLOOR SWITCH EV",
        isVetriPayanam = true,
        fareRange = "100% FREE for Women (Ordinary EV) • ₹12 - ₹35",
        frequency = "Every 8 mins",
        operatingHours = "05:00 AM - 11:30 PM",
        totalDistanceKm = 39.5,
        keyStops = listOf(
            "CMBT Koyambedu", "Vadapalani", "Ashok Pillar", "Guindy Kathipara",
            "Velachery", "SRP Tools", "Perungudi", "Thoraipakkam", "Sholinganallur",
            "Navalur", "Siruseri IT Park", "Kelambakkam"
        ),
        badgeColor = ChennaiColors.ElectricGreen
    ),
    MtcBusRouteItem(
        routeNumber = "102 EV",
        origin = "Broadway Bus Terminus",
        destination = "Kelambakkam Bus Terminus",
        serviceType = "100% ELECTRIC ZERO-EMISSION LOW-FLOOR",
        isVetriPayanam = true,
        fareRange = "100% FREE for Women • ₹10 - ₹30",
        frequency = "Every 10 mins",
        operatingHours = "05:15 AM - 11:00 PM",
        totalDistanceKm = 36.8,
        keyStops = listOf(
            "Broadway", "Marina Beach", "Santhome", "Adyar Signal", "Tidel Park",
            "Perungudi", "Thoraipakkam", "Sholinganallur", "Siruseri SIPCOT", "Kelambakkam"
        ),
        badgeColor = ChennaiColors.ElectricGreen
    )
)

/**
 * Official Tamil Nadu Government Welfare Schemes and Concessions for Chennai Commuters.
 */
val CHENNAI_TRANSIT_SCHEMES = listOf(
    MtcWelfareSchemeItem(
        id = "vetri_payanam",
        title = "Vidiyal & Vettri Payanam Thittam (Magalir Payanam)",
        titleTamil = "விடியல் மற்றும் வெற்றி பயணம் திட்டம் (மகளிர் இலவச பயணம்)",
        emoji = "🌸",
        eligibility = "All women, transgender persons, girls of all ages, and children under 5 years",
        coverage = "All MTC Ordinary White-Board city & suburban buses + non-AC low-floor electric buses across Chennai",
        fareDetails = "100% Free Zero-Fare Travel • Conductor issues official zero-value pink ticket",
        howToAvail = "No registration, application, or smart card needed. Board any White-board or designated low-floor MTC bus.",
        accentColor = ChennaiColors.MtcPinkWomen
    ),
    MtcWelfareSchemeItem(
        id = "ncmc_singara",
        title = "Singara Chennai NCMC Card Tap-and-Pay",
        titleTamil = "சிங்கார சென்னை NCMC அட்டை (பயணச்சீட்டு இயந்திரங்கள்)",
        emoji = "💳",
        eligibility = "All Chennai commuters with Singara Chennai NCMC RuPay card",
        coverage = "Valid across all MTC Electronic Ticketing Machines (ETMs) and CMRL Metro automatic gates",
        fareDetails = "Contactless digital ticketing + 20% discount on CMRL Metro lines",
        howToAvail = "Tap your physical card against conductor's handheld Android ETM terminal upon boarding.",
        accentColor = ChennaiColors.NeonCyan
    ),
    MtcWelfareSchemeItem(
        id = "student_pass",
        title = "Student Free Bus Pass Scheme",
        titleTamil = "மாணவர் இலவச பேருந்து அட்டை திட்டம்",
        emoji = "🎒",
        eligibility = "All school students (Classes 1 to 12) & government / aided college students",
        coverage = "Unlimited travel between residence and educational institution across non-AC MTC buses",
        fareDetails = "100% Free Travel for the entire academic year",
        howToAvail = "Issued via school/college administration with Smart RFID Card. Uniform/school ID accepted during initial phase.",
        accentColor = Color(0xFF38BDF8)
    ),
    MtcWelfareSchemeItem(
        id = "senior_tokens",
        title = "Senior Citizen 10 Free Tokens Scheme",
        titleTamil = "மூத்த குடிமக்கள் இலவச டோக்கன் திட்டம்",
        emoji = "👴",
        eligibility = "Senior citizens aged 60 years and above residing in Tamil Nadu",
        coverage = "Valid in all non-AC MTC buses (Ordinary, Express, and Deluxe)",
        fareDetails = "10 free journey tokens per month (60 tokens booklet issued every 6-month cycle)",
        howToAvail = "Collect booklet twice yearly at any of the 40+ MTC depots across Chennai with age proof (Aadhaar / Voter ID).",
        accentColor = ChennaiColors.MtcAmber
    ),
    MtcWelfareSchemeItem(
        id = "tayp_pass",
        title = "Travel As You Please (TAYP) Day Passes",
        titleTamil = "ஒரு நாள் வரம்பற்ற பயண பாஸ் (TAYP)",
        emoji = "🎫",
        eligibility = "Open to all commuters, tourists, and daily visitors in Chennai",
        coverage = "Unlimited travel for 1 full calendar day across all designated routes",
        fareDetails = "₹50 Day Pass (Non-AC buses) • ₹100 Chennai Ula Pass (Includes AC Volvo Low-Floor)",
        howToAvail = "Purchase on-the-spot from any MTC bus conductor or bus terminus ticket counter.",
        accentColor = ChennaiColors.LiveOnlineGreen
    ),
    MtcWelfareSchemeItem(
        id = "monthly_season",
        title = "Monthly Season Ticket (MST) - Gold & Diamond Passes",
        titleTamil = "மாதாந்திர சீசன் பாஸ் (கோல்ட் & டைமண்ட்)",
        emoji = "💳",
        eligibility = "Regular office commuters, college students, and professionals",
        coverage = "Unlimited travel for 30 consecutive days across Chennai transit network",
        fareDetails = "₹1,000 Gold Pass (All non-AC buses) • ₹2,000 Diamond Pass (Includes AC Volvo & Night Services)",
        howToAvail = "Renewable monthly at major MTC terminus counters or online via transport portal.",
        accentColor = Color(0xFFA855F7)
    ),
    MtcWelfareSchemeItem(
        id = "differently_abled",
        title = "Differently-Abled Persons Free Travel Scheme",
        titleTamil = "மாற்றுத்திறனாளிகள் மற்றும் உதவியாளர் இலவச பயணம்",
        emoji = "♿",
        eligibility = "Persons with disabilities holding UDID card or State Disability Welfare Identity",
        coverage = "All non-AC MTC buses with free travel for 1 accompanying escort/attendant for severe disabilities",
        fareDetails = "100% Free Travel across all MTC routes",
        howToAvail = "Show Government UDID smart card or State Welfare pass to conductor upon boarding.",
        accentColor = Color(0xFFF97316)
    )
)

sealed class MtcDirectoryTab(val title: String) {
    data object All : MtcDirectoryTab("All Routes")
    data object VetriPayanam : MtcDirectoryTab("🌸 Vidiyal Payanam")
    data object ElectricBus : MtcDirectoryTab("🌿 Switch EV")
    data object ExpressDeluxe : MtcDirectoryTab("⚡ Express & Deluxe")
    data object AcVolvo : MtcDirectoryTab("❄️ AC Low-Floor")
    data object Schemes : MtcDirectoryTab("📜 Govt Schemes")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MtcBusDirectoryScreen(
    onBackClick: () -> Unit,
    onSelectBusRoute: (MtcBusRouteItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf<MtcDirectoryTab>(MtcDirectoryTab.All) }

    // Filter routes based on search and selected tab
    val filteredRoutes = remember(searchQuery, selectedTab) {
        val query = searchQuery.trim().lowercase()
        CHENNAI_MTC_BUS_ROUTES.filter { route ->
            val matchesQuery = query.isEmpty() ||
                route.routeNumber.lowercase().contains(query) ||
                route.origin.lowercase().contains(query) ||
                route.destination.lowercase().contains(query) ||
                route.keyStops.any { it.lowercase().contains(query) }

            val matchesTab = when (selectedTab) {
                MtcDirectoryTab.All -> true
                MtcDirectoryTab.VetriPayanam -> route.isVetriPayanam
                MtcDirectoryTab.ElectricBus -> route.serviceType.contains("ELECTRIC", ignoreCase = true) || route.routeNumber.contains("EV", ignoreCase = true)
                MtcDirectoryTab.ExpressDeluxe -> route.serviceType.contains("DELUXE", ignoreCase = true) || route.serviceType.contains("EXPRESS", ignoreCase = true)
                MtcDirectoryTab.AcVolvo -> route.serviceType.contains("AC", ignoreCase = true)
                MtcDirectoryTab.Schemes -> false
            }

            matchesQuery && matchesTab
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Chennai MTC Bus Hub",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color.White
                        )
                        Text(
                            text = "25+ Routes, Stops & TN Welfare Schemes",
                            fontSize = 11.sp,
                            color = ChennaiColors.TextSecondary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ChennaiColors.SurfaceCardElevated
                )
            )
        },
        containerColor = ChennaiColors.DeepCanvas,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Spacer(modifier = Modifier.height(2.dp)) }

            // 1. Live Vector Animated MTC Bus Banner
            item {
                if (selectedTab == MtcDirectoryTab.ElectricBus) {
                    MtcElectricBusAnimation(
                        busNumber = "570 EV",
                        destination = "CMBT ↔ Kelambakkam OMR (Zero Emission)"
                    )
                } else {
                    MtcBusAnimation(
                        busNumber = "21G",
                        destination = "Broadway ↔ Tambaram West (Vidiyal Payanam)",
                        isVetriPayanam = true
                    )
                }
            }

            // 2. Search Input Field
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = "Search by route (21G, 570) or stop (Adyar, Porur)...",
                            fontSize = 13.sp,
                            color = ChennaiColors.TextMuted
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = ChennaiColors.TextSecondary
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = ChennaiColors.TextSecondary
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ChennaiColors.SurfaceCard,
                        unfocusedContainerColor = ChennaiColors.SurfaceCard,
                        focusedBorderColor = ChennaiColors.MtcRed,
                        unfocusedBorderColor = ChennaiColors.SurfaceBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // 3. Category Filter Chips
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val tabs = listOf(
                        MtcDirectoryTab.All,
                        MtcDirectoryTab.VetriPayanam,
                        MtcDirectoryTab.ElectricBus,
                        MtcDirectoryTab.ExpressDeluxe,
                        MtcDirectoryTab.AcVolvo,
                        MtcDirectoryTab.Schemes
                    )

                    tabs.forEach { tab ->
                        val isSelected = selectedTab == tab
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedTab = tab },
                            label = { Text(tab.title, fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = when (tab) {
                                    MtcDirectoryTab.VetriPayanam -> ChennaiColors.MtcPinkWomen
                                    MtcDirectoryTab.ElectricBus -> ChennaiColors.ElectricGreen
                                    else -> ChennaiColors.MtcRed
                                },
                                selectedLabelColor = Color.White,
                                containerColor = ChennaiColors.SurfaceCard,
                                labelColor = ChennaiColors.TextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) Color.Transparent else ChennaiColors.SurfaceBorder
                            )
                        )
                    }
                }
            }

            // 4. Welfare Schemes Section (Rendered when Schemes tab is active or under All tab)
            if (selectedTab == MtcDirectoryTab.Schemes || (selectedTab == MtcDirectoryTab.All && searchQuery.isEmpty())) {
                item {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("📜", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "TN GOVT WELFARE SCHEMES & PASSES",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = ChennaiColors.TextSecondary,
                                    letterSpacing = 1.2.sp
                                )
                            }
                            Surface(
                                color = ChennaiColors.MtcPinkWomen.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "OFFICIAL CONCESSIONS",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ChennaiColors.MtcPinkWomen,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                items(CHENNAI_TRANSIT_SCHEMES) { scheme ->
                    MtcSchemeCard(scheme = scheme)
                }

                if (selectedTab == MtcDirectoryTab.All && searchQuery.isEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(6.dp))
                        HorizontalDivider(color = ChennaiColors.SurfaceBorder)
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }

            // 5. Bus Routes Section Header
            if (selectedTab != MtcDirectoryTab.Schemes) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CHENNAI BUS ROUTES (${filteredRoutes.size})",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = ChennaiColors.TextSecondary,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = "Tap to explore stops",
                            fontSize = 11.sp,
                            color = ChennaiColors.TextMuted
                        )
                    }
                }

                if (filteredRoutes.isEmpty()) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = ChennaiColors.SurfaceCard,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("🚌", fontSize = 32.sp)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "No matching bus routes found",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Try searching by another route number or stop name like 'Tambaram' or 'Adyar'",
                                    fontSize = 12.sp,
                                    color = ChennaiColors.TextSecondary
                                )
                            }
                        }
                    }
                } else {
                    items(filteredRoutes) { route ->
                        MtcBusRouteCard(
                            route = route,
                            onSelectRoute = { onSelectBusRoute(route) }
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

/**
 * Interactive Card for an MTC Bus Route with Expandable Stop Sequence and Tracking CTA.
 */
@Composable
private fun MtcBusRouteCard(
    route: MtcBusRouteItem,
    onSelectRoute: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = ChennaiColors.SurfaceCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, ChennaiColors.SurfaceBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Route Number Badge + Service Type + Distance
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(route.badgeColor, route.badgeColor.copy(alpha = 0.8f))
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = route.routeNumber,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = route.serviceType,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (route.isVetriPayanam) ChennaiColors.MtcPinkWomen else ChennaiColors.MtcAmber
                        )
                        Text(
                            text = "${route.totalDistanceKm} km • ${route.frequency}",
                            fontSize = 11.sp,
                            color = ChennaiColors.TextSecondary
                        )
                    }
                }

                if (route.isVetriPayanam) {
                    Surface(
                        color = ChennaiColors.MtcPinkWomen.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ChennaiColors.MtcPinkWomen.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = "🌸 VETRI PAYANAM",
                            color = ChennaiColors.MtcPinkWomen,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Origin ↔ Destination
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "ORIGIN",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = ChennaiColors.LiveOnlineGreen
                    )
                    Text(
                        text = route.origin,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                Text(
                    text = "⇄",
                    color = ChennaiColors.MetroBlueLight,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Text(
                        text = "DESTINATION",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = ChennaiColors.AlertRed
                    )
                    Text(
                        text = route.destination,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Fare Info Strip
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = ChennaiColors.DeepCanvas,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ConfirmationNumber,
                            contentDescription = null,
                            tint = ChennaiColors.TextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = route.fareRange,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = ChennaiColors.TextPrimary
                        )
                    }

                    Text(
                        text = route.operatingHours,
                        fontSize = 10.sp,
                        color = ChennaiColors.TextMuted
                    )
                }
            }

            // Expandable Key Stops Section
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Text(
                        text = "KEY STOPS ALONG ROUTE (${route.keyStops.size} STOPS)",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = ChennaiColors.TextSecondary,
                        fontSize = 10.sp,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    route.keyStops.forEachIndexed { idx, stopName ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 3.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(ChennaiColors.SurfaceBorder, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${idx + 1}",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stopName,
                                fontSize = 12.sp,
                                color = if (idx == 0 || idx == route.keyStops.lastIndex) Color.White else ChennaiColors.TextSecondary,
                                fontWeight = if (idx == 0 || idx == route.keyStops.lastIndex) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Row: Expand Stops + Select Route
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clickable { isExpanded = !isExpanded }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isExpanded) "Hide Stops" else "View All ${route.keyStops.size} Stops",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ChennaiColors.MetroBlueLight
                    )
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = ChennaiColors.MetroBlueLight,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Button(
                    onClick = onSelectRoute,
                    colors = ButtonDefaults.buttonColors(containerColor = route.badgeColor),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Navigation,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Set & Track",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

/**
 * Card for Tamil Nadu Government Transit Welfare Schemes.
 */
@Composable
private fun MtcSchemeCard(scheme: MtcWelfareSchemeItem) {
    var isExpanded by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = ChennaiColors.SurfaceCardElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, scheme.accentColor.copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with Emoji, Title and Tamil Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(scheme.accentColor.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(scheme.emoji, fontSize = 20.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = scheme.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = scheme.titleTamil,
                            fontSize = 11.sp,
                            color = ChennaiColors.TextSecondary
                        )
                    }
                }

                IconButton(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = scheme.accentColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Fare / Benefit Highlight
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = scheme.accentColor.copy(alpha = 0.12f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = scheme.accentColor,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = scheme.fareDetails,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = scheme.accentColor
                    )
                }
            }

            // Expandable Details
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    HorizontalDivider(color = ChennaiColors.SurfaceBorder.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(10.dp))

                    DetailItem(label = "ELIGIBILITY", value = scheme.eligibility)
                    Spacer(modifier = Modifier.height(6.dp))
                    DetailItem(label = "COVERAGE", value = scheme.coverage)
                    Spacer(modifier = Modifier.height(6.dp))
                    DetailItem(label = "HOW TO AVAIL", value = scheme.howToAvail)
                }
            }
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = ChennaiColors.TextMuted,
            letterSpacing = 0.8.sp
        )
        Text(
            text = value,
            fontSize = 12.sp,
            color = ChennaiColors.TextPrimary,
            lineHeight = 16.sp
        )
    }
}
