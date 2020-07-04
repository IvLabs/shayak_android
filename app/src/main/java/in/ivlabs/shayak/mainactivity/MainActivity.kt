package `in`.ivlabs.shayak.mainactivity

import `in`.ivlabs.shayak.storage.RobotStorageInterface
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ivlabs.shayak.R
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainActivityViewInterface {
    var presenter: MainActivityPresenter? = null

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainActivityPresenter(this)

        main_add_fab.setOnClickListener {
            val bottomSheet =
                MainBottomSheet(presenter!!)
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun displayRobotList(list: List<RobotStorageInterface.RobotData>) {
        val robotAdapter = ItemAdapter<RobotItem>()
        val fastAdapter = FastAdapter.with(robotAdapter)
        main_recycler.adapter = fastAdapter
        main_recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        robotAdapter.add(
            list.sortedByDescending { it.isFavorite }.map {
                RobotItem(it)
            }.toList()
        )
        fastAdapter.onClickListener =
            { view: View?, iAdapter: IAdapter<RobotItem>, robotItem: RobotItem, i: Int ->
                presenter!!.connectToRobot(robotItem.robot)
            }
    }

    override fun robotConnected(robot: RobotStorageInterface.RobotData) {
        Toast.makeText(this, "Robot Connected, move to screen 2 here!", Toast.LENGTH_SHORT).show()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            presenter?.sendMessage("Hello World!")
        }
    }

}

class RobotItem(val robot: RobotStorageInterface.RobotData) :
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
