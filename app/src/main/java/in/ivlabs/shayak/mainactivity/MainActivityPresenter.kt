package `in`.ivlabs.shayak.mainactivity

import `in`.ivlabs.shayak.model.robot.RobotConnection
import `in`.ivlabs.shayak.storage.RobotStorage
import `in`.ivlabs.shayak.storage.RobotStorageInterface
import android.os.Build
import androidx.annotation.RequiresApi

class MainActivityPresenter(val activity: MainActivity) : MainActivityPresenterInterface {
    val storage = RobotStorage(this)
    val connection = RobotConnection(this)

    /**
     * As soon as the presenter is made in an activity, display the robots list in that activity.
     */
    init {
        activity.displayRobotList(storage.fetchRobotList())
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun connectToRobot(UUID: String): Boolean {
        connection.connect(UUID)
        return true
    }

    /**
     *
     *
     * @param robot The robot to store/persist in the database.
     */
    override fun storeRobot(robot: RobotStorageInterface.RobotData) {
        storage.addRobot(robot)
    }

    /**
     * The Storage Model calls this method in the presenter. The presenter will display the robots to the Activity(View)
     *
     * @param robotList
     */
    override fun forwardRobotsList(robotList: List<RobotStorageInterface.RobotData>) {
        activity.displayRobotList(robotList)
    }


}