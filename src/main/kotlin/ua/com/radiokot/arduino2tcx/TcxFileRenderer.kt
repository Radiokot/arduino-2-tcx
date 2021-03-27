package ua.com.radiokot.arduino2tcx

import ua.com.radiokot.arduino2tcx.model.RecordPoint
import java.io.BufferedWriter
import java.io.OutputStream
import java.text.SimpleDateFormat

class TcxFileRenderer {
    fun render(
        points: List<RecordPoint>,
        outputWriter: BufferedWriter,
    ) {
        require(points.isNotEmpty()) {
            "Can't render no points"
        }

        outputWriter.use { writer ->
            writer.write("""
            <?xml version="1.0" encoding="UTF-8" standalone="no" ?>
            <TrainingCenterDatabase xmlns="http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation
            ="http://www.garmin.com/xmlschemas/ActivityExtension/v2 http://www.garmin.com/xmlschemas/ActivityExtensionv2.xsd http://www.garmin.com/xmlschemas/TrainingCenterDat
            abase/v2 http://www.garmin.com/xmlschemas/TrainingCenterDatabasev2.xsd">

        """.trimIndent())

            val startTime = points.first().localTime

            writer.write(
                """
                    |   <Activities>
                    |       <Activity Sport="Biking">
                    |           <Id>42</Id>
                    |           <Lap StartTime="${DATE_FORMAT.format(startTime)}">
                    |
                """.trimMargin()
            )

            val totalTimeSeconds = (points.last().localTime.time - points.first().localTime.time) / 1000
            val totalDistanceMeters = points.sumBy { it.distanceIncrementMm } / 1000

            writer.write("""
            |               <TotalTimeSeconds>$totalTimeSeconds</TotalTimeSeconds>
            |               <DistanceMeters>$totalDistanceMeters</DistanceMeters>
            |               <TriggerMethod>Manual</TriggerMethod>
            |
        """.trimMargin())

            writer.write("""
            |                <Track>
            |
        """.trimMargin())

            var distanceMeters = 0.0

            points.forEach { point ->
                distanceMeters += point.distanceIncrementMm.toDouble() / 1000

                writer.write("""
                |                   <Trackpoint>
                |                       <Time>${DATE_FORMAT.format(point.localTime)}</Time>
                |                       <DistanceMeters>${"%.2f".format(distanceMeters)}</DistanceMeters>
                |                   </Trackpoint>
                |                   <Extensions>
                |                       <TPX xmlns="https://www8.garmin.com/xmlschemas/ActivityExtensionv2.xsd">
                |                           <Speed>${"%.2f".format(point.speedMph.toDouble() / 3600)}</Speed>
                |                       </TPX>
                |                   </Extensions>
                |
            """.trimMargin())
            }

            writer.write("""
            |                </Track>
            |            </Lap>
            |        </Activity>
            |    </Activities>
            |</TrainingCenterDatabase>
        """.trimMargin())
        }
    }

    private companion object {
        private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    }
}