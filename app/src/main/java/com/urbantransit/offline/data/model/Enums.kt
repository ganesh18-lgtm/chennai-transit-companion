package com.urbantransit.offline.data.model

enum class TransitMode {
    BUS,
    SUBWAY,
    COMMUTER_RAIL,
    TRAM,
    FERRY
}

enum class SyncStatus {
    PENDING,
    SYNCING,
    SYNCED,
    FAILED
}
