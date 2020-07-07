package net.teamtruta.cuscacasa.db

import androidx.room.Database
import androidx.room.RoomDatabase
import net.teamtruta.cuscacasa.SensorReading
import net.teamtruta.cuscacasa.SensorReadingDao

@Database(entities = [SensorReading::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): SensorReadingDao
}