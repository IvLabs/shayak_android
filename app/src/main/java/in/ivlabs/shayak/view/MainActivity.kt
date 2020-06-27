package `in`.ivlabs.shayak.view

import `in`.ivlabs.shayak.tcpServer.serverActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ivlabs.shayak.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent :Intent = Intent(this, serverActivity::class.java)
        startActivity(intent)

    }
}