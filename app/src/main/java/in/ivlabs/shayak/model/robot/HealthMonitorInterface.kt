package `in`.ivlabs.shayak.model.robot

/**
 * Interface for checking robot health
 */
interface HealthMonitorInterface {

    enum class RobotErrorCode {

    }

    /**
     * Robot health check
     * @return returns true if no errors are found
     */
    fun isHealthy() : Boolean

    /**
     * Error code check
     * @return returns error code
     */
    fun getErrorCode() : RobotErrorCode

}