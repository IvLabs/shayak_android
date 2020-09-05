package `in`.ivlabs.shayak.navigateactivity

import android.net.Uri
import org.videolan.libvlc.Media

/**
 * Interface for View in MVP pattern for Navigate Activity
 */
interface NavigateActivityViewInterface {

    /**
     * Updates the audio level display
     * [level] Audio level (0.0 to 1.0) where 0.0 is mute and 1.0 is loudest
     */
    fun updateRobotAudioLevel(level: Int)

    /**
     * Updates the Battery level display
     * [level] battery charge level (0.0 to 1.0)
     */
    fun updateBatteryCharge(level : Float)

    /**
     * Updates the Wireless connection strength display
     * [level] connection strength (0.0 to 1.0)
     */
    fun updateConnectionStrength(level : Float)

    /**
     * Update camera state
     * [state] true if camera is on
     */
    fun updateCameraState(state : Boolean)


    /**
     * Update the camera feed from the robot
     * [media] camera feed location
     */
    fun updateCameraFeed(media : Uri)
}