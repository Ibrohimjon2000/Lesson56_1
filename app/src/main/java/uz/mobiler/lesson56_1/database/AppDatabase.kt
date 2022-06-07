package uz.mobiler.lesson56_1.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.mobiler.lesson56_1.database.dao.ModelDao
import uz.mobiler.lesson56_1.database.entity.Model

@Database(entities = [Model::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun modelDao(): ModelDao

    companion object {
        private var appDatabase: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (appDatabase == null) {
                appDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "my_db")
                    .allowMainThreadQueries()
                    .build()
            }
            return appDatabase!!
        }
    }
}
