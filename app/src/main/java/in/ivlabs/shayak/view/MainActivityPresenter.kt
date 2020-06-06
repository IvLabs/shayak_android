package `in`.ivlabs.shayak.view

import `in`.ivlabs.shayak.mainactivity.MainActivityPresenterInterface
import `in`.ivlabs.shayak.storage.RobotStorage
import `in`.ivlabs.shayak.storage.RobotStorageInterface

class MainActivityPresenter(val activity: MainActivity) : MainActivityPresenterInterface {
    val storage = RobotStorage(this)

    /**
     * As soon as the presenter is made in an activity, display the robots list in that activity.
     */
    init {
        activity.displayRobotList(storage.fetchRobotList())
    }

    override fun connectToRobot(UUID: String): Boolean {
        TODO("Not yet implemented")
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