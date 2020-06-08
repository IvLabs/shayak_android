package `in`.ivlabs.shayak.navigateactivity

import `in`.ivlabs.shayak.view.MainActivity
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ivlabs.shayak.R
import kotlinx.android.synthetic.main.activity_navigate.*
import java.util.*

class Navigate : AppCompatActivity() , NavigateActivityPresenterInterface, NavigateActivityViewInterface{
    var muteStatus = 0
    var oldLevel=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigate)
       //mute the call
        muteButton.setOnClickListener{
            muteCall()
        }
        //end the call
        EndCallButton.setOnClickListener{
            toggleTabVideo()
            val intent: Intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        val audioManager : AudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)
        val currentVol = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL)
        volumeSeekBar.max = maxVol
        volumeSeekBar.progress = currentVol
        volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, i,0)
                setRobotAudioLevel(i.toFloat())

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something

            }
        })


    }



    private fun muteCall(){
        //unComment updateRobotAudioLevel after implemenating it
        if (muteStatus == 0){
            oldLevel = volumeSeekBar.progress
            muteButton.setImageResource(android.R.drawable.presence_audio_online)
            setRobotAudioLevel(0.0f)
            volumeSeekBar.progress = 0
            muteStatus=1
        }
        else{
            if(volumeSeekBar.progress > 0){
                oldLevel = volumeSeekBar.progress
            }
            muteButton.setImageResource(android.R.drawable.stat_notify_call_mute)
            volumeSeekBar.progress=oldLevel
            setRobotAudioLevel(oldLevel.toFloat())
            muteStatus = 0
        }
    }

    override fun updateJoystickInput(xAxisLocation: Float, yAxisLocation: Float) {
        TODO("Not yet implemented")
    }

    override fun setRobotAudioLevel(level: Float) {

        // implemented changing call volume on cell phone, dont know how to do it on robot tablet
        /*
            Have to change this to get robots max and current volume, for now both mobile and robot volume will update with same value
         */
        updateRobotAudioLevel(level)


    }

    override fun toggleTabVideo() {
        //("Not yet implemented")
    }

    override fun updateRobotAudioLevel(level: Float) {
        //implement it
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