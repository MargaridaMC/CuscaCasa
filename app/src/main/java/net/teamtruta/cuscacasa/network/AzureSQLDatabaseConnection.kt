package net.teamtruta.cuscacasa.network

import net.teamtruta.cuscacasa.db.SensorReading
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

class AzureSQLDatabaseConnection {
    fun getNReadings(url : String, n: Int): List<SensorReading> {
        val readings: MutableList<SensorReading> =
            ArrayList()

        // Connect to database
        val connection: Connection = DriverManager.getConnection(url)

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
                            resultSet.getString(2), resultSet.getDouble(3),
                            resultSet.getDouble(4), resultSet.getDouble(5),
                            resultSet.getDouble(6), resultSet.getDouble(7),
                            resultSet.getDouble(8), resultSet.getDouble(9),
                            resultSet.getDouble(10), resultSet.getDouble(11)
                        )
                        readings.add(reading)
                    }
                    connection.close()
        return readings
    }
}