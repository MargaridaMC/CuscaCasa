package net.teamtruta.cuscacasa.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SensorReadingDao {

    @Query("SELECT * FROM sensor_reading")
    fun getAll(): LiveData<List<SensorReading>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(reading: SensorReading)

    @Delete
    fun delete(reading: SensorReading)

    @Query("DELETE FROM sensor_reading")
    fun deleteAll()

}