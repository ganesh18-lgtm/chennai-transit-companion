package com.urbantransit.offline.data.local

import com.urbantransit.offline.data.local.entity.FareRule
import com.urbantransit.offline.data.local.entity.Schedule
import com.urbantransit.offline.data.local.entity.TransitRoute
import com.urbantransit.offline.data.local.entity.TransitStop
import com.urbantransit.offline.data.model.TransitMode

object ChennaiTransitSeed {

    val routes = listOf(
        // CMRL Blue Line (Corridor 1)
        TransitRoute(
            id = "cmrl_blue",
            shortName = "Blue Line",
            longName = "Wimco Nagar Depot ↔ Chennai Airport",
            mode = TransitMode.SUBWAY,
            colorHex = "#2563EB", // Royal CMRL Blue
            textColorHex = "#FFFFFF",
            agencyName = "Chennai Metro Rail (CMRL)"
        ),
        // CMRL Green Line (Corridor 2)
        TransitRoute(
            id = "cmrl_green",
            shortName = "Green Line",
            longName = "Central Metro ↔ St. Thomas Mount",
            mode = TransitMode.SUBWAY,
            colorHex = "#059669", // Emerald CMRL Green
            textColorHex = "#FFFFFF",
            agencyName = "Chennai Metro Rail (CMRL)"
        ),
        // CMRL Yellow Line (Corridor 4 / Phase 2)
        TransitRoute(
            id = "cmrl_yellow",
            shortName = "Yellow Line",
            longName = "Poonamallee Bypass ↔ Vadapalani (Phase 2 Operational Stretch)",
            mode = TransitMode.SUBWAY,
            colorHex = "#EAB308", // Vivid Metro Yellow
            textColorHex = "#000000",
            agencyName = "Chennai Metro Rail (CMRL Phase 2)"
        ),
        // MTC 21G
        TransitRoute(
            id = "mtc_21g",
            shortName = "21G Deluxe",
            longName = "Broadway ↔ Tambaram (Via Marina, Mylapore, Guindy • Vetri Payanam)",
            mode = TransitMode.BUS,
            colorHex = "#DC2626", // MTC Crimson Red
            textColorHex = "#FFFFFF",
            agencyName = "MTC Chennai"
        ),
        // MTC 570
        TransitRoute(
            id = "mtc_570",
            shortName = "570 AC",
            longName = "CMBT Koyambedu ↔ Kelambakkam (Via Guindy, OMR IT Expressway)",
            mode = TransitMode.BUS,
            colorHex = "#0284C7", // MTC AC Blue
            textColorHex = "#FFFFFF",
            agencyName = "MTC Chennai"
        ),
        // MTC 102
        TransitRoute(
            id = "mtc_102",
            shortName = "102 Express",
            longName = "Broadway ↔ Kelambakkam (Via Santhome, Adyar, OMR • Vetri Payanam)",
            mode = TransitMode.BUS,
            colorHex = "#EA580C", // MTC Express Orange
            textColorHex = "#FFFFFF",
            agencyName = "MTC Chennai"
        ),
        // MTC 29C
        TransitRoute(
            id = "mtc_29c",
            shortName = "29C Ordinary",
            longName = "Perambur ↔ Besant Nagar (Vetri Payanam Women Free Travel)",
            mode = TransitMode.BUS,
            colorHex = "#EC4899", // Vetri Payanam Pink
            textColorHex = "#FFFFFF",
            agencyName = "MTC Chennai"
        ),
        // MTC 11G
        TransitRoute(
            id = "mtc_11g",
            shortName = "11G Deluxe",
            longName = "Broadway ↔ K.K. Nagar (Via T. Nagar Panagal Park)",
            mode = TransitMode.BUS,
            colorHex = "#7C3AED", // Violet
            textColorHex = "#FFFFFF",
            agencyName = "MTC Chennai"
        ),
        // MTC 54
        TransitRoute(
            id = "mtc_54",
            shortName = "54 Express",
            longName = "Broadway ↔ Poonamallee (Via Guindy & Porur)",
            mode = TransitMode.BUS,
            colorHex = "#059669",
            textColorHex = "#FFFFFF",
            agencyName = "MTC Chennai"
        )
    )

    val stops = listOf(
        // CMRL Blue Line
        TransitStop("cmrl_wimco", "Wimco Nagar Depot", 13.1673, 80.3015, "zone_north", true),
        TransitStop("cmrl_tollgate", "Tollgate Metro", 13.1384, 80.2941, "zone_north", true),
        TransitStop("cmrl_washermanpet", "Washermanpet", 13.1092, 80.2868, "zone_north", true),
        TransitStop("cmrl_central", "Puratchi Thalaivar Dr. M.G.R Central Metro", 13.0827, 80.2755, "zone_central", true),
        TransitStop("cmrl_lic", "LIC Metro", 13.0645, 80.2678, "zone_central", true),
        TransitStop("cmrl_thousand_lights", "Thousand Lights", 13.0560, 80.2562, "zone_central", true),
        TransitStop("cmrl_teynampet", "Teynampet", 13.0401, 80.2452, "zone_south", true),
        TransitStop("cmrl_nandanam", "Nandanam", 13.0305, 80.2392, "zone_south", true),
        TransitStop("cmrl_saidapet", "Saidapet Metro", 13.0210, 80.2223, "zone_south", true),
        TransitStop("cmrl_guindy", "Guindy Metro", 13.0092, 80.2131, "zone_south", true),
        TransitStop("cmrl_alandur", "Alandur Interchange", 12.9998, 80.2014, "zone_south", true),
        TransitStop("cmrl_meenambakkam", "Meenambakkam", 12.9840, 80.1834, "zone_airport", true),
        TransitStop("cmrl_airport", "Chennai International Airport", 12.9815, 80.1636, "zone_airport", true),

        // CMRL Green Line
        TransitStop("cmrl_egmore", "Egmore Metro", 13.0784, 80.2608, "zone_central", true),
        TransitStop("cmrl_nehru_park", "Nehru Park", 13.0789, 80.2514, "zone_central", true),
        TransitStop("cmrl_kilpauk", "Kilpauk Medical College", 13.0792, 80.2435, "zone_west", true),
        TransitStop("cmrl_shenoy_nagar", "Shenoy Nagar", 13.0772, 80.2241, "zone_west", true),
        TransitStop("cmrl_anna_nagar_tower", "Anna Nagar Tower", 13.0856, 80.2120, "zone_west", true),
        TransitStop("cmrl_thirumangalam", "Thirumangalam", 13.0852, 80.1998, "zone_west", true),
        TransitStop("cmrl_cmbt", "Koyambedu CMBT", 13.0694, 80.1948, "zone_west", true),
        TransitStop("cmrl_vadapalani", "Vadapalani Metro", 13.0512, 80.2115, "zone_west", true),
        TransitStop("cmrl_ashok_nagar", "Ashok Nagar", 13.0354, 80.2110, "zone_south", true),
        TransitStop("cmrl_st_thomas_mount", "St. Thomas Mount", 12.9942, 80.1989, "zone_south", true),

        // CMRL Yellow Line (Corridor 4 / Phase 2 Operational Section: Poonamallee to Porur & Vadapalani)
        TransitStop("cmrl_y_poonamallee_bypass", "Poonamallee Bypass (Depot)", 13.0485, 80.0920, "zone_west", true),
        TransitStop("cmrl_y_poonamallee_bus", "Poonamallee Bus Stand", 13.0512, 80.1060, "zone_west", true),
        TransitStop("cmrl_y_poonamallee_gh", "Poonamallee GH (Mullai Thottam)", 13.0505, 80.1140, "zone_west", true),
        TransitStop("cmrl_y_karayanchavadi", "Karayanchavadi", 13.0498, 80.1215, "zone_west", true),
        TransitStop("cmrl_y_kumananchavadi", "Kumananchavadi", 13.0482, 80.1340, "zone_west", true),
        TransitStop("cmrl_y_kattupakkam", "Kattupakkam", 13.0465, 80.1450, "zone_west", true),
        TransitStop("cmrl_y_iyyappanthangal", "Iyyappanthangal Depot", 13.0430, 80.1580, "zone_west", true),
        TransitStop("cmrl_y_thelliyaragaram", "Thelliyaragaram (Sri Ramachandra Hospital)", 13.0395, 80.1620, "zone_west", true),
        TransitStop("cmrl_y_porur_bypass", "Porur Bypass", 13.0385, 80.1650, "zone_west", true),
        TransitStop("cmrl_y_porur", "Porur Junction", 13.0375, 80.1685, "zone_west", true),
        // Intermediate skipped/non-stop stations between Porur and Vadapalani (bypassed in current operational phase)
        TransitStop("cmrl_y_valasaravakkam", "Valasaravakkam (Non-Stop / Skipped)", 13.0420, 80.1795, "zone_phase2_skip", false),
        TransitStop("cmrl_y_alwarthirunagar", "Alwarthirunagar (Non-Stop / Skipped)", 13.0445, 80.1880, "zone_phase2_skip", false),
        TransitStop("cmrl_y_saligramam", "Saligramam (Non-Stop / Skipped)", 13.0490, 80.1990, "zone_phase2_skip", false),
        // Beyond Vadapalani: Phase 2 Future Extension (Under Construction)
        TransitStop("cmrl_y_kodambakkam", "Kodambakkam Power House (Phase 2 Future)", 13.0520, 80.2240, "zone_phase2_future", false),
        TransitStop("cmrl_y_panagal_park", "Panagal Park (Phase 2 Future)", 13.0415, 80.2340, "zone_phase2_future", false),
        TransitStop("cmrl_y_royapettah", "Royapettah (Phase 2 Future)", 13.0530, 80.2640, "zone_phase2_future", false),
        TransitStop("cmrl_y_thirumayilai", "Thirumayilai (Phase 2 Future)", 13.0336, 80.2690, "zone_phase2_future", false),
        TransitStop("cmrl_y_lighthouse", "Lighthouse (Phase 2 Future)", 13.0402, 80.2798, "zone_phase2_future", false),

        // MTC Bus Stops (Chennai City & Suburbs)
        TransitStop("mtc_broadway", "Broadway Bus Terminus", 13.0886, 80.2882, "zone_central", true),
        TransitStop("mtc_central", "Central Bus Stand (Park Station)", 13.0827, 80.2755, "zone_central", true),
        TransitStop("mtc_anna_square", "Anna Square / Marina Beach", 13.0640, 80.2840, "zone_central", true),
        TransitStop("mtc_santhome", "Santhome Church Bus Stop", 13.0336, 80.2785, "zone_south", true),
        TransitStop("mtc_mylapore", "Mylapore Luz Corner", 13.0336, 80.2662, "zone_south", true),
        TransitStop("mtc_mandaveli", "Mandaveli Bus Depot", 13.0270, 80.2610, "zone_south", true),
        TransitStop("mtc_adyar_gate", "Adyar Gate / Chamiers Road", 13.0185, 80.2520, "zone_south", true),
        TransitStop("mtc_adyar", "Adyar Signal / Bus Depot", 13.0064, 80.2575, "zone_south", true),
        TransitStop("mtc_besant_nagar", "Besant Nagar Beach Terminus", 12.9998, 80.2685, "zone_south", true),
        TransitStop("mtc_madhya_kailash", "Madhya Kailash (OMR Junction)", 13.0075, 80.2450, "zone_south", true),
        TransitStop("mtc_tidel_park", "Tidel Park / Thiruvanmiyur", 12.9891, 80.2482, "zone_omr", true),
        TransitStop("mtc_srp_tools", "SRP Tools (Kandanchavadi)", 12.9750, 80.2460, "zone_omr", true),
        TransitStop("mtc_perungudi", "Perungudi OMR IT Corridor", 12.9640, 80.2425, "zone_omr", true),
        TransitStop("mtc_thoraipakkam", "Thoraipakkam Tea Stop", 12.9430, 80.2370, "zone_omr", true),
        TransitStop("mtc_karapakkam", "Karapakkam TCS Campus", 12.9190, 80.2315, "zone_omr", true),
        TransitStop("mtc_sholinganallur", "Sholinganallur OMR Junction", 12.9010, 80.2279, "zone_omr", true),
        TransitStop("mtc_navalur", "Navalur Toll / AGS Cinemas", 12.8480, 80.2250, "zone_omr", true),
        TransitStop("mtc_siruseri", "Siruseri SIPCOT IT Park", 12.8250, 80.2190, "zone_omr", true),
        TransitStop("mtc_kelambakkam", "Kelambakkam Bus Terminus", 12.7844, 80.2206, "zone_omr", true),
        TransitStop("mtc_saidapet", "Saidapet Court / Bus Stand", 13.0210, 80.2223, "zone_south", true),
        TransitStop("mtc_guindy", "Guindy Industrial Estate / Station", 13.0092, 80.2131, "zone_south", true),
        TransitStop("mtc_meenambakkam", "Meenambakkam Old Airport", 12.9840, 80.1834, "zone_airport", true),
        TransitStop("mtc_pallavaram", "Pallavaram Bus Stand", 12.9670, 80.1490, "zone_suburban", true),
        TransitStop("mtc_chromepet", "Chromepet MIT Gate", 12.9515, 80.1410, "zone_suburban", true),
        TransitStop("mtc_tambaram_sanatorium", "Tambaram Sanatorium / MEPZ", 12.9360, 80.1250, "zone_suburban", true),
        TransitStop("mtc_tambaram", "Tambaram West Bus Terminus", 12.9250, 80.1180, "zone_suburban", true),
        TransitStop("mtc_cmbt", "Koyambedu CMBT Bus Terminus", 13.0694, 80.1948, "zone_west", true),
        TransitStop("mtc_mmda", "MMDA Colony / Arumbakkam", 13.0650, 80.2030, "zone_west", true),
        TransitStop("mtc_vadapalani", "Vadapalani Bus Terminus", 13.0512, 80.2115, "zone_west", true),
        TransitStop("mtc_ashok_pillar", "Ashok Pillar / 11th Avenue", 13.0354, 80.2110, "zone_south", true),
        TransitStop("mtc_ekkattuthangal", "Ekkattuthangal / Kasi Theatre", 13.0220, 80.2050, "zone_south", true),
        TransitStop("mtc_kk_nagar", "K.K. Nagar Bus Terminus", 13.0390, 80.1980, "zone_west", true),
        TransitStop("mtc_perambur", "Perambur Bus Stand & Railway Station", 13.1090, 80.2395, "zone_north", true),
        TransitStop("mtc_otteri", "Otteri / Jamalia", 13.0980, 80.2520, "zone_north", true),
        TransitStop("mtc_doveton", "Doveton / Vepery", 13.0870, 80.2600, "zone_central", true),
        TransitStop("mtc_egmore", "Egmore Railway Station Bus Stand", 13.0784, 80.2608, "zone_central", true),
        TransitStop("mtc_chetpet", "Chetpet Signal / Harrington Road", 13.0720, 80.2420, "zone_central", true),
        TransitStop("mtc_loyola", "Loyola College / Nungambakkam", 13.0630, 80.2360, "zone_central", true),
        TransitStop("mtc_gemini", "Gemini / Anna Flyover (Mount Road)", 13.0560, 80.2520, "zone_central", true),
        TransitStop("mtc_dms", "DMS / Anna Arivalayam", 13.0450, 80.2480, "zone_central", true),
        TransitStop("mtc_panagal_park", "Panagal Park (T. Nagar)", 13.0415, 80.2340, "zone_central", true),
        TransitStop("mtc_t_nagar", "T. Nagar Bus Terminus", 13.0380, 80.2310, "zone_central", true),
        TransitStop("mtc_pondypbazaar", "Pondy Bazaar", 13.0400, 80.2370, "zone_central", true),
        TransitStop("mtc_kodambakkam", "Kodambakkam Power House / Liberty", 13.0520, 80.2240, "zone_central", true),
        TransitStop("mtc_virugambakkam", "Virugambakkam Market", 13.0470, 80.1920, "zone_west", true),
        TransitStop("mtc_valasaravakkam", "Valasaravakkam Bus Stop", 13.0420, 80.1795, "zone_west", true),
        TransitStop("mtc_alwarthirunagar", "Alwarthirunagar", 13.0445, 80.1880, "zone_west", true),
        TransitStop("mtc_porur", "Porur Roundtana / Junction", 13.0375, 80.1685, "zone_west", true),
        TransitStop("mtc_ramachandra", "Sri Ramachandra Hospital (Porur)", 13.0390, 80.1600, "zone_west", true),
        TransitStop("mtc_iyyappanthangal", "Iyyappanthangal Bus Depot", 13.0430, 80.1580, "zone_west", true),
        TransitStop("mtc_kattupakkam", "Kattupakkam Bus Stop", 13.0465, 80.1450, "zone_west", true),
        TransitStop("mtc_kumananchavadi", "Kumananchavadi Signal", 13.0482, 80.1340, "zone_west", true),
        TransitStop("mtc_karayanchavadi", "Karayanchavadi Bus Stop", 13.0498, 80.1215, "zone_west", true),
        TransitStop("mtc_poonamallee", "Poonamallee Bus Terminus", 13.0512, 80.1060, "zone_west", true),
        TransitStop("mtc_thirumangalam", "Thirumangalam Signal", 13.0852, 80.1998, "zone_west", true),
        TransitStop("mtc_anna_nagar_west", "Anna Nagar West Depot", 13.0920, 80.2010, "zone_west", true),
        TransitStop("mtc_thiruvanmiyur", "Thiruvanmiyur Bus Depot", 12.9860, 80.2590, "zone_south", true),
        TransitStop("mtc_velachery", "Velachery Vijayanagar Bus Stand", 12.9780, 80.2220, "zone_south", true)
    )

    val schedules = listOf(
        // Blue Line
        Schedule("sch_b1", "cmrl_blue", "cmrl_wimco", "06:00", "06:01", 1),
        Schedule("sch_b2", "cmrl_blue", "cmrl_tollgate", "06:07", "06:08", 2),
        Schedule("sch_b3", "cmrl_blue", "cmrl_washermanpet", "06:14", "06:15", 3),
        Schedule("sch_b4", "cmrl_blue", "cmrl_central", "06:21", "06:23", 4),
        Schedule("sch_b5", "cmrl_blue", "cmrl_lic", "06:27", "06:28", 5),
        Schedule("sch_b6", "cmrl_blue", "cmrl_thousand_lights", "06:31", "06:32", 6),
        Schedule("sch_b7", "cmrl_blue", "cmrl_teynampet", "06:36", "06:37", 7),
        Schedule("sch_b8", "cmrl_blue", "cmrl_nandanam", "06:40", "06:41", 8),
        Schedule("sch_b9", "cmrl_blue", "cmrl_saidapet", "06:44", "06:45", 9),
        Schedule("sch_b10", "cmrl_blue", "cmrl_guindy", "06:48", "06:50", 10),
        Schedule("sch_b11", "cmrl_blue", "cmrl_alandur", "06:54", "06:56", 11),
        Schedule("sch_b12", "cmrl_blue", "cmrl_meenambakkam", "07:01", "07:02", 12),
        Schedule("sch_b13", "cmrl_blue", "cmrl_airport", "07:07", "07:10", 13),

        // Green Line
        Schedule("sch_g1", "cmrl_green", "cmrl_central", "06:10", "06:12", 1),
        Schedule("sch_g2", "cmrl_green", "cmrl_egmore", "06:16", "06:17", 2),
        Schedule("sch_g3", "cmrl_green", "cmrl_nehru_park", "06:20", "06:21", 3),
        Schedule("sch_g4", "cmrl_green", "cmrl_kilpauk", "06:24", "06:25", 4),
        Schedule("sch_g5", "cmrl_green", "cmrl_shenoy_nagar", "06:28", "06:29", 5),
        Schedule("sch_g6", "cmrl_green", "cmrl_anna_nagar_tower", "06:33", "06:34", 6),
        Schedule("sch_g7", "cmrl_green", "cmrl_thirumangalam", "06:37", "06:38", 7),
        Schedule("sch_g8", "cmrl_green", "cmrl_cmbt", "06:42", "06:44", 8),
        Schedule("sch_g9", "cmrl_green", "cmrl_vadapalani", "06:49", "06:51", 9),
        Schedule("sch_g10", "cmrl_green", "cmrl_ashok_nagar", "06:55", "06:56", 10),
        Schedule("sch_g11", "cmrl_green", "cmrl_alandur", "07:02", "07:04", 11),
        Schedule("sch_g12", "cmrl_green", "cmrl_st_thomas_mount", "07:09", "07:12", 12),

        // Yellow Line (Corridor 4 - Phase 2 Operational Section: Poonamallee Bypass ↔ Vadapalani)
        // Stops at all stations between Poonamallee & Porur; direct non-stop express between Porur & Vadapalani
        Schedule("sch_y1", "cmrl_yellow", "cmrl_y_poonamallee_bypass", "06:00", "06:02", 1),
        Schedule("sch_y2", "cmrl_yellow", "cmrl_y_poonamallee_bus", "06:05", "06:06", 2),
        Schedule("sch_y3", "cmrl_yellow", "cmrl_y_poonamallee_gh", "06:09", "06:10", 3),
        Schedule("sch_y4", "cmrl_yellow", "cmrl_y_karayanchavadi", "06:13", "06:14", 4),
        Schedule("sch_y5", "cmrl_yellow", "cmrl_y_kumananchavadi", "06:17", "06:18", 5),
        Schedule("sch_y6", "cmrl_yellow", "cmrl_y_kattupakkam", "06:21", "06:22", 6),
        Schedule("sch_y7", "cmrl_yellow", "cmrl_y_iyyappanthangal", "06:25", "06:26", 7),
        Schedule("sch_y8", "cmrl_yellow", "cmrl_y_thelliyaragaram", "06:29", "06:30", 8),
        Schedule("sch_y9", "cmrl_yellow", "cmrl_y_porur_bypass", "06:33", "06:34", 9),
        Schedule("sch_y10", "cmrl_yellow", "cmrl_y_porur", "06:37", "06:39", 10),
        // Express non-stop run from Porur directly to Vadapalani (no intermediate stops)
        Schedule("sch_y11", "cmrl_yellow", "cmrl_vadapalani", "06:52", "06:55", 11),

        // MTC 21G Bus (Broadway ↔ Tambaram)
        Schedule("sch_m1", "mtc_21g", "mtc_broadway", "06:30", "06:35", 1),
        Schedule("sch_m2", "mtc_21g", "mtc_central", "06:42", "06:44", 2),
        Schedule("sch_m3", "mtc_21g", "mtc_anna_square", "06:55", "06:57", 3),
        Schedule("sch_m4", "mtc_21g", "mtc_santhome", "07:06", "07:08", 4),
        Schedule("sch_m5", "mtc_21g", "mtc_mylapore", "07:16", "07:18", 5),
        Schedule("sch_m6", "mtc_21g", "mtc_mandaveli", "07:23", "07:25", 6),
        Schedule("sch_m7", "mtc_21g", "mtc_adyar", "07:34", "07:36", 7),
        Schedule("sch_m8", "mtc_21g", "mtc_madhya_kailash", "07:42", "07:44", 8),
        Schedule("sch_m9", "mtc_21g", "mtc_saidapet", "07:53", "07:55", 9),
        Schedule("sch_m10", "mtc_21g", "mtc_guindy", "08:02", "08:05", 10),
        Schedule("sch_m11", "mtc_21g", "mtc_meenambakkam", "08:14", "08:16", 11),
        Schedule("sch_m12", "mtc_21g", "mtc_pallavaram", "08:24", "08:26", 12),
        Schedule("sch_m13", "mtc_21g", "mtc_chromepet", "08:33", "08:35", 13),
        Schedule("sch_m14", "mtc_21g", "mtc_tambaram_sanatorium", "08:42", "08:44", 14),
        Schedule("sch_m15", "mtc_21g", "mtc_tambaram", "08:52", "08:55", 15),

        // MTC 570 Bus (CMBT ↔ Kelambakkam via OMR IT Corridor)
        Schedule("sch_570_1", "mtc_570", "mtc_cmbt", "07:00", "07:05", 1),
        Schedule("sch_570_2", "mtc_570", "mtc_mmda", "07:10", "07:12", 2),
        Schedule("sch_570_3", "mtc_570", "mtc_vadapalani", "07:19", "07:21", 3),
        Schedule("sch_570_4", "mtc_570", "mtc_ashok_pillar", "07:27", "07:29", 4),
        Schedule("sch_570_5", "mtc_570", "mtc_ekkattuthangal", "07:34", "07:36", 5),
        Schedule("sch_570_6", "mtc_570", "mtc_guindy", "07:44", "07:47", 6),
        Schedule("sch_570_7", "mtc_570", "mtc_velachery", "07:58", "08:00", 7),
        Schedule("sch_570_8", "mtc_570", "mtc_tidel_park", "08:12", "08:15", 8),
        Schedule("sch_570_9", "mtc_570", "mtc_srp_tools", "08:22", "08:24", 9),
        Schedule("sch_570_10", "mtc_570", "mtc_perungudi", "08:30", "08:32", 10),
        Schedule("sch_570_11", "mtc_570", "mtc_thoraipakkam", "08:39", "08:41", 11),
        Schedule("sch_570_12", "mtc_570", "mtc_karapakkam", "08:48", "08:50", 12),
        Schedule("sch_570_13", "mtc_570", "mtc_sholinganallur", "08:58", "09:01", 13),
        Schedule("sch_570_14", "mtc_570", "mtc_navalur", "09:12", "09:14", 14),
        Schedule("sch_570_15", "mtc_570", "mtc_siruseri", "09:22", "09:24", 15),
        Schedule("sch_570_16", "mtc_570", "mtc_kelambakkam", "09:35", "09:40", 16),

        // MTC 102 Bus (Broadway ↔ Kelambakkam via Santhome & OMR)
        Schedule("sch_102_1", "mtc_102", "mtc_broadway", "06:40", "06:45", 1),
        Schedule("sch_102_2", "mtc_102", "mtc_central", "06:52", "06:54", 2),
        Schedule("sch_102_3", "mtc_102", "mtc_anna_square", "07:05", "07:07", 3),
        Schedule("sch_102_4", "mtc_102", "mtc_santhome", "07:16", "07:18", 4),
        Schedule("sch_102_5", "mtc_102", "mtc_adyar", "07:30", "07:33", 5),
        Schedule("sch_102_6", "mtc_102", "mtc_thiruvanmiyur", "07:42", "07:44", 6),
        Schedule("sch_102_7", "mtc_102", "mtc_tidel_park", "07:50", "07:52", 7),
        Schedule("sch_102_8", "mtc_102", "mtc_srp_tools", "07:59", "08:01", 8),
        Schedule("sch_102_9", "mtc_102", "mtc_perungudi", "08:07", "08:09", 9),
        Schedule("sch_102_10", "mtc_102", "mtc_thoraipakkam", "08:16", "08:18", 10),
        Schedule("sch_102_11", "mtc_102", "mtc_sholinganallur", "08:29", "08:32", 11),
        Schedule("sch_102_12", "mtc_102", "mtc_navalur", "08:44", "08:46", 12),
        Schedule("sch_102_13", "mtc_102", "mtc_siruseri", "08:54", "08:56", 13),
        Schedule("sch_102_14", "mtc_102", "mtc_kelambakkam", "09:10", "09:15", 14),

        // MTC 29C Bus (Perambur ↔ Besant Nagar - Women Free Travel)
        Schedule("sch_29c_1", "mtc_29c", "mtc_perambur", "06:50", "06:55", 1),
        Schedule("sch_29c_2", "mtc_29c", "mtc_otteri", "07:02", "07:04", 2),
        Schedule("sch_29c_3", "mtc_29c", "mtc_doveton", "07:11", "07:13", 3),
        Schedule("sch_29c_4", "mtc_29c", "mtc_egmore", "07:20", "07:22", 4),
        Schedule("sch_29c_5", "mtc_29c", "mtc_chetpet", "07:30", "07:32", 5),
        Schedule("sch_29c_6", "mtc_29c", "mtc_gemini", "07:42", "07:44", 6),
        Schedule("sch_29c_7", "mtc_29c", "mtc_dms", "07:51", "07:53", 7),
        Schedule("sch_29c_8", "mtc_29c", "mtc_mylapore", "08:03", "08:05", 8),
        Schedule("sch_29c_9", "mtc_29c", "mtc_mandaveli", "08:11", "08:13", 9),
        Schedule("sch_29c_10", "mtc_29c", "mtc_adyar_gate", "08:19", "08:21", 10),
        Schedule("sch_29c_11", "mtc_29c", "mtc_adyar", "08:29", "08:31", 11),
        Schedule("sch_29c_12", "mtc_29c", "mtc_besant_nagar", "08:40", "08:45", 12),

        // MTC 11G Bus (Broadway ↔ K.K. Nagar via T. Nagar)
        Schedule("sch_11g_1", "mtc_11g", "mtc_broadway", "07:00", "07:05", 1),
        Schedule("sch_11g_2", "mtc_11g", "mtc_central", "07:12", "07:14", 2),
        Schedule("sch_11g_3", "mtc_11g", "mtc_gemini", "07:30", "07:32", 3),
        Schedule("sch_11g_4", "mtc_11g", "mtc_dms", "07:39", "07:41", 4),
        Schedule("sch_11g_5", "mtc_11g", "mtc_panagal_park", "07:51", "07:54", 5),
        Schedule("sch_11g_6", "mtc_11g", "mtc_pondypbazaar", "07:59", "08:01", 6),
        Schedule("sch_11g_7", "mtc_11g", "mtc_kodambakkam", "08:10", "08:12", 7),
        Schedule("sch_11g_8", "mtc_11g", "mtc_vadapalani", "08:21", "08:23", 8),
        Schedule("sch_11g_9", "mtc_11g", "mtc_ashok_pillar", "08:30", "08:32", 9),
        Schedule("sch_11g_10", "mtc_11g", "mtc_kk_nagar", "08:40", "08:45", 10),

        // MTC 54 Bus (Broadway ↔ Poonamallee via Porur)
        Schedule("sch_54_1", "mtc_54", "mtc_broadway", "06:45", "06:50", 1),
        Schedule("sch_54_2", "mtc_54", "mtc_central", "06:57", "06:59", 2),
        Schedule("sch_54_3", "mtc_54", "mtc_egmore", "07:08", "07:10", 3),
        Schedule("sch_54_4", "mtc_54", "mtc_chetpet", "07:18", "07:20", 4),
        Schedule("sch_54_5", "mtc_54", "mtc_loyola", "07:27", "07:29", 5),
        Schedule("sch_54_6", "mtc_54", "mtc_kodambakkam", "07:38", "07:40", 6),
        Schedule("sch_54_7", "mtc_54", "mtc_vadapalani", "07:49", "07:52", 7),
        Schedule("sch_54_8", "mtc_54", "mtc_virugambakkam", "07:59", "08:01", 8),
        Schedule("sch_54_9", "mtc_54", "mtc_valasaravakkam", "08:08", "08:10", 9),
        Schedule("sch_54_10", "mtc_54", "mtc_porur", "08:20", "08:23", 10),
        Schedule("sch_54_11", "mtc_54", "mtc_ramachandra", "08:29", "08:31", 11),
        Schedule("sch_54_12", "mtc_54", "mtc_iyyappanthangal", "08:37", "08:39", 12),
        Schedule("sch_54_13", "mtc_54", "mtc_kattupakkam", "08:45", "08:47", 13),
        Schedule("sch_54_14", "mtc_54", "mtc_kumananchavadi", "08:53", "08:55", 14),
        Schedule("sch_54_15", "mtc_54", "mtc_poonamallee", "09:05", "09:10", 15)
    )

    val fareRules = listOf(
        FareRule("fr_cmrl_subway", TransitMode.SUBWAY, null, null, baseFareCents = 1000L, perKmFareCents = 150L, peakSurchargeCents = 0L),
        FareRule("fr_mtc_bus", TransitMode.BUS, null, null, baseFareCents = 500L, perKmFareCents = 100L, peakSurchargeCents = 0L)
    )
}
