package net.teamtruta.cuscacasa.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.teamtruta.cuscacasa.db.AppDatabase
import net.teamtruta.cuscacasa.db.SensorReading
import net.teamtruta.cuscacasa.db.SensorReadingDao
import net.teamtruta.cuscacasa.network.AzureSQLDatabaseConnection

class SensorReadingRepository(private val database: AppDatabase) {

    var readings : LiveData<List<SensorReading>> = database.sensorReadingDao.getAll()

    suspend fun refreshReadings(connectionString : String){

        var readingsFromCloud : List<SensorReading> = mutableListOf()
        withContext(Dispatchers.IO){
            try{
                readingsFromCloud = getReadingsFromCloud(connectionString)
            } catch (cause : Throwable){
                throw DataRefreshError("Unable to refresh data", cause)
            }

            for(reading in readingsFromCloud){
                database.sensorReadingDao.insert(reading)
            }

        }

    }

    private fun getReadingsFromCloud(connectionString : String) : List<SensorReading> {
        val connection = AzureSQLDatabaseConnection()
        return connection.getNReadings(connectionString, 10)
    }
}

class DataRefreshError(message : String, cause : Throwable?) : Throwable(message, cause)