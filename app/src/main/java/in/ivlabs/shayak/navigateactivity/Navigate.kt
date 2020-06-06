package `in`.ivlabs.shayak.navigateactivity

import android.content.Context
import android.media.AudioManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.SeekBar
import android.widget.Toast
import com.ivlabs.shayak.R
import kotlinx.android.synthetic.main.activity_navigate.*
import java.util.*

class Navigate : AppCompatActivity() , NavigateActivityPresenterInterface, NavigateActivityViewInterface{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigate)
    }

    override fun updateJoystickInput(xAxisLocation: Float, yAxisLocation: Float) {
        TODO("Not yet implemented")
    }

    override fun setRobotAudioLevel(level: Float) {

        // implemented changing call volume on cell phone, dont know how to do it on robot tablet
        val audioManager : AudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)
        var currentVol = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL)
        volumeSeekBar.max = maxVol
        volumeSeekBar.progress = currentVol

        volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, i,0)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something

            }
        })



    }

    override fun toggleTabVideo() {
        TODO("Not yet implemented")
    }

    override fun updateRobotAudioLevel(level: Float) {
        TODO("Not yet implemented")
    }

    override fun updateBatteryCharge(level: Float) {
        TODO("Not yet implemented")
    }

    override fun updateConnectionStrength(level: Float) {
        TODO("Not yet implemented")
    }

    override fun updateCameraState(state: Boolean) {
        TODO("Not yet implemented")
    }
}