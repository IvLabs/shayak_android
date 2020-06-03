package com.ivlabs.shayak.contract

import android.net.Uri
import com.ivlabs.shayak.robot.RobotState

/**
 * Contract interface for Model View Presenter
 */
interface ContractInterface{

    /**
     * Robot Data for display
     * [UUID] Unique ID for a given Robot. Cannot be changed after the robot is manufactured
     * [robotName] Human Friendly Name of the robot. Can be changed by the user
     * [robotDescription] Short Description of the robot. Useful for the user
     */
    data class RobotViewData(val UUID : String, val robotName : String, val robotDescription : String)

    /*
     * Interface for View. Each activity or fragment has to implement this for itself
     */
    interface View {
        /**
         * To be called in the constructor of the view
         */
        fun initView()

        /**
         * To be called whenever data update is needed
         */
        fun updateViewData()
    }

    /**
     * Each view will have a reference to the presenter. The presenter is used
     * to seperate UI logic from business logic
     */
    interface Presenter {
        /**
         * Provides a list of cached robots that the app already knows about
         * @return returns a list of cached robots
         */
        fun getRobotList() : List<RobotViewData>

        /**
         * Adds the robot to a list of favorites so they can be easily retrived
         * [UUID] Unique ID of the robot
         */
        fun addRobotToFavorite(UUID : String)

        /**
         * Checks if the robot is in the list of favorites
         * [UUID] Unique ID of the robot
         * @return true if the robot is favorite
         */
        fun isRobotFavorite(UUID : String) : Boolean

        /**
         * Updates the robot data stored in the cache
         * [UUID] Unique ID of the robot
         * [robotName] New name
         */
        fun updateRobotName(UUID : String, robotName : String)

        /**
        * Updates the robot data stored in the cache
        * [UUID] Unique ID of the robot
        * [robotDescription] New Description
        */
        fun updateRobotDescription(UUID : String, robotDescription: String)

        /**
         * Established a connection with the robot
         * [UUID] Unique ID of the robot
         * @return returns true if connection is established
         */
        fun connectToRobot(UUID : String) :Boolean

        /**
         * Starts/Updates the motion of the robot if robot is connected
         * [xLocationValue] x location of the joystick (-1.0 to 1.0)
         * [yLocationValue] y location of the joystick (-1.0 to 1.0)
         */
        fun setRobotMotion(xLocationValue : Float, yLocationValue : Float)

        /**
         * Sets the loudness of the speaker on the robot
         * [level] Audio level (0.0 to 1.0) where 0.0 is mute and 1.0 is loudest
         */
        fun setRobotAudioLevel(level : Float)

        /**
         * Retrives the audio level of the robot
         * @return returns Audio level (0.0 to 1.0) where 0.0 is mute and 1.0 is loudest
         */
        fun getRobotAudioLevel() : Float

        /**
         * Reads the radio strength of the communication
         * @return returns strength (0.0 to 1.0)
         */
        fun getRobotConnectionStrengthPercent() : Float

        /**
         * Reads the amount of battery left in the robot
         * @return returns battery charge level (0.0 to 1.0)
         */
        fun getRobotBatteryCharge() : Float
    }

    /**
     * Interface for Model. It is responsible for all the communication with the robot
     */
    interface Model {

        /**
         * Establishes a wireless connection with the robot
         * [UUID] Unique ID of the robot
         * @return returns true if connection is established
         */
        fun connectToRobot(UUID : String) : Boolean

        /**
         * Talks to the robot and gets the adress of the Video Feed
         * @returns the URI of the videofeed
         */
        fun getRobotVideoFeedURI() : Uri

        /**
         * Talks to the robot and retrives robot health
         * @returns true if robot is operational
         */
        fun isRobotHealthy() : Boolean

        /**
         * Starts/Updates the motion of the robot if robot is connected
         * [speed] desired speed in meters per sec
         * [turningRadius] desired turning radius in meters
         */
        fun setRobotMotion(state : RobotState)

        /**
         * Sets the loudness of the speaker on the robot
         * [level] Audio level (0.0 to 1.0) where 0.0 is mute and 1.0 is loudest
         */
        fun setRobotAudioLevel(level : Float)

        /**
         * Reads the amount of battery left in the robot
         * @return returns battery charge level (0.0 to 1.0)
         */
        fun getRobotBatteryCharge() : Float
    }

}