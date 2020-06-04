package com.ivlabs.shayak.mainactivity

interface MainActivityViewInterface {

    /**
     * Robot Data for display
     * [UUID] Unique ID for a given Robot. Cannot be changed after the robot is manufactured
     * [robotName] Human Friendly Name of the robot. Can be changed by the user
     * [robotDescription] Short Description of the robot. Useful for the user
     */
    data class RobotViewData(val UUID : String, val robotName : String, val robotDescription : String, val isFavorite : String)

    /**
     * Displays a list of Robots that are available
     * [list] List of data for the robots
     */
    fun displayRobotList(list : List<RobotViewData>)

    /**
     * Notifies the view that the robot was connected
     */
    fun robotConnected(UUID : String)

}