package `in`.ivlabs.shayak.model.robot

import `in`.ivlabs.shayak.mainactivity.MainActivityPresenter
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.os.StrictMode
import android.widget.Toast
import androidx.annotation.RequiresApi
import org.json.JSONObject
import java.io.DataOutputStream
import java.net.Socket


class RobotConnection(private val presenter: MainActivityPresenter) : RobotConnectionInterface {
    val ssid = "TP-LINK_D3B6"
    val pass = "39806437"

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun connect(UUID: String): Boolean {
        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        Toast.makeText(presenter.activity, "In Connect", Toast.LENGTH_LONG).show()
        /*val specifier = WifiNetworkSpecifier.Builder()
            .setSsidPattern(PatternMatcher("TP-LINK_D3B6", PatternMatcher.PATTERN_PREFIX))
            .setWpa2Passphrase("39806437")
            .build()

        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .setNetworkSpecifier(specifier)
            .build()

        val connectivityManager =
            presenter.activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network?) {
                Toast.makeText(presenter.activity, "CONNECTED", Toast.LENGTH_LONG).show()

            }

            override fun onUnavailable() {
                Toast.makeText(presenter.activity, "UNAVAILABLE", Toast.LENGTH_LONG).show()
            }
        }
        connectivityManager.requestNetwork(request, networkCallback)
// Release the request when done.
        connectivityManager.unregisterNetworkCallback(networkCallback)*/
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

        connectivityManager?.requestNetwork(networkRequest, ConnectivityManager.NetworkCallback())

        val json = JSONObject()
        json.put("test_message", "Hello World!")
        val s = Socket("192.168.1.105", 6969)
        val dos = DataOutputStream(s.getOutputStream())
        dos.writeUTF("Hello World!")
        dos.close()
        s.close()
        return true
    }

    override fun disconnect() {
        TODO("Not yet implemented")
    }

    override fun sendRequest(msg: String): String {
        TODO("Not yet implemented")
    }

    override fun isAlive(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getConnectionStrength(): Float {
        TODO("Not yet implemented")
    }


}