package com.urbantransit.offline.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.urbantransit.offline.data.model.TransitMode

@Entity(tableName = "transit_routes")
data class TransitRoute(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "short_name")
    val shortName: String,

    @ColumnInfo(name = "long_name")
    val longName: String,

    @ColumnInfo(name = "mode")
    val mode: TransitMode,

    @ColumnInfo(name = "color_hex")
    val colorHex: String,

    @ColumnInfo(name = "text_color_hex")
    val textColorHex: String = "#FFFFFF",

    @ColumnInfo(name = "agency_name")
    val agencyName: String
)
