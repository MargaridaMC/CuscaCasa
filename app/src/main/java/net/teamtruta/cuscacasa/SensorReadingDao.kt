package net.teamtruta.cuscacasa

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SensorReadingDao {

    @Query("SELECT * FROM sensorReading")
    fun getAll(): List<SensorReading>

    @Insert
    fun insertAll(vararg readings : SensorReading)

    @Delete
    fun delete(reading: SensorReading)

}