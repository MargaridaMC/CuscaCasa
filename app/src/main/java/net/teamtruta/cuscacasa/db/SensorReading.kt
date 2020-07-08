package net.teamtruta.cuscacasa.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sensor_reading")
data class SensorReading (
    val deviceId: String,
    val readingTime: String,
    val iAQ: String,
    val iAQState: String,
    val temperature: String,
    val relativeHumidity: String,
    val pressure: String,
    val gas: String,
    val staticIAQ: String,
    val eCO2: String,
    val bVOCe: String,
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0
    )