package `in`.ivlabs.shayak.navigateactivity

/**
 * Interface for Presenter in MVP pattern for Navigate Activity
 */
interface NavigateActivityPresenterInterface {

    /**
     * This function runs when view is created
     * It helps populate the view
     */
    fun onViewCreated()

    /**
     * Starts/Updates the motion of the robot if robot is connected
     * [xAxisLocation] normalized value of x location of the joystick (-1.0 to 1.0)
     * [yAxisLocation] normalized value of y location of the joystick (-1.0 to 1.0)
     */
    fun updateJoystickInput(angle: Double, strength: Float)

    /**
     * Sets the loudness of the speaker on the robot
     * [level] Audio level (0.0 to 1.0) where 0.0 is mute and 1.0 is loudest
     */
    fun setRobotAudioLevel(level : Float)

    /**
     * Toggles the video on the tab ie turns the video feed from tab to the robot
     * On/Off
     */
    fun toggleTabVideo()

    /**
     * Mutes/Unmutes the robot speaker
     */
    fun toggleRobotAudio()

}