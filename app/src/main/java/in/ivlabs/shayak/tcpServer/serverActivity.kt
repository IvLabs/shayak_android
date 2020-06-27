package `in`.ivlabs.shayak.tcpServer

import `in`.ivlabs.shayak.view.MainActivity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.ivlabs.shayak.R
import kotlinx.android.synthetic.main.activity_server.*
import java.lang.Exception
import java.net.ServerSocket
import java.util.*

class serverActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server)
        val list = mutableListOf<String>()
        val myAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        ShowMsg.adapter=myAdapter;


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



}