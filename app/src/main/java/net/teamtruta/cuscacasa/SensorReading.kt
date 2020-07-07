package net.teamtruta.cuscacasa

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SensorReading (
    val deviceId: String,
    val readingTime: String,
    val IAQ: String,
    val IAQState: String,
    val temperature: String,
    val relativeHumidity: String,
    val pressure: String,
    val gas: String,
    val staticIAQ: String,
    val eCO2: String,
    val bVOCe: String,
    @PrimaryKey(autoGenerate = true) val uid: Int = 0)