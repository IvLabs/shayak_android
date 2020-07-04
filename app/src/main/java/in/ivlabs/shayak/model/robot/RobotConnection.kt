package `in`.ivlabs.shayak.model.robot

import `in`.ivlabs.shayak.mainactivity.MainActivityPresenter
import `in`.ivlabs.shayak.storage.RobotStorageInterface
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.os.StrictMode
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.io.DataOutputStream
import java.net.Socket


class RobotConnection(private val presenter: MainActivityPresenter) : RobotConnectionInterface {

    private var dataOutputStream: DataOutputStream? = null

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun connect(robot: RobotStorageInterface.RobotData): Boolean {
        val wifiDetails = getWifiNetworkDetails(robot)
        val ssid = wifiDetails.first
        val pass = wifiDetails.second
        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        Toast.makeText(presenter.activity, "In Connect", Toast.LENGTH_LONG).show()
        val wifiNetworkSpecifier = WifiNetworkSpecifier.Builder()
            .setSsid(ssid)
            .setWpa2Passphrase(pass)
            .build()

        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .setNetworkSpecifier(wifiNetworkSpecifier)
            .build()

        val connectivityManager =
            presenter.activity.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        connectivityManager?.requestNetwork(networkRequest, object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                connectivityManager.bindProcessToNetwork(network)
                Toast.makeText(presenter.activity, "Sending Shizz", Toast.LENGTH_SHORT).show()
                val s = Socket("192.168.1.105", 12345)
                dataOutputStream = DataOutputStream(s.getOutputStream())
                presenter.connectedCallback(robot)
            }
        })
        return true
    }

    override fun disconnect() {
        TODO("Not yet implemented")
    }

    override fun sendMessage(msg: String): String {
        dataOutputStream?.writeUTF(msg)
        dataOutputStream?.flush()
        return msg
    }

    override fun isAlive(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getConnectionStrength(): Float {
        TODO("Not yet implemented")
    }

    /**
     * Get Wifi Network Details for a specific Robot. Returns Dummy Data now
     *
     * @param UUID: String UUID of robot.
     * @return
     */
    private fun getWifiNetworkDetails(robot: RobotStorageInterface.RobotData): Pair<String, String> {
        val ssid = "TP-LINK_D3B6"
        val pass = "39806437"
        return Pair(ssid, pass)
    }
}