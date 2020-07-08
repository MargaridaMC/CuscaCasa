package net.teamtruta.cuscacasa.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(SensorReading::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    // Dao for sensor readings
    abstract fun sensorReadingDao(): SensorReadingDao
    var context: Context? = null

    companion object{

        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getDatabase(
            context: Context
        ) : AppDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "AppDatabase"
                ).build()
                instance.context = context.applicationContext
                INSTANCE = instance
                return instance
            }
        }

    }

}