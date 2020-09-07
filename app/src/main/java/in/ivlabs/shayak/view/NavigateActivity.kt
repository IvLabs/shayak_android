package `in`.ivlabs.shayak.view

//import org.webrtc.VideoRenderer;

//import org.webrtc.VideoRenderer;
//import org.webrtc.VideoRenderer;

import `in`.ivlabs.shayak.navigateactivity.NavigateActivityPresenter
import `in`.ivlabs.shayak.navigateactivity.NavigateActivityViewInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.ivlabs.shayak.R
import com.jackandphantom.joystickview.JoyStickView
import kotlinx.android.synthetic.main.activity_navigate.*
import org.webrtc.*
import org.webrtc.CameraVideoCapturer.CameraEventsHandler
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.lang.StringBuilder
import java.net.Socket
import java.nio.ByteBuffer
import java.nio.charset.Charset


class NavigateActivity : AppCompatActivity(), NavigateActivityViewInterface,
    SeekBar.OnSeekBarChangeListener  {
    private val mPresenter = NavigateActivityPresenter(this)
    private lateinit var mLocalVideoView: SurfaceViewRenderer
    private lateinit var mRemoteVideoView: SurfaceViewRenderer
    private lateinit var mPeerConnectionFactory : PeerConnectionFactory
    private lateinit var mLocalPeer : PeerConnection
    private lateinit var mRemotePeer : PeerConnection
    private lateinit var mLocalVideoTrack : VideoTrack
    private lateinit var mLocalAudioTrack: AudioTrack
    private lateinit var mRemoteAudioTrack: AudioTrack
    private lateinit var stream : MediaStream
    private lateinit var mRemoteVideoTrack : VideoTrack
    private val ServerIP = "192.168.29.27"
    private val ServerPort = 8080
    private lateinit var output: PrintWriter
    private lateinit var input: BufferedReader
    private lateinit var mDataChannel : DataChannel

    private val mRootEglBase = EglBase.create()
    var surfaceTextureHelper: SurfaceTextureHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigate)

        val joyStickView: JoyStickView = findViewById(R.id.joyStickView)
        joyStickView.setOnMoveListener { angle, strength ->
            //mPresenter.updateJoystickInput(angle, strength)
        }

        val robotVolumeButton = findViewById<ImageButton>(R.id.robotVolumeButton)
        robotVolumeButton.setOnClickListener {
            //mPresenter.toggleRobotAudio()
            Thread{
                val socket = Socket(ServerIP, ServerPort)
                output = PrintWriter(socket.getOutputStream());
                input = BufferedReader(InputStreamReader(socket.getInputStream()));
                call()
            }.start()
        }

        val robotVolumeBar = findViewById<SeekBar>(R.id.robotVolumeSeekbar)
        robotVolumeBar.setOnSeekBarChangeListener(this)

        val videoFeedButton = findViewById<ImageButton>(R.id.videoButton)
        videoFeedButton.setOnClickListener {
            mPresenter.toggleTabVideo()
        }

        surfaceTextureHelper =
            SurfaceTextureHelper.create("CaptureThread", mRootEglBase.eglBaseContext)
        mLocalVideoView =
            findViewById<View>(R.id.local_renderer) as SurfaceViewRenderer

        mRemoteVideoView =
            findViewById<View>(R.id.remote_renderer) as SurfaceViewRenderer

        mLocalVideoView.init(mRootEglBase.eglBaseContext, null)
        mRemoteVideoView.init(mRootEglBase.eglBaseContext, null)

        mLocalVideoView.setZOrderMediaOverlay(true)
        mRemoteVideoView.setZOrderMediaOverlay(true)
        mLocalVideoView.setEnableHardwareScaler(true)
        mRemoteVideoView.setEnableHardwareScaler(true)

        startCall()

        mPresenter.onViewCreated()
    }

    private fun startCall()
    {
        val options = PeerConnectionFactory.InitializationOptions
            .builder(applicationContext)
            .setEnableInternalTracer(true)
            .setFieldTrials("WebRTC-IntelVp8Encoder/Enabled/")
            .setFieldTrials("WebRTC-H264HighProfile/Enabled/")
            .createInitializationOptions()
        PeerConnectionFactory.initialize(options)

        val encoderFactory = DefaultVideoEncoderFactory(mRootEglBase.eglBaseContext, false, true)
        val decoderFactory = DefaultVideoDecoderFactory(mRootEglBase.eglBaseContext)
        val builder = PeerConnectionFactory.builder()
            .setVideoEncoderFactory(encoderFactory)
            .setVideoDecoderFactory(decoderFactory)
        builder.setOptions(null)

        mPeerConnectionFactory = builder.createPeerConnectionFactory()
        val videoCapturer = createVideoCapturer(CustomCameraEventsHandler())

        //Create MediaConstraints - Will be useful for specifying video and audio constraints.
        val audioConstraints = MediaConstraints()
        val videoConstraints = MediaConstraints()

        val videoSource = mPeerConnectionFactory.createVideoSource(videoCapturer!!.isScreencast)
        mLocalVideoTrack = mPeerConnectionFactory.createVideoTrack("10230", videoSource)
        videoCapturer.initialize(
            surfaceTextureHelper,
            this,
            videoSource.capturerObserver
        )

        val audioSource = mPeerConnectionFactory.createAudioSource(audioConstraints)
        mLocalAudioTrack = mPeerConnectionFactory.createAudioTrack("13143101", audioSource);

        mLocalVideoView.visibility = View.VISIBLE

        videoCapturer.startCapture(1080, 1584, 3)
        mLocalVideoTrack.addSink(mLocalVideoView)
        mLocalVideoTrack.setEnabled(true)
        mLocalVideoView.setMirror(true)

    }

    private fun call()
    {
        val iceServerList = ArrayList<PeerConnection.IceServer>()

        val sdpConstraints = MediaConstraints()
        sdpConstraints.mandatory.add(MediaConstraints.KeyValuePair("offerToReceiveAudio", "true"))
        sdpConstraints.mandatory.add(MediaConstraints.KeyValuePair("offerToReceiveVideo", "true"))


        //creating localPeer

        //creating localPeer
        mLocalPeer = mPeerConnectionFactory.createPeerConnection(
            iceServerList,
            object : CustomPeerConnectionObserver("LocalPeerObserver") {
                override fun onIceCandidate(iceCandidate: IceCandidate?) {
                    super.onIceCandidate(iceCandidate)
                    onIceCandidateReceived(mLocalPeer, iceCandidate)
                }

                override fun onAddStream(mediaStream: MediaStream?) {
                    super.onAddStream(mediaStream)
                    mRemoteAudioTrack = mediaStream?.audioTracks?.get(0)!!
                    val mRemoteVideoTrack = mediaStream?.videoTracks?.get(0)
                    mRemoteVideoTrack?.setEnabled(true)
                    mRemoteVideoTrack?.addSink(mRemoteVideoView)
                }
            })!!

        stream = mPeerConnectionFactory.createLocalMediaStream("1222")
        stream.addTrack(mLocalVideoTrack)
        stream.addTrack(mLocalAudioTrack)
        mLocalPeer.addStream(stream)

        mLocalPeer.createOffer( object : CustomSpdObserver("localCreateOffer")
        {
            override fun onCreateSuccess(sessionDescription: SessionDescription?) {
                super.onCreateSuccess(sessionDescription)
                mLocalPeer.setLocalDescription(CustomSpdObserver("localSetLocalDesc"), sessionDescription)
                if(sessionDescription?.description != null) {
                    val descLines = sessionDescription?.description?.lines().toString()
                    output.write("$descLines\r\n")
                    output.flush()
                }

                Thread{
                    var done = false;
                    while(!done)
                    {
                        val answer = input.readLine().replace(", ","\r\n").removePrefix("[").removeSuffix("]")
                        if(answer != null)
                        {
                            mLocalPeer.setRemoteDescription(CustomSpdObserver("localSetRemoteDesc"),
                                SessionDescription(SessionDescription.Type.ANSWER, answer))
                            done = true
                        }
                    }

                    while(true)
                    {
                        val spd = input.readLine()
                        val spdMid = input.readLine()
                        val spdMidLineIndex = input.readLine().toInt()
                        val iceCandidate = IceCandidate(spdMid, spdMidLineIndex, spd)
                        mLocalPeer.addIceCandidate(iceCandidate)
                    }

                }.start()
            }
        }, sdpConstraints)


    }

    fun onIceCandidateReceived(
        peer: PeerConnection,
        iceCandidate: IceCandidate?
    ) {
        //we have received ice candidate. We can set it to the other peer.
        //mLocalPeer.addIceCandidate(iceCandidate)
        Log.d ("Ice Candidate", iceCandidate.toString())
        if (iceCandidate != null) {
            output.write(iceCandidate.sdp + "\r\n")
            output.write(iceCandidate.sdpMid + "\r\n")
            output.write((iceCandidate.sdpMLineIndex).toString() + "\r\n")
            output.flush()
        }

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
        if (state)
            videoButton.setImageResource(R.drawable.ic_button_videocam_enabled_24)
        else
            videoButton.setImageResource(R.drawable.ic_button_videocam_disabled_24)
    }

    override fun updateCameraFeed(media: Uri) {

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

    private fun createVideoCapturer(eventsHandler: CameraEventsHandler): VideoCapturer? {

        return createCameraCapturer(Camera2Enumerator(this), eventsHandler)
    }

    private fun createCameraCapturer(enumerator: Camera2Enumerator, eventsHandler: CameraEventsHandler): VideoCapturer? {
        val deviceNames = enumerator.deviceNames

        // First, try to find front facing camera

        for (deviceName in deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                //Logging.d(MainActivity.TAG, "Creating front facing camera capturer.")
                val videoCapturer: VideoCapturer? = enumerator.createCapturer(deviceName, eventsHandler)
                if (videoCapturer != null) {
                    return videoCapturer
                }
            }
        }

        // Front facing camera not found, try something else
        //Logging.d(MainActivity.TAG, "Looking for other cameras.")
        for (deviceName in deviceNames) {
            if (!enumerator.isFrontFacing(deviceName)) {
                //Logging.d(MainActivity.TAG, "Creating other camera capturer.")
                val videoCapturer: VideoCapturer? = enumerator.createCapturer(deviceName, eventsHandler)
                if (videoCapturer != null) {
                    return videoCapturer
                }
            }
        }
        return null
    }

}

