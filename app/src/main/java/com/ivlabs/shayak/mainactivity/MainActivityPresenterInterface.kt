package com.ivlabs.shayak.mainactivity

/**
 * Interface for Presenter in MVP pattern for MainActivity
 */
interface MainActivityPresenterInterface {

    /**
     * Established a connection with the robot
     * [UUID] Unique ID of the robot
     * @return returns true if connection is established
     */
    fun connectToRobot(UUID : String) : Boolean
}