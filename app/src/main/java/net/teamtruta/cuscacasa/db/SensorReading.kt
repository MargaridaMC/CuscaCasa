package net.teamtruta.cuscacasa.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sensor_reading")
data class SensorReading (
    val deviceId: String,
    val readingTime: String,
    val iAQ: Double,
    val iAQState: Double,
    val temperature: Double,
    val relativeHumidity: Double,
    val pressure: Double,
    val gas: Double,
    val staticIAQ: Double,
    val eCO2: Double,
    val bVOCe: Double,
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0
    )