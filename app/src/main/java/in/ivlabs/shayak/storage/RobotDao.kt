package `in`.ivlabs.shayak.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RobotDao {
    @Query("SELECT * FROM robotdata")
    fun getAll(): List<RobotStorageInterface.RobotData>

    @Insert
    fun insert(robot: RobotStorageInterface.RobotData)

}