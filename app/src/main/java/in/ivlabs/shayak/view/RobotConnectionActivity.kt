package `in`.ivlabs.shayak.view

import `in`.ivlabs.shayak.model.robot.RobotDatabaseInterface
import `in`.ivlabs.shayak.robotconnectionactivity.RobotConnectionActivityPresenter
import `in`.ivlabs.shayak.robotconnectionactivity.RobotConnectionActivityViewInterface
import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.vision.barcode.Barcode
import com.ivlabs.shayak.R
import info.androidhive.barcode.BarcodeReader

class RobotConnectionActivity : AppCompatActivity(),
RobotConnectionActivityViewInterface,
BarcodeReader.BarcodeReaderListener
{
    private var mPresenter =
        RobotConnectionActivityPresenter(
            this
        )
    var barcodeReader: BarcodeReader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_robot_connection)

        val robotSelectionView = findViewById<View>(R.id.robotSelectionView) as RecyclerView
        robotSelectionView.layoutManager = LinearLayoutManager(this)

        barcodeReader =
            (supportFragmentManager.findFragmentById(R.id.qrCodeScanView) as BarcodeReader)
        Toast.makeText(getApplicationContext(), "Test string", Toast.LENGTH_SHORT).show();

        mPresenter.onViewCreated();
    }

    override fun displayRobotList(list: List<RobotDatabaseInterface.RobotData>) {
        val robotSelectionView = findViewById<View>(R.id.robotSelectionView) as RecyclerView
        val robotSelectionAdapter = RobotSelectionAdapter(list, mPresenter)
        robotSelectionView.adapter = robotSelectionAdapter
    }

    override fun robotConnected(UUID: String) {
        val intent = Intent(this, NavigateActivity::class.java).apply {
            putExtra("Server_IP", UUID)
        }
        startActivity(intent)
    }

    override fun displayRobotConnectionError() {
        TODO("Not yet implemented")
    }

    override fun onBitmapScanned(sparseArray: SparseArray<Barcode>?) {
        Toast.makeText(getApplicationContext(), "Robot Connected", Toast.LENGTH_SHORT).show();
        TODO("Not yet implemented")
    }

    override fun onScannedMultiple(barcodes: MutableList<Barcode>?) {
        Toast.makeText(getApplicationContext(), "Robot Connected", Toast.LENGTH_SHORT).show();
        TODO("Not yet implemented")
    }

    override fun onCameraPermissionDenied() {
        TODO("Not yet implemented. Camera Permission Denied")
    }

    override fun onScanned(barcode: Barcode?) {
        //Toast.makeText(getApplicationContext(), "Robot Connected", Toast.LENGTH_SHORT).show();
        barcodeReader!!.playBeep();
        if(barcode?.rawValue != null)
        mPresenter.connectToRobot(barcode.rawValue)
    }

    override fun onScanError(errorMessage: String?) {
        TODO("Not yet implemented")
    }
}

class RobotSelectionAdapter(private val mRobotList: List<RobotDatabaseInterface.RobotData>, private val mPresenter : RobotConnectionActivityPresenter)
    : RecyclerView.Adapter<RobotSelectionAdapter.ViewHolder>() {

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val nameTextView = itemView.findViewById<TextView>(R.id.robotName)
        val descriptionTextView = itemView.findViewById<TextView>(R.id.robotDescription)
        val robotStarButton = itemView.findViewById<ImageButton>(R.id.robotFavButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val robotView = inflater.inflate(R.layout.item_robot_connection, parent, false)

        return ViewHolder(robotView)
    }

    override fun getItemCount(): Int {
        return mRobotList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val robot : RobotDatabaseInterface.RobotData = mRobotList.get(position)
        holder.nameTextView.text = robot.robotName
        holder.descriptionTextView.text = robot.robotDescription

        val button = holder.robotStarButton
        button.isEnabled = true

        val item = mRobotList[position]

        button.setOnClickListener {
            mPresenter.markRobotAsFavorite(item.UUID)
        }

        holder.itemView.setOnClickListener{
            mPresenter.connectToRobot(item.UUID)
        }

        holder.itemView.setOnLongClickListener(
            {
                TODO("Add option to edit robot information")
            }
        )
    }
}