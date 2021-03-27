package ua.com.radiokot.arduino2tcx.model

import java.util.*

data class RecordPoint(
    val speedMph: Int,
    val distanceIncrementMm: Int,
    val localTime: Date,
)