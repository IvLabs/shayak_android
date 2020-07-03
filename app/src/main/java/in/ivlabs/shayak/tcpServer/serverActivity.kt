package `in`.ivlabs.shayak.tcpServer

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.felhr.usbserial.UsbSerialDevice
import com.felhr.usbserial.UsbSerialInterface
import com.ivlabs.shayak.R
import java.net.ServerSocket
import java.util.*


class serverActivity : AppCompatActivity() {

    public final var ACTION_USB_PERMISSION :String = "confoosball.lmu.mff.confoosball.USB_PERMISSION"
    var usbManager: UsbManager? = null
    var device: UsbDevice? = null
    var serialPort: UsbSerialDevice? = null
    var connection: UsbDeviceConnection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server)
        openConnection()

    }

    public fun startListening(view: View){
        val myThread = Thread{
            val server = ServerSocket(6000)
            Log.i("server","running of port 6000")
            var st = true
            while (st){
                try{
                    val socket = server.accept()
                    val scanner = Scanner(socket.getInputStream())
                    while(st){
                        val text = scanner.nextLine()
                        Log.i("serverMsg",text)
                        serialPort?.write(text.toByteArray())
                        if(text == "EXIT"){
                            st = false
                        }
                    }

                }catch (ex :Exception){
                    ex.printStackTrace()
                }


            }
            server.close()
        }
        myThread.start()
    }

    fun openConnection() {
        val usbDevices : HashMap<String, UsbDevice> = usbManager!!.deviceList
        if (usbDevices.isNotEmpty()) {
            var keep = true
            for ((_, value) in usbDevices) {
                device = value
                val deviceVID = device!!.vendorId
                if (deviceVID == 0x2341) //Arduino Vendor ID
                {
                    val pi = PendingIntent.getBroadcast(
                        this, 0,
                        Intent(ACTION_USB_PERMISSION), 0
                    )
                    usbManager!!.requestPermission(device, pi)
                    keep = false
                } else {
                    connection = null
                    device = null
                }
                if (!keep) break
            }
        }
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        //Broadcast Receiver to automatically start and stop the Serial connection.
        override fun onReceive(context: Context?, intent: Intent) {
            if (intent.action == ACTION_USB_PERMISSION) {
                val granted =
                    intent.extras!!.getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED)
                if (granted) {
                    connection = usbManager!!.openDevice(device)
                    serialPort = UsbSerialDevice.createUsbSerialDevice(device, connection)
                    if (serialPort != null) {
                        if (serialPort!!.open()) { //Set Serial Connection Parameters.
                            //Enable Buttons in UI
                            serialPort!!.setBaudRate(9600)
                            serialPort!!.setDataBits(UsbSerialInterface.DATA_BITS_8)
                            serialPort!!.setStopBits(UsbSerialInterface.STOP_BITS_1)
                            serialPort!!.setParity(UsbSerialInterface.PARITY_NONE)
                            serialPort!!.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF)
                            //serialPort!!.read(mCallback) //
                            //tvAppend(textView, "Serial Connection Opened!\n")
                        } else {
                            Log.d("SERIAL", "PORT NOT OPEN")
                        }
                    } else {
                        Log.d("SERIAL", "PORT IS NULL")
                    }
                } else {
                    Log.d("SERIAL", "PERM NOT GRANTED")
                }
            }
        }
    }


}