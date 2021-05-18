package ru.kpfu.itis.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "car_washes")
class CarWashLocal(
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "description")
    var lat: Double,
    var lon: Double
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
