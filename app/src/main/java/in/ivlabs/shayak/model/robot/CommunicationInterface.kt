package `in`.ivlabs.shayak.model.robot

import android.net.Uri

interface CommunicationInterface {

    /*  Returns the Uri of the robot camera feed
     */
    fun getRemoteCameraFeedUri() : Uri?

    /*  Get the robot audio level
     */
    fun getRobotAudioLevel() : Double?

    /*
     *
     */
    fun setRobotAudioLevel(level : Double) : Boolean

}