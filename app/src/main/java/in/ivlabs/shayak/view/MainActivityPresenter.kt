package `in`.ivlabs.shayak.view

import `in`.ivlabs.shayak.mainactivity.MainActivityPresenterInterface
import `in`.ivlabs.shayak.mainactivity.MainActivityViewInterface

class MainActivityPresenter(val activity: MainActivity) : MainActivityPresenterInterface {

    override fun connectToRobot(UUID: String): Boolean {
        TODO("Not yet implemented")
    }

    /**
     *
     *
     * @return a list of stored robots. Dummy data for now.
     */
    override fun getRobotsList(): List<MainActivityViewInterface.RobotViewData> {
        val robotList = List(5) {
            MainActivityViewInterface.RobotViewData(
                "$it",
                "Robot $it",
                "Short Description of Robot $it",
                it % 2 == 0
            )
        }
        return robotList
    }

}