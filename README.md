# 🚇 Chennai Transit Companion (Offline-First)

> **Modern, High-Speed, 100% Offline Multimodal Transit Guide for Chennai Metro Rail (CMRL) & Chennai MTC Buses**

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin%201.9-purple.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose%20(Material%203)-4285F4.svg)](https://developer.android.com/jetpack/compose)
[![Architecture](https://img.shields.io/badge/Architecture-Clean%20%2B%20MVVM-orange.svg)](https://developer.android.com/topic/architecture)
[![Database](https://img.shields.io/badge/Database-Room%20(100%25%20Offline)-blue.svg)](https://developer.android.com/training/data-storage/room)
[![License](https://img.shields.io/badge/License-Apache%202.0-lightgrey.svg)](LICENSE)

---

## 🌟 Why the App Was Built

Chennai is one of India's largest metropolitan transit hubs, moving millions of commuters every day via the **Chennai Metro Rail Limited (CMRL)** and **Metropolitan Transport Corporation (MTC)** buses. However, commuters face persistent digital roadblocks:
- **Underground Cellular Blackouts**: Deep underground metro tunnels (such as Central Metro, High Court, Thousand Lights, and Anna Nagar) often have weak or non-existent cellular reception.
- **Peak Hour Network Congestion**: Overloaded cellular towers in major terminals like CMBT Koyambedu, Central, and Guindy cause live tracking and fare checking apps to stall or timeout.
- **Fragmented Transit Information**: Metro rail information and bus network details are usually segregated across different apps, forcing users to switch apps while on the move.

**Chennai Transit Companion** was built to solve this fundamental problem: providing a **lightning-fast, 100% offline-first companion** inspired by the beloved *"Where is my Train"* paradigm, giving commuters complete confidence in fares, routes, station stops, and welfare schemes without needing an active internet connection.

---

## 💡 What Problem It Solves

| Challenge | How Chennai Transit Companion Solves It |
|---|---|
| **No Internet in Tunnels** | **0ms Offline Engine**: All metro lines, intermediate stations, MTC bus corridors, and fare matrix rules are bundled locally inside an optimized SQLite Room database. |
| **Confusing Fares & Rules** | **Automatic Fare & Scheme Engine**: Dynamically calculates standard token fares, Singara Chennai NCMC Smartcard 20% discounts, and checks eligibility for Tamil Nadu's **Vidiyal & Vettri Payanam Thittam** (free travel for women). |
| **Phase 2 Metro Inaccuracies** | **Latest 2026 Phase 2 Corridor 4 Data**: Accurately maps the operational section between Poonamallee Bypass and Vadapalani (stopping at all intermediate stations up to Porur Junction, followed by an express non-stop run from Porur to Vadapalani). |
| **Heavy / Glitchy Animations** | **High-Performance Vector Canvas**: Custom lightweight mathematical Canvas drawing runs at a locked 60fps without heavy GIF or Lottie overhead. |
| **Quick Access Without Opening App** | **Dynamic Android Home Screen Widget**: Commuters can check upcoming departures, peak-hour frequency, and launch directly into tracking or fare calculation with one tap. |

---

## ✨ Unique Features

### 1. 📍 "Where Is My Train" Signature Station Progression Timeline
- Vertical progression view displaying every intermediate station, calculated distance, scheduled arrival times, and assigned platform/bay numbers.
- Live station tracking with passed/approaching visual states and optional arrival geofencing alarms.

### 2. 🎬 Cinematic Vector Motion Picture Banner
- A high-fidelity animated Chennai skyline rendered completely with Compose Canvas:
  - Silhouettes of Chennai Central Clocktower, Ripon Building dome, Marina Lighthouse with moving light beam, and OMR IT towers.
  - An illuminated elevated viaduct track with cruising metro trains and forward beam headlights.

### 3. 🚆 Dedicated Vehicle & Electric Bus Animations
- **Phase 2 Yellow Line GoA4 Train**: Driverless train car with distinctive amber livery, pantograph, and viaduct track.
- **MTC Switch EV Electric Bus**: Low-floor zero-emission green-and-white city bus featuring battery pod roof styling and illuminated digital LED destination board.
- **Dynamic Wheel Spoke Physics**: Mathematically synced spinning wheels on MTC buses during transit.

### 4. 🏷️ Tactile Spring-Physics Transit Stickers Ribbon
Interactive tactile badges celebrating Tamil Nadu transit schemes and status updates:
- 🌸 **Vidiyal & Vettri Payanam Thittam**: Free bus travel scheme for women.
- ⚡ **Phase 2 Yellow Line**: Operational status between Poonamallee and Vadapalani.
- 💳 **Singara Chennai NCMC Card**: National Common Mobility Card 20% discount.
- 🌿 **Switch EV Fleet**: Zero-emission fleet coverage.

### 5. 💳 3D Holographic Offline Smartcard Pass
- An interactive digital smartcard ticket rendered with real-time 3D perspective tilt physics (`interactive3DTilt`) and a holographic specular sheen brush.
- Displays offline QR validation tokens and dynamic concession pass badges.

### 6. 📱 Interactive Android App Widget
- Glassmorphic desktop companion displaying real-time departure ETA based on Chennai transit intervals.
- 3 fast-action direct deep-linking touchpoints:
  - 🚇 **Tracker**: Opens route timeline.
  - 🚌 **MTC Buses**: Opens 25+ route directory.
  - 💳 **Fare & Pass**: Opens offline ticket pass generator.

---

## 🛠️ Technologies & Libraries Used

- **Language**: [Kotlin 1.9.23](https://kotlinlang.org)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with [Material 3](https://m3.material.io)
- **Architecture**: Clean Architecture + MVVM + Unidirectional Data Flow (UDF)
- **Local Persistence**: [Room 2.6.1](https://developer.android.com/training/data-storage/room) with pre-seeded SQLite transit database
- **Dependency Injection**: [Dagger Hilt 2.51.1](https://dagger.dev/hilt/)
- **Asynchronous / Reactive**: Kotlin Coroutines 1.8.0 & Kotlin StateFlow
- **Background Operations**: AndroidX WorkManager 2.9.0
- **Location & Geofencing**: Google Play Services Location API
- **Widget Provider**: Android AppWidgetProvider with RemoteViews & Deep-Link PendingIntents

---

## 🏗️ How the Project Was Built

```
                     ┌─────────────────────────┐
                     │   Chennai Transit App   │
                     └────────────┬────────────┘
                                  │
         ┌────────────────────────┼────────────────────────┐
         ▼                        ▼                        ▼
┌──────────────────┐    ┌──────────────────┐    ┌──────────────────┐
│ Presentation / UI│    │   Domain Logic   │    │ Data / Storage   │
├──────────────────┤    ├──────────────────┤    ├──────────────────┤
│ Jetpack Compose  │    │ FareCalculator   │    │ Room Database    │
│ Custom 2D Canvas │    │ Route Finder     │    │ Seed Data        │
│ 3D Perspective   │    │ Geofence Manager │    │ DAO Entities     │
│ AppWidgetProvider│    │ Session / Pass   │    │ Offline Cache    │
└──────────────────┘    └──────────────────┘    └──────────────────┘
```

1. **Domain & Data Modeling**: Modeled transit entities (`TransitRoute`, `TransitStop`, `Schedule`, `FareRule`) and pre-populated all stations across Blue Line (26 stations), Green Line (17 stations), Phase 2 Yellow Line, and 25+ MTC bus routes.
2. **Rule-Based Offline Fare Engine**: Built a standalone formula calculating exact stage-based bus fares and distance-based metro fares with automated concession deductions.
3. **Hardware-Accelerated UI**: Implemented vector Canvas drawing routines utilizing `graphicsLayer` for hardware acceleration, eliminating frame drops during scroll.
4. **App Widget & Deep-Linking Pipeline**: Constructed RemoteViews widgets interacting seamlessly with `MainActivity` launch intents.
5. **Rigorous Build Verification**: Maintained 100% build health with Gradle 8/9 and Kotlin 1.9 compiler checks.

---

## 🚀 Getting Started & Building

### Prerequisites
- Android Studio Ladybug / Meerkat or newer
- JDK 17+ (or bundled Android Studio JBR)
- Android SDK 34 (minSdk 26, targetSdk 34)

### Clone & Compile
```bash
# Clone the repository
git clone https://github.com/ganesh18-lgtm/chennai-transit-companion.git
cd chennai-transit-companion

# Compile and assemble the debug APK
./gradlew assembleDebug
```

The compiled APK will be located at:
```
app/build/outputs/apk/debug/app-debug.apk
```

---

## 👨‍💻 Author

**Ganesh**  
- GitHub: [@ganesh18-lgtm](https://github.com/ganesh18-lgtm)  
- Email: [ganesh180208@gmail.com](mailto:ganesh180208@gmail.com)

---

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.
