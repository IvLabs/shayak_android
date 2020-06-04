package `in`.ivlabs.shayak.model.robot

/**
 * This interface is used to implement the robot controller
 * aka the robot motion
 */
interface RobotControlInterface {

    data class RobotState(val speed : Float, val turningRadius : Float )

    /**
     * Sets the reference value for the motion of the robot
     * This value is sent to the robot
     * [state] reference state
     */
    fun setReferenceState(state: RobotState)

    /**
     * Fetches the current state of the robot
     * @return current state of the robot motion
     */
    fun getCurrentState() : RobotState

}