package `in`.ivlabs.shayak.view

import `in`.ivlabs.shayak.mainactivity.MainActivityPresenterInterface
import `in`.ivlabs.shayak.mainactivity.MainActivityViewInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivlabs.shayak.R
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.items.AbstractItem

val Boolean.int
    get() = if (this) 1 else 0

class MainActivity : AppCompatActivity(), MainActivityViewInterface,
    MainActivityPresenterInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        displayRobotList(getDummyRobotList())
    }

    override fun displayRobotList(list: List<MainActivityViewInterface.RobotViewData>) {
        val robotAdapter = ItemAdapter<RobotItem>()
        val fastAdapter = FastAdapter.with(robotAdapter)
        findViewById<RecyclerView>(R.id.main_recycler).adapter = fastAdapter
        findViewById<RecyclerView>(R.id.main_recycler).layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        robotAdapter.add(
            list.sortedWith(Comparator { o1: MainActivityViewInterface.RobotViewData, o2: MainActivityViewInterface.RobotViewData -> o2.isFavorite.int - o1.isFavorite.int })
                .map {
                    RobotItem(it)
                }.toList()
        )
        Toast.makeText(this, "Here!", Toast.LENGTH_SHORT).show()
    }

    override fun robotConnected(UUID: String) {
        TODO("Not yet implemented")
    }

    override fun connectToRobot(UUID: String): Boolean {
        TODO("Not yet implemented")
    }

    /*Gets Dummy List of Robots to test RecyclerView Population*/
    fun getDummyRobotList(): List<MainActivityViewInterface.RobotViewData> {
        val ROBOTS_TO_DISPLAY = List<MainActivityViewInterface.RobotViewData>(5) {
            MainActivityViewInterface.RobotViewData(
                "$it",
                "Robot $it",
                "Short Description of Robot $it",
                it % 2 == 0
            )
        }
        return ROBOTS_TO_DISPLAY
    }
}

class RobotItem(robot: MainActivityViewInterface.RobotViewData) :
    AbstractItem<RobotItem.ViewHolder>() {
    val name = robot.robotName
    val description = robot.robotDescription
    val favourite = robot.isFavorite
    val uuid = robot.UUID

    class ViewHolder(view: View) : FastAdapter.ViewHolder<RobotItem>(view) {
        var name_textView = view.findViewById<TextView>(R.id.robot_card_title)
        var description_textView = view.findViewById<TextView>(R.id.robot_card_description)
        var favourite_imageView = view.findViewById<ImageView>(R.id.robot_card_favourite)
        override fun bindView(item: RobotItem, payloads: List<Any>) {
            name_textView.text = item.name
            description_textView.text = item.description
            if (item.favourite) {
                favourite_imageView.setColorFilter(Color.BLUE)
            }
        }

        override fun unbindView(item: RobotItem) {

        }

    }

    override val layoutRes: Int
        get() = R.layout.robot_card
    override val type: Int
        get() = 0

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

}
