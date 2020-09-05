package `in`.ivlabs.shayak.FakeRobot

import `in`.ivlabs.shayak.model.robot.*
import android.net.Uri

class FakeRobot : HealthMonitorInterface,
    RobotConnectionInterface,
    RobotControlInterface,
    RobotDatabaseInterface,
    CommunicationInterface
{
    override fun isHealthy(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getErrorCode(): HealthMonitorInterface.RobotErrorCode {
        TODO("Not yet implemented")
    }

    override fun connect(UUID: String): Boolean {
        TODO("Not yet implemented. Connect to fake robot")
    }

    override fun disconnect() {
        TODO("Not yet implemented")
    }

    override fun sendRequest(msg: String): String {
        TODO("Not yet implemented")
    }

    override fun isAlive(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getConnectionStrength(): Float {
        TODO("Not yet implemented")
    }

    override fun setReferenceState(state: RobotControlInterface.RobotState) {
        TODO("Not yet implemented")
    }

    override fun getCurrentState(): RobotControlInterface.RobotState {
        TODO("Not yet implemented")
    }

    override fun getRobots(): List<RobotDatabaseInterface.RobotData> {
        return listOf<RobotDatabaseInterface.RobotData>(
            RobotDatabaseInterface.RobotData("0x2342", "Jenna", "First floor back room", true),
            RobotDatabaseInterface.RobotData("0x33423", "Aletta", "Second floor OPT", false))
    }

    override fun setRobotAsFavorite(UUID: String) {
        TODO("Not yet implemented. Setting Robot as favorite")
    }

    override fun getRemoteCameraFeedUri(): Uri? {
        return Uri.parse("rtsp://192.168.29.64:5540/ch0")
    }

    override fun getRobotAudioLevel(): Double? {
        return 10.0
    }

    override fun setRobotAudioLevel(level: Double): Boolean {
        return true
    }

}