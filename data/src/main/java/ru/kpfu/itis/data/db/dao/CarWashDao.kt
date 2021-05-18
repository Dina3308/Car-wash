package ru.kpfu.itis.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ru.kpfu.itis.data.db.entity.CarWashLocal

@Dao
interface CarWashDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCarWashes(carWashes: List<CarWashLocal>)

    @Query("DELETE FROM car_washes")
    suspend fun deleteAllCarWashes()

    @Query("SELECT * FROM car_washes")
    suspend fun getCarWashes(): List<CarWashLocal>?

    @Transaction
    suspend fun updateCarWashes(carWashes: List<CarWashLocal>) {
        deleteAllCarWashes()
        insertCarWashes(carWashes)
    }
}
