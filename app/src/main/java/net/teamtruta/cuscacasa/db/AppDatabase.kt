package net.teamtruta.cuscacasa.db

import android.content.Context
import androidx.room.Database
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = arrayOf(SensorReading::class), version = 2)
abstract class AppDatabase : RoomDatabase() {

    // Dao for sensor readings
    abstract val sensorReadingDao: SensorReadingDao
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
                ).addMigrations(MIGRATION_1_2)
                    .build()
                instance.context = context.applicationContext
                INSTANCE = instance
                return instance
            }
        }

    }

}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Change type of values from string to doubles

        val oldTableName = "sensor_reading_old"
        val newTableName = "sensor_reading"

        // Rename old table
        database.execSQL("ALTER TABLE $newTableName RENAME TO $oldTableName")

        // Create new table
        database.execSQL("CREATE TABLE $newTableName (" +
                "uid INTEGER NOT NULL, " +
                "deviceId TEXT NOT NULL, " +
                "readingTime TEXT NOT NULL, " +
                "iAQ REAL NOT NULL, " +
                "iAQState REAL NOT NULL, " +
                "temperature REAL NOT NULL, " +
                "relativeHumidity REAL NOT NULL, " +
                "pressure REAL NOT NULL, " +
                "gas REAL NOT NULL, " +
                "staticIAQ REAL NOT NULL, " +
                "eCO2 REAL NOT NULL, " +
                "bVOCe REAL NOT NULL, " +
                "PRIMARY KEY(uid))")

        // Transfer values from old table to new one
        database.execSQL("INSERT INTO $newTableName (uid, deviceId, readingTime, iAQ, iAQState, " +
                "temperature, relativeHumidity, pressure, gas, staticIAQ, eCO2, bVOCe) " +
                "SELECT uid, deviceId, readingTime, CAST(iAQ as REAL), CAST(iAQState as REAL), " +
                "CAST(temperature as REAL), CAST(relativeHumidity as REAL), CAST(pressure as REAL), " +
                "CAST(gas as REAL), CAST(staticIAQ as REAL), CAST(eCO2 as REAL), CAST(bVOCe as REAL) " +
                "FROM $oldTableName")

        // Delete old table
        database.execSQL("DROP TABLE $oldTableName")
    }
}