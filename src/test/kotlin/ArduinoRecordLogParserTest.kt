import org.junit.Assert
import org.junit.Test
import ua.com.radiokot.arduino2tcx.ArduinoRecordLogParser
import java.io.BufferedReader
import java.io.StringReader
import java.text.SimpleDateFormat
import java.util.*

class ArduinoRecordLogParserTest {
    init {
        Locale.setDefault(Locale.ENGLISH)
    }

    private val date = SimpleDateFormat("dd.MM.yyy").parse("25.03.2021")
    private val logDateFormat = SimpleDateFormat("HH:mm:ss.SSS")
    private val valueDelimiters = charArrayOf(' ', ';')

    @Test
    fun parseSimple() {
        val linesReader = """
            20:52:35.329 11855;3168
            20:52:36.321 20784;5808
            20:52:37.343 26968;7656
        """.trimIndent()
            .let { BufferedReader(StringReader(it)) }

        val points = ArduinoRecordLogParser(logDateFormat, valueDelimiters)
            .parseLog(date, linesReader)

        Assert.assertEquals(
            "[RecordPoint(speedMph=11855, distanceIncrementMm=3168, localTime=Thu Mar 25 20:52:35 EET 2021), RecordPoint(speedMph=20784, distanceIncrementMm=5808, localTime=Thu Mar 25 20:52:36 EET 2021), RecordPoint(speedMph=26968, distanceIncrementMm=7656, localTime=Thu Mar 25 20:52:37 EET 2021)]",
            points.toString()
        )
    }

    @Test
    fun parseDirty() {
        val linesReader = """
            20:52:20.428 USB device detected
            20:52:23.305 Connected to PL2303 device
            20:52:35.329 11855;3168
            ~~~~
            
            20:52:36.321 20784;5808
            20:52:37.343 26968;7656
            21:25:01.343 Disconnected
            21:25:01.493 Connected to PL2303 device
        """.trimIndent()
            .let { BufferedReader(StringReader(it)) }

        val points = ArduinoRecordLogParser(logDateFormat, valueDelimiters)
            .parseLog(date, linesReader)

        Assert.assertEquals(
            "[RecordPoint(speedMph=11855, distanceIncrementMm=3168, localTime=Thu Mar 25 20:52:35 EET 2021), RecordPoint(speedMph=20784, distanceIncrementMm=5808, localTime=Thu Mar 25 20:52:36 EET 2021), RecordPoint(speedMph=26968, distanceIncrementMm=7656, localTime=Thu Mar 25 20:52:37 EET 2021)]",
            points.toString()
        )
    }

    @Test
    fun trimZeros() {
        val linesReader = """
            20:52:20.428 USB device detected
            20:52:23.305 Connected to PL2303 device
            20:52:23.318 0;0
            20:52:25.320 0;0
            20:52:27.322 0;0
            20:52:29.324 0;0
            20:52:31.326 0;0
            20:52:33.327 0;0
            20:52:35.329 11855;3168
            20:52:36.321 20784;5808
            20:52:37.343 26968;7656
            21:24:50.448 26758;7656
            21:24:51.478 25836;7392
            21:24:52.526 23601;6864
            21:24:54.528 8254;1848
            21:24:56.528 0;0
            21:24:58.531 0;0
            21:25:00.533 0;0
            21:25:01.343 Disconnected
            21:25:01.493 Connected to PL2303 device
            21:25:02.536 0;0
            21:25:03.552 Disconnected
        """.trimIndent()
            .let { BufferedReader(StringReader(it)) }

        val points = ArduinoRecordLogParser(logDateFormat, valueDelimiters)
            .parseLog(date, linesReader)

        Assert.assertEquals(
            "[RecordPoint(speedMph=0, distanceIncrementMm=0, localTime=Thu Mar 25 20:52:33 EET 2021), RecordPoint(speedMph=11855, distanceIncrementMm=3168, localTime=Thu Mar 25 20:52:35 EET 2021), RecordPoint(speedMph=20784, distanceIncrementMm=5808, localTime=Thu Mar 25 20:52:36 EET 2021), RecordPoint(speedMph=26968, distanceIncrementMm=7656, localTime=Thu Mar 25 20:52:37 EET 2021), RecordPoint(speedMph=26758, distanceIncrementMm=7656, localTime=Thu Mar 25 21:24:50 EET 2021), RecordPoint(speedMph=25836, distanceIncrementMm=7392, localTime=Thu Mar 25 21:24:51 EET 2021), RecordPoint(speedMph=23601, distanceIncrementMm=6864, localTime=Thu Mar 25 21:24:52 EET 2021), RecordPoint(speedMph=8254, distanceIncrementMm=1848, localTime=Thu Mar 25 21:24:54 EET 2021), RecordPoint(speedMph=0, distanceIncrementMm=0, localTime=Thu Mar 25 21:24:56 EET 2021)]",
            points.toString()
        )
    }
}