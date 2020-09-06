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
import java.util.stream.Stream


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
    private lateinit var stream : MediaStream
    private lateinit var mRemoteVideoTrack : VideoTrack


    private val mRootEglBase = EglBase.create()
    var surfaceTextureHelper: SurfaceTextureHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigate)

        val joyStickView: JoyStickView = findViewById(R.id.joyStickView)
        joyStickView.setOnMoveListener { angle, strength ->
            mPresenter.updateJoystickInput(angle, strength)
        }

        val robotVolumeButton = findViewById<ImageButton>(R.id.robotVolumeButton)
        robotVolumeButton.setOnClickListener {
            call()
            //mPresenter.toggleRobotAudio()
        }

        val robotVolumeBar = findViewById<SeekBar>(R.id.robotVolumeSeekbar)
        robotVolumeBar.setOnSeekBarChangeListener(this)

        val videoFeedButton = findViewById<ImageButton>(R.id.videoButton)
        videoFeedButton.setOnClickListener {
            mPresenter.toggleTabVideo()
        }

//        //Initialize PeerConnectionFactory globals.
//        //Params are context, initAudio,initVideo and videoCodecHwAcceleration
//        //PeerConnectionFactory.initializeAndroidGlobals(this, true, true, true);
//        PeerConnectionFactory.initialize(
//            PeerConnectionFactory.InitializationOptions.builder(this).createInitializationOptions()
//        )
//
//        //Create a new PeerConnectionFactory instance.
//        //PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
//        val peerConnectionFactory =
//            PeerConnectionFactory.builder().createPeerConnectionFactory()
//
//
//        //Now create a VideoCapturer instance. Callback methods are there if you want to do something! Duh!
//        val videoCapturerAndroid = createVideoCapturer(CustomCameraEventsHandler())
//        //Create MediaConstraints - Will be useful for specifying video and audio constraints. More on this later!
//        val constraints = MediaConstraints()
//

        surfaceTextureHelper =
            SurfaceTextureHelper.create("CaptureThread", mRootEglBase.eglBaseContext)
//        val videoSource =
//            peerConnectionFactory.createVideoSource(videoCapturerAndroid!!.isScreencast)
//        videoCapturerAndroid.initialize(
//            surfaceTextureHelper,
//            this,
//            videoSource.capturerObserver
//        )
//        val localVideoTrack = peerConnectionFactory.createVideoTrack("100", videoSource)
//        //create an AudioSource instance
//
//        //create an AudioSource instance
//        val audioSource =
//            peerConnectionFactory.createAudioSource(constraints)
//        val localAudioTrack =
//            peerConnectionFactory.createAudioTrack("101", audioSource)
//
//        //we will start capturing the video from the camera
//        //width,height and fps
//        //create surface renderer, init it and add the renderer to the track
        mLocalVideoView =
            findViewById<View>(R.id.local_renderer) as SurfaceViewRenderer

        mRemoteVideoView =
            findViewById<View>(R.id.remote_renderer) as SurfaceViewRenderer
//
        mLocalVideoView.init(mRootEglBase.eglBaseContext, null)
        mRemoteVideoView.init(mRootEglBase.eglBaseContext, null)

        mLocalVideoView.setZOrderMediaOverlay(true)
        mRemoteVideoView.setZOrderMediaOverlay(true)
//        mLocalVideoView.visibility = View.VISIBLE
//
//        //we will start capturing the video from the camera
//        //width,height and fps
//        videoCapturerAndroid.startCapture(1080, 1584, 3)
//
//        localVideoTrack.addSink(mLocalVideoView)
//        mLocalVideoView.setMirror(true)

        startCall()

        mPresenter.onViewCreated()
    }

    private fun startCall()
    {
        val options = PeerConnectionFactory.InitializationOptions
            .builder(applicationContext).setEnableInternalTracer(true).createInitializationOptions()
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
        mLocalVideoTrack = mPeerConnectionFactory.createVideoTrack("100", videoSource)
        videoCapturer.initialize(
            surfaceTextureHelper,
            this,
            videoSource.capturerObserver
        )

        val audioSource = mPeerConnectionFactory.createAudioSource(audioConstraints)
        mLocalAudioTrack = mPeerConnectionFactory.createAudioTrack("101", audioSource);

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
            })!!



        //creating remotePeer

        //creating remotePeer
        mRemotePeer = mPeerConnectionFactory.createPeerConnection(
            iceServerList,
            object : CustomPeerConnectionObserver("RemotePeerObserver") {
                override fun onIceCandidate(iceCandidate: IceCandidate?) {
                    super.onIceCandidate(iceCandidate)
                    onIceCandidateReceived(mRemotePeer, iceCandidate)
                }

                override fun onAddTrack(rtpReceiver: RtpReceiver?, p1: Array<out MediaStream>?) {
                    super.onAddTrack(rtpReceiver, p1)
                    val track = rtpReceiver?.track()
                    if (track is VideoTrack) {
                        mRemoteVideoTrack = track as VideoTrack
                        mRemoteVideoTrack.addSink(mRemoteVideoView)
                    }
                }
            })!!

        stream = mPeerConnectionFactory.createLocalMediaStream("1222")
        mLocalPeer.addTrack(mLocalVideoTrack)
        //stream.addTrack(mLocalAudioTrack)
        //mLocalPeer.addStream(stream)

        mLocalPeer.createOffer( object : CustomSpdObserver("localCreateOffer")
        {
            override fun onCreateSuccess(sessionDescription: SessionDescription?) {
                super.onCreateSuccess(sessionDescription)
                if (sessionDescription != null) {
                    Log.d("SessionDescriptionToString", sessionDescription.description)
                }
                mLocalPeer.setLocalDescription(CustomSpdObserver("localSetLocalDesc"), sessionDescription)
                mRemotePeer.setRemoteDescription(CustomSpdObserver("remoteSetRemoteDesc"), sessionDescription)
                mRemotePeer.createAnswer(object : CustomSpdObserver("remoteCreateOffer"){
                    override fun onCreateSuccess(sessionDescription: SessionDescription?)
                    {
                        super.onCreateSuccess(sessionDescription)
                        mRemotePeer.setLocalDescription(CustomSpdObserver("remoteSetLocalDesc"), sessionDescription)
                        mLocalPeer.setRemoteDescription(CustomSpdObserver("localSetRemoteDesc"), sessionDescription)
                    }
                }, sdpConstraints)
            }
        }, sdpConstraints)


    }

    fun onIceCandidateReceived(
        peer: PeerConnection,
        iceCandidate: IceCandidate?
    ) {
        //we have received ice candidate. We can set it to the other peer.
        if (peer === mLocalPeer) {
            mRemotePeer.addIceCandidate(iceCandidate)
        } else {
            mLocalPeer.addIceCandidate(iceCandidate)
        }
    }

    private fun gotRemoteStream(stream: MediaStream) {
        //we have remote video stream. add to the renderer.
        mRemoteVideoTrack = stream.videoTracks[0]
        mRemoteVideoTrack.setEnabled(true)
        //val audioTrack: AudioTrack = stream.audioTracks[0]
        runOnUiThread {
            try {
//                remoteRenderer = VideoRenderer(remoteVideoView)
                mRemoteVideoView.setVisibility(View.VISIBLE)
//                videoTrack.addRenderer(remoteRenderer)
                mRemoteVideoTrack.addSink(mRemoteVideoView)
            } catch (e: Exception) {
                e.printStackTrace()
            }
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

