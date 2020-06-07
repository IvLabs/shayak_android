package `in`.ivlabs.shayak.storage

import `in`.ivlabs.shayak.mainactivity.MainActivityPresenter
import androidx.room.Room

class RobotStorage(val presenter: MainActivityPresenter) : RobotStorageInterface {
    private val db = Room.databaseBuilder(
        presenter.activity,
        RobotDatabase::class.java, "robot-database"
    ).allowMainThreadQueries().build()

    override fun fetchRobotList(): List<RobotStorageInterface.RobotData> {
        return db.robotDao().getAll()
    }

    override fun addRobot(robot: RobotStorageInterface.RobotData) {
        db.robotDao().insert(robot)
        presenter.activity.displayRobotList(fetchRobotList())
    }

}