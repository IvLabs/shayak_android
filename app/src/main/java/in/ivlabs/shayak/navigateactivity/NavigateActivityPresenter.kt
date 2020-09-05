package `in`.ivlabs.shayak.navigateactivity

import `in`.ivlabs.shayak.model.robot.RobotRepository
import android.net.Uri

class NavigateActivityPresenter (view: NavigateActivityViewInterface) : NavigateActivityPresenterInterface {

    private var mView: NavigateActivityViewInterface? = view
    private var mRobotConnection = RobotRepository().getRobotConnection()
    private var mRobotHealthMonitor = RobotRepository().getHealthMonitor()
    private var mCommunication = RobotRepository().getCommunicationInterface()

    override fun onViewCreated() {
        mView?.updateCameraState(false)
        mView?.updateRobotAudioLevel(10)

        val feedUri = mCommunication.getRemoteCameraFeedUri()
        if (feedUri != null) {
            mView?.updateCameraFeed(feedUri)
        }
        else
            TODO("Could not get camera feed uri")
    }

    override fun updateJoystickInput(angle: Double, strength: Float) {
        TODO("Not yet implemented")
    }

    override fun setRobotAudioLevel(level: Float) {
        TODO("Not yet implemented")
    }

    override fun toggleTabVideo() {
        TODO("Not yet implemented")
    }

    override fun toggleRobotAudio() {
        TODO("Not yet implemented")
    }
}