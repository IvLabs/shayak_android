package `in`.ivlabs.shayak.robotconnectionactivity

/**
 * Interface for Presenter in MVP pattern for MainActivity
 */
interface RobotConnectionActivityPresenterInterface {

    /**
     * This function runs when view is created
     * It helps populate the view
     */
    fun onViewCreated()

    /**
     * Established a connection with the robot
     * [UUID] Unique ID of the robot
     * @return returns true if connection is established
     */
    fun connectToRobot(UUID : String)

    /**
     * Mark the robot as favorite
     * [UUID] Unique ID of the robot
     */
    fun markRobotAsFavorite(UUID: String)
}