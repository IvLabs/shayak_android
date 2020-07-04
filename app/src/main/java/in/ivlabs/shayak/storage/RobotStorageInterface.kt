package `in`.ivlabs.shayak.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

interface RobotStorageInterface {
    /**
     * Robot Data for display/in.ivlabs.shayak.storage
     * [UUID] Unique ID for a given Robot. Cannot be changed after the robot is manufactured
     * [robotName] Human Friendly Name of the robot. Can be changed by the user
     * [robotDescription] Short Description of the robot. Useful for the user
     */
    @Entity
    data class RobotData(
        @PrimaryKey val UUID: String,
        @ColumnInfo(name = "robotName") val robotName: String,
        @ColumnInfo(name = "robotDescription") val robotDescription: String,
        @ColumnInfo(name = "isFavourite") val isFavorite: Boolean
    )

    fun fetchRobotList(): List<RobotData>

    fun addRobot(robot: RobotData)

}