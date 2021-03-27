import org.junit.Assert
import org.junit.Test
import ua.com.radiokot.arduino2tcx.TcxFileRenderer
import ua.com.radiokot.arduino2tcx.model.RecordPoint
import java.io.BufferedOutputStream
import java.io.BufferedWriter
import java.io.OutputStream
import java.io.StringWriter
import java.util.*

class TcxFileRendererTest {
    @Test
    fun renderSimple() {
        var startTime = 100500

        val points = listOf(
            RecordPoint(
                speedMph = 11855,
                distanceIncrementMm = 3168,
                localTime = Date(++startTime * 1000L)
            ),
            RecordPoint(
                speedMph = 20784,
                distanceIncrementMm = 5808,
                localTime = Date(++startTime * 1000L)
            ),
            RecordPoint(
                speedMph = 26968,
                distanceIncrementMm = 7656,
                localTime = Date(++startTime * 1000L)
            )
        )

        val writer = StringWriter()

        TcxFileRenderer().render(
            points,
            BufferedWriter(writer)
        )

        writer.flush()
        val result = writer.toString()

        Assert.assertEquals(
            """
                <?xml version="1.0" encoding="UTF-8" standalone="no" ?>
                <TrainingCenterDatabase xmlns="http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation
                ="http://www.garmin.com/xmlschemas/ActivityExtension/v2 http://www.garmin.com/xmlschemas/ActivityExtensionv2.xsd http://www.garmin.com/xmlschemas/TrainingCenterDat
                abase/v2 http://www.garmin.com/xmlschemas/TrainingCenterDatabasev2.xsd">
                   <Activities>
                       <Activity Sport="Biking">
                           <Id>42</Id>
                           <Lap StartTime="1970-01-02T05:55:01.000Z">
                               <TotalTimeSeconds>2</TotalTimeSeconds>
                               <DistanceMeters>16</DistanceMeters>
                               <TriggerMethod>Manual</TriggerMethod>
                                <Track>
                                   <Trackpoint>
                                       <Time>1970-01-02T05:55:01.000Z</Time>
                                       <DistanceMeters>3,17</DistanceMeters>
                                   </Trackpoint>
                                   <Extensions>
                                       <TPX xmlns="https://www8.garmin.com/xmlschemas/ActivityExtensionv2.xsd">
                                           <Speed>3,29</Speed>
                                       </TPX>
                                   </Extensions>
                                   <Trackpoint>
                                       <Time>1970-01-02T05:55:02.000Z</Time>
                                       <DistanceMeters>8,98</DistanceMeters>
                                   </Trackpoint>
                                   <Extensions>
                                       <TPX xmlns="https://www8.garmin.com/xmlschemas/ActivityExtensionv2.xsd">
                                           <Speed>5,77</Speed>
                                       </TPX>
                                   </Extensions>
                                   <Trackpoint>
                                       <Time>1970-01-02T05:55:03.000Z</Time>
                                       <DistanceMeters>16,63</DistanceMeters>
                                   </Trackpoint>
                                   <Extensions>
                                       <TPX xmlns="https://www8.garmin.com/xmlschemas/ActivityExtensionv2.xsd">
                                           <Speed>7,49</Speed>
                                       </TPX>
                                   </Extensions>
                                </Track>
                            </Lap>
                        </Activity>
                    </Activities>
                </TrainingCenterDatabase>
            """.trimIndent(),
            result
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun renderEmpty() {
        TcxFileRenderer().render(emptyList(), BufferedWriter(StringWriter()))
    }
}