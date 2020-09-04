package `in`.ivlabs.shayak.view

import `in`.ivlabs.shayak.navigateactivity.NavigateActivityPresenter
import `in`.ivlabs.shayak.navigateactivity.NavigateActivityViewInterface
import android.os.Bundle
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.ivlabs.shayak.R
import com.jackandphantom.joystickview.JoyStickView
import kotlinx.android.synthetic.main.activity_navigate.*


class NavigateActivity : AppCompatActivity(), NavigateActivityViewInterface, SeekBar.OnSeekBarChangeListener {

    private val mPresenter = NavigateActivityPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigate)

        val joyStickView: JoyStickView = findViewById(R.id.joyStickView)
        joyStickView.setOnMoveListener { angle, strength ->
            mPresenter.updateJoystickInput(angle, strength)
        }

        val robotVolumeButton = findViewById<ImageButton>(R.id.robotVolumeButton)
        robotVolumeButton.setOnClickListener{
            mPresenter.toggleRobotAudio()
        }

        val robotVolumeBar = findViewById<SeekBar>(R.id.robotVolumeSeekbar)
        robotVolumeBar.setOnSeekBarChangeListener(this)

        val videoFeedButton = findViewById<ImageButton>(R.id.videoButton)
        videoFeedButton.setOnClickListener{
            mPresenter.toggleTabVideo()
        }

        val videoFeed = findViewById<VideoView>(R.id.videoView)
        videoFeed.setVideoPath("rtsp://192.168.29.64:5554/playlist.m3u")
        videoFeed.start()
        mPresenter.onViewCreated()
    }

    override fun updateRobotAudioLevel(level: Int) {
        robotVolumeSeekbar.progress = level as Int
    }

    override fun updateBatteryCharge(level: Float) {
        TODO("Not yet implemented")
    }

    override fun updateConnectionStrength(level: Float) {
        TODO("Not yet implemented")
    }

    override fun updateCameraState(state: Boolean) {
        if(state)
            videoButton.setImageResource(R.drawable.ic_button_videocam_enabled_24)
        else
            videoButton.setImageResource(R.drawable.ic_button_videocam_disabled_24)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        //Do Nothing
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        //Do Nothing
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        mPresenter.setRobotAudioLevel(seekBar?.progress as Float)
    }
}