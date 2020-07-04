package `in`.ivlabs.shayak.storage

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RobotStorageInterface.RobotData::class], version = 1)
abstract class RobotDatabase : RoomDatabase() {
    abstract fun robotDao(): RobotDao
}