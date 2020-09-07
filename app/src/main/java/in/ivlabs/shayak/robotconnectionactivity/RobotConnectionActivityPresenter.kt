package `in`.ivlabs.shayak.robotconnectionactivity

import `in`.ivlabs.shayak.model.robot.RobotRepository
import `in`.ivlabs.shayak.robotconnectionactivity.RobotConnectionActivityPresenterInterface
import `in`.ivlabs.shayak.robotconnectionactivity.RobotConnectionActivityViewInterface
import `in`.ivlabs.shayak.view.NavigateActivity
import android.content.Intent

class RobotConnectionActivityPresenter(view : RobotConnectionActivityViewInterface)
    : RobotConnectionActivityPresenterInterface
{
    private var mView: RobotConnectionActivityViewInterface? = view
    private var mRobotConnection = RobotRepository().getRobotConnection()
    private var mRobotDatabase = RobotRepository().getRobotDatabase()

    override fun onViewCreated() {
        mView?.displayRobotList(mRobotDatabase.getRobots());
        print(mRobotDatabase.getRobots())
    }

    override fun connectToRobot(UUID: String) {
        if(mRobotConnection.connect(UUID)) {
            mView?.robotConnected(UUID);
        }
        else
            mView?.displayRobotConnectionError();
    }

    override fun markRobotAsFavorite(UUID: String) {
        mRobotDatabase.setRobotAsFavorite(UUID)
    }

}