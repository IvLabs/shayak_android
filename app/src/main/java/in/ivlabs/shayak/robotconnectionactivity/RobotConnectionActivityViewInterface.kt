package `in`.ivlabs.shayak.robotconnectionactivity

import `in`.ivlabs.shayak.model.robot.RobotDatabaseInterface

/**
 * Interface for View in MVP pattern for MainActivity
 */
interface RobotConnectionActivityViewInterface {

    /**
     * Displays a list of Robots that are available
     * [list] List of data for the robots
     */
    fun displayRobotList(list : List<RobotDatabaseInterface.RobotData>)

    /**
     * Notifies the view that the robot was connected
     */
    fun robotConnected(UUID : String)

    /**
     * Display Error while trying to connect
     */
    fun displayRobotConnectionError()

}