package `in`.ivlabs.shayak.view

import `in`.ivlabs.shayak.navigateactivity.NavigateActivityPresenter
import `in`.ivlabs.shayak.navigateactivity.NavigateActivityViewInterface
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.SurfaceView
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.ivlabs.shayak.R
import com.jackandphantom.joystickview.JoyStickView
import kotlinx.android.synthetic.main.activity_navigate.*
import org.videolan.libvlc.IVLCVout
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer


class NavigateActivity : AppCompatActivity(), NavigateActivityViewInterface,
    SeekBar.OnSeekBarChangeListener, MediaPlayer.EventListener, IVLCVout.Callback
{
    private val mPresenter = NavigateActivityPresenter(this)
    private lateinit var mMediaPlayer : MediaPlayer
    private lateinit var mVLC: LibVLC

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
        videoFeedButton.setOnClickListener {
            mPresenter.toggleTabVideo()
        }

        val surfaceView = findViewById<SurfaceView>(R.id.surfaceView)
        val surfaceHolder = surfaceView.holder
        surfaceHolder.setKeepScreenOn(true)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val videoParams: ViewGroup.LayoutParams = surfaceView.layoutParams
        videoParams.width = displayMetrics.widthPixels
        videoParams.height = displayMetrics.heightPixels

        val vlcArgs = ArrayList<String>()
        vlcArgs.add("-vvv");
        vlcArgs.add("--network-caching=1500");
        vlcArgs.add("--aout=opensles");
        mVLC = LibVLC(applicationContext, vlcArgs)
        mMediaPlayer = MediaPlayer(mVLC)
        mMediaPlayer.setEventListener(this)

        val vout = mMediaPlayer.vlcVout
        vout.setVideoView(surfaceView);
        vout.setWindowSize(videoParams.width,videoParams.height);
        vout.addCallback(this)
        vout.attachViews();

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

    override fun updateCameraFeed(media : Uri) {
        val m = Media(mVLC, media)
        mMediaPlayer.media = m;
        mMediaPlayer.play();
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

    override fun onEvent(event: MediaPlayer.Event?) {
        //Do Nothing
    }

    override fun onSurfacesCreated(vlcVout: IVLCVout?) {
        //Do Nothing
    }

    override fun onSurfacesDestroyed(vlcVout: IVLCVout?) {
        //Do Nothing
    }
}

