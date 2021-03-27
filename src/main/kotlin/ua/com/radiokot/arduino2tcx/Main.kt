@file:JvmName("Main")

package ua.com.radiokot.arduino2tcx

import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.text.SimpleDateFormat
import java.util.*

object Main {
    init {
        Locale.setDefault(Locale.ENGLISH)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        if (args.isEmpty()) {
            System.err.println("Expected log file path as the only argument")
            return
        }

        val inputFile = File(args.first())
        val inputFileAttributes = Files.readAttributes(inputFile.toPath(), BasicFileAttributes::class.java)

        val points = ArduinoRecordLogParser(
            timeFormat = SimpleDateFormat("HH:mm:ss.SSS"),
            valueDelimiters = charArrayOf(' ', ';'),
        )
            .parseLog(
                logDate = Date(inputFileAttributes.creationTime().toMillis()),
                logReader = inputFile.bufferedReader(Charsets.UTF_8)
            )

        val outputFile = File("./output.tcx")

        TcxFileRenderer().render(
            points = points,
            outputWriter = outputFile.bufferedWriter(Charsets.UTF_8)
        )
    }
}