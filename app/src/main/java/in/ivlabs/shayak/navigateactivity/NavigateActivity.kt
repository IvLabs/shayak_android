package `in`.ivlabs.shayak.navigateactivity

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.ivlabs.shayak.R
import kotlinx.android.synthetic.main.activity_navigate.*

class Navigate : AppCompatActivity(), NavigateActivityPresenterInterface,
    NavigateActivityViewInterface {
    var muteStatus = 0
    var oldLevel = 0
    var minV = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigate)

        volumeSeekBar.max = getRobotMaxVol()
        volumeSeekBar.progress = getRobotCurrentVol()

        volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                // write custom code for progress is changed
                updateRobotAudioLevel(progress.toFloat())

            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped

            }
        })

        muteButton.setOnClickListener {
            if (muteStatus == 0) {

                oldLevel = volumeSeekBar.progress
                updateRobotAudioLevel(1.0f)
                muteStatus = 1
                volumeSeekBar.progress = 0
            } else {
                updateRobotAudioLevel(oldLevel.toFloat())
                muteStatus = 0
                volumeSeekBar.progress = oldLevel
            }
        }

        EndCallButton.setOnClickListener {
            toggleTabVideo()
        }

        moveForwardButton.setOnClickListener { updateJoystickInput(0.0f, 1.0f) }
        moveBackwardButton.setOnClickListener { updateJoystickInput(0.0f, -1.0f) }
        leftTurnButton.setOnClickListener { updateJoystickInput(-1.0f, 0.0f) }
        rightTurnButton.setOnClickListener { updateJoystickInput(1.0f, 0.0f) }


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

    override fun getRobotMaxVol(): Int {
        //dummy function for now
        val audioManager: AudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        return audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)
    }

    override fun getRobotCurrentVol(): Int {
        val audioManager: AudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        return audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL)
    }


    override fun updateRobotAudioLevel(level: Float) {
        //implement it, for now it control mobile audio
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, level.toInt(), 1)
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