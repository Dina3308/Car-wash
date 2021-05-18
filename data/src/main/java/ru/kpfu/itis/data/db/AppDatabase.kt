package ru.kpfu.itis.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.kpfu.itis.data.db.dao.CarWashDao
import ru.kpfu.itis.data.db.entity.CarWashLocal

@Database(entities = [CarWashLocal::class], version = 1)
abstract class AppDatabase() : RoomDatabase() {

    abstract fun carWashDao(): CarWashDao

    companion object {

        private const val DATABASE_NAME = "car_wash.db"

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }
}
