package `in`.ivlabs.shayak.model.robot

import `in`.ivlabs.shayak.storage.RobotStorageInterface

/**
 * Interface for connecting to the robot
 */
interface RobotConnectionInterface {

    /**
     * Connects to the robot
     * [UUID] Unique Identifer for the robot to be connected to
     * @return true if connection is established
     */
    fun connect(robot:RobotStorageInterface.RobotData): Boolean

    /**
     * Disconnects from the robot
     */
    fun disconnect()

    /**
     * Sends the robot server a request
     * [msg] message to be sent
     * @return response from the robot
     */
    fun sendMessage(msg: String): String

    /**
     * Checks if the connection to the robot is still alive
     * @return true if the connection is alive
     */
    fun isAlive(): Boolean

    /**
     * Get the wireless signal strength
     * @return wireless signal strength (0.0 to 1.0)
     */
    fun getConnectionStrength(): Float
}