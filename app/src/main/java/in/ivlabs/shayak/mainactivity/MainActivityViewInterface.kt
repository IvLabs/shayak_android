package `in`.ivlabs.shayak.mainactivity

import `in`.ivlabs.shayak.storage.RobotStorageInterface

/**
 * Interface for View in MVP pattern for MainActivity
 */
interface MainActivityViewInterface {
    /**
     * Displays a list of Robots that are available
     * [list] List of data for the robots
     */
    fun displayRobotList(list: List<RobotStorageInterface.RobotData>)

    /**
     * Notifies the view that the robot was connected
     */
    fun robotConnected(robot: RobotStorageInterface.RobotData)

}