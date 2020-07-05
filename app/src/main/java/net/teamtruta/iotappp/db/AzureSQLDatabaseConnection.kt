package net.teamtruta.iotappp.db

import net.teamtruta.iotappp.SensorReading
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

class AzureSQLDatabaseConnection {
    fun getNReadings(url : String, n: Int): List<SensorReading> {
        val readings: MutableList<SensorReading> =
            ArrayList()

        // u: bmejota
        // p: 145Dffsdasgaszzzz

        // Connect to database

        val connection: Connection
        try {
            connection = DriverManager.getConnection(url)

            // Create and execute a SELECT SQL statement.
            val selectSql = "SELECT TOP ($n) [DeviceId], [ReadingTime]," +
                    "[IAQ], " +
                    "[IAQState], " +
                    "[Temperature], " +
                    "[RelativeHumidity], " +
                    "[Pressure], " +
                    "[Gas], " +
                    "[StaticIAQ], " +
                    "[eCO2], " +
                    "[bVOCe] " +
                    " FROM [SensorReadingsFilteredAveraged]"

            val statement = connection.createStatement()
            val resultSet = statement.executeQuery(selectSql)

                    // Print results from select statement
                    println("Top 10 categories:")
                    while (resultSet.next()) {
                        val reading = SensorReading(
                            resultSet.getString(1),
                            resultSet.getString(2), resultSet.getString(3),
                            resultSet.getString(4), resultSet.getString(5),
                            resultSet.getString(6), resultSet.getString(7),
                            resultSet.getString(8), resultSet.getString(9),
                            resultSet.getString(10), resultSet.getString(11)
                        )
                        readings.add(reading)
                    }
                    connection.close()
                } catch (e: Exception) {
            e.printStackTrace()
        }
        return readings
    }
}