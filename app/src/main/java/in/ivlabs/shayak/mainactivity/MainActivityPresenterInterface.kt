package `in`.ivlabs.shayak.mainactivity

import `in`.ivlabs.shayak.storage.RobotStorageInterface

/**
 * Interface for Presenter in MVP pattern for MainActivity
 */
interface MainActivityPresenterInterface {

    /**
     * Established a connection with the robot
     * [UUID] Unique ID of the robot
     * @return returns true if connection is established
     */
    fun connectToRobot(robot: RobotStorageInterface.RobotData): Boolean

    fun storeRobot(robot: RobotStorageInterface.RobotData)

    fun forwardRobotsList(robotList: List<RobotStorageInterface.RobotData>)

    fun connectedCallback(robot: RobotStorageInterface.RobotData)

    fun sendMessage(msg: String)
}