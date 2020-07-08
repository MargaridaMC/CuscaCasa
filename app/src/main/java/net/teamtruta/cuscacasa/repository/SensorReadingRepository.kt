package net.teamtruta.cuscacasa.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.teamtruta.cuscacasa.db.SensorReading
import net.teamtruta.cuscacasa.db.SensorReadingDao
import net.teamtruta.cuscacasa.network.AzureSQLDatabaseConnection

class SensorReadingRepository(private val sensorReadingDao: SensorReadingDao) {

    var readings : LiveData<List<SensorReading>>? = sensorReadingDao.getAll()

    suspend fun refreshReadings(connectionString : String){

        var readingsFromCloud : List<SensorReading> = mutableListOf()
        withContext(Dispatchers.IO){
                readingsFromCloud = getReadingsFromCloud(connectionString)
        }
        for(reading in readingsFromCloud){
            sensorReadingDao.insert(reading)
        }
    }

    private fun getReadingsFromCloud(connectionString : String) : List<SensorReading> {
        val connection = AzureSQLDatabaseConnection()
        return connection.getNReadings(connectionString, 10)
    }
}