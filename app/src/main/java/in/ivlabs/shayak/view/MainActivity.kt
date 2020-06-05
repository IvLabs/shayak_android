package `in`.ivlabs.shayak.view

import `in`.ivlabs.shayak.mainactivity.MainActivityViewInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ivlabs.shayak.R
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.activity_main.*

val Boolean.int
    get() = if (this) 1 else 0

class MainActivity : AppCompatActivity(), MainActivityViewInterface {
    val presenter = MainActivityPresenter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val robotList = presenter.getRobotsList()
        displayRobotList(robotList)
    }

    override fun displayRobotList(list: List<MainActivityViewInterface.RobotViewData>) {
        val robotAdapter = ItemAdapter<RobotItem>()
        val fastAdapter = FastAdapter.with(robotAdapter)
        main_recycler.adapter = fastAdapter
        main_recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        robotAdapter.add(
            list.sortedWith(Comparator { o1: MainActivityViewInterface.RobotViewData, o2: MainActivityViewInterface.RobotViewData -> o2.isFavorite.int - o1.isFavorite.int })
                .map {
                    RobotItem(it)
                }.toList()
        )
    }

    override fun robotConnected(UUID: String) {
        TODO("Not yet implemented")
    }

}

class RobotItem(robot: MainActivityViewInterface.RobotViewData) :
    AbstractItem<RobotItem.ViewHolder>() {
    val name = robot.robotName
    val description = robot.robotDescription
    val favourite = robot.isFavorite
    val uuid = robot.UUID

    class ViewHolder(view: View) : FastAdapter.ViewHolder<RobotItem>(view) {
        val nameTextview = view.findViewById<TextView>(R.id.robot_card_title)
        var descriptionTextview = view.findViewById<TextView>(R.id.robot_card_description)
        var favouriteImageview = view.findViewById<ImageView>(R.id.robot_card_favourite)
        override fun bindView(item: RobotItem, payloads: List<Any>) {
            nameTextview.text = item.name
            descriptionTextview.text = item.description
            if (item.favourite) {
                favouriteImageview.setColorFilter(Color.BLUE)
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
