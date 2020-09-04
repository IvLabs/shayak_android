package `in`.ivlabs.shayak.model.robot

interface RobotDatabaseInterface
{
    /**
     * Robot Data for display
     * [UUID] Unique ID for a given Robot. Cannot be changed after the robot is manufactured
     * [robotName] Human Friendly Name of the robot. Can be changed by the user
     * [robotDescription] Short Description of the robot. Useful for the user
     */
    data class RobotData(val UUID : String, val robotName : String, val robotDescription : String, val isFavorite : Boolean)

    fun getRobots() : List<RobotData>
    fun setRobotAsFavorite(UUID : String)
}