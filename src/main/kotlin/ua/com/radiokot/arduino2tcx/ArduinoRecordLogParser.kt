package ua.com.radiokot.arduino2tcx

import ua.com.radiokot.arduino2tcx.model.RecordPoint
import java.io.BufferedReader
import java.text.DateFormat
import java.util.*

class ArduinoRecordLogParser(
    private val timeFormat: DateFormat,
    private val valueDelimiters: CharArray,
) {
    fun parseLog(
        logDate: Date,
        logReader: BufferedReader,
    ): List<RecordPoint> {
        return logReader
            .readLines()
            .mapNotNull { line ->
                val split = line.split(*valueDelimiters)
                    .takeIf { it.size == 3 }
                    ?: return@mapNotNull null


                val completeLocalTimeCalendar = Calendar.getInstance().apply {
                    time = logDate

                    val relativePointTimeCalendar = Calendar.getInstance().apply {
                        time = timeFormat.parse(split[0])
                    }

                    set(Calendar.HOUR_OF_DAY, relativePointTimeCalendar[Calendar.HOUR_OF_DAY])
                    set(Calendar.MINUTE, relativePointTimeCalendar[Calendar.MINUTE])
                    set(Calendar.SECOND, relativePointTimeCalendar[Calendar.SECOND])
                    set(Calendar.MILLISECOND, relativePointTimeCalendar[Calendar.MILLISECOND])
                }

                RecordPoint(
                    localTime = completeLocalTimeCalendar.time,
                    speedMph = split.getOrNull(1)
                        ?.toIntOrNull()
                        ?: return@mapNotNull null,
                    distanceIncrementMm = split.getOrNull(2)
                        ?.toIntOrNull()
                        ?: return@mapNotNull null,
                )
            }
            .trimStartAndEnd()
    }

    private fun List<RecordPoint>.trimStartAndEnd() = subList(
        fromIndex = (indexOfFirst { it.distanceIncrementMm > 0 } - 1).coerceAtLeast(0),
        toIndex = (indexOfLast { it.distanceIncrementMm > 0 } + 2).coerceAtMost(size)
    )
}