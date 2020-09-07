package com.ivlabs.shayak

import `in`.ivlabs.shayak.view.CustomCameraEventsHandler
import `in`.ivlabs.shayak.view.CustomPeerConnectionObserver
import `in`.ivlabs.shayak.view.CustomSpdObserver
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import org.webrtc.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.InetAddress
import java.net.ServerSocket
import java.nio.ByteBuffer
import java.nio.ByteOrder


class RobotRunningActivity : AppCompatActivity() {

    private lateinit var mLocalVideoView: SurfaceViewRenderer
    private lateinit var mRemoteVideoView: SurfaceViewRenderer
    private lateinit var mQRCodeImageView : ImageView
    private lateinit var mPeerConnectionFactory : PeerConnectionFactory
    private lateinit var mLocalPeer : PeerConnection
    private lateinit var mRemotePeer : PeerConnection
    private lateinit var mLocalVideoTrack : VideoTrack
    private lateinit var mRemoteAudioTrack: AudioTrack
    private lateinit var mLocalAudioTrack: AudioTrack
    private lateinit var stream : MediaStream
    private lateinit var mRemoteVideoTrack : VideoTrack
    private lateinit var mThread1 : Thread
    private val mRootEglBase = EglBase.create()
    private lateinit var sdpConstraints : MediaConstraints
    var surfaceTextureHelper: SurfaceTextureHelper? = null
    private lateinit var mLocalIP : String
    private lateinit var mServerSocket: ServerSocket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_robot_running)

        surfaceTextureHelper =
            SurfaceTextureHelper.create("CaptureThread", mRootEglBase.eglBaseContext)

        mLocalVideoView =
            findViewById<View>(R.id.local_renderer) as SurfaceViewRenderer

        mRemoteVideoView =
            findViewById<View>(R.id.remote_renderer) as SurfaceViewRenderer

        mQRCodeImageView =
            findViewById<ImageView>(R.id.imageView3)
//
        mLocalVideoView.init(mRootEglBase.eglBaseContext, null)
        mRemoteVideoView.init(mRootEglBase.eglBaseContext, null)

        mLocalVideoView.setZOrderMediaOverlay(true)
        mRemoteVideoView.setZOrderMediaOverlay(true)
        mLocalVideoView.setEnableHardwareScaler(true)
        mRemoteVideoView.setEnableHardwareScaler(true)

        mLocalIP = getLocalIP()
        val writer = MultiFormatWriter()
        try {
            val bitMatrix: BitMatrix =
                writer.encode(mLocalIP, BarcodeFormat.QR_CODE, 200, 200)
            val bitmap = BarcodeEncoder().createBitmap(bitMatrix)
            mQRCodeImageView.setImageBitmap(bitmap)
        } catch (e : WriterException) {
            e.printStackTrace();
        }

        mThread1 = ServerConnectionThread() as Thread
        mThread1.start()

        startCall()
        call()
    }

    private fun getLocalIP(): String {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        var ipInt = wifiInfo.ipAddress
        return InetAddress.getByAddress(
            ByteBuffer.allocate(4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(ipInt).array()).hostAddress
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
        mLocalVideoTrack = mPeerConnectionFactory.createVideoTrack("100", videoSource)
        videoCapturer.initialize(
            surfaceTextureHelper,
            this,
            videoSource.capturerObserver
        )

        val audioSource = mPeerConnectionFactory.createAudioSource(audioConstraints)
        mLocalAudioTrack = mPeerConnectionFactory.createAudioTrack("101", audioSource);

        mLocalVideoView.visibility = View.INVISIBLE
        mRemoteVideoView.visibility  = View.INVISIBLE

        videoCapturer.startCapture(1080, 1584, 3)
        mLocalVideoTrack.addSink(mLocalVideoView)
        mLocalVideoTrack.setEnabled(true)
        mLocalVideoView.setMirror(true)

    }

    private fun call()
    {
        val iceServerList = ArrayList<PeerConnection.IceServer>()

        sdpConstraints = MediaConstraints()
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
                    mRemoteVideoTrack = mediaStream?.videoTracks?.get(0)!!
                    mRemoteVideoTrack?.setEnabled(true)
                    mRemoteVideoTrack?.addSink(mRemoteVideoView)
                    runOnUiThread {
                        mQRCodeImageView.visibility = View.INVISIBLE
                        mLocalVideoView.visibility = View.VISIBLE
                        mRemoteVideoView.visibility  = View.VISIBLE
                    }
                }
            })!!

        stream = mPeerConnectionFactory.createLocalMediaStream("122")
        stream.addTrack(mLocalVideoTrack)
        stream.addTrack(mLocalAudioTrack)
        mLocalPeer.addStream(stream)
    }

    fun onIceCandidateReceived(
        peer: PeerConnection,
        iceCandidate: IceCandidate?
    ) {
        Log.d ("Ice Candidate", iceCandidate.toString())
        if (iceCandidate != null) {
            output.write(iceCandidate.sdp + "\r\n")
            output.write(iceCandidate.sdpMid + "\r\n")
            output.write(iceCandidate.sdpMLineIndex.toString() + "\r\n")
            output.flush()
        }
    }

    private fun createVideoCapturer(eventsHandler: CameraVideoCapturer.CameraEventsHandler): VideoCapturer? {

        return createCameraCapturer(Camera2Enumerator(this), eventsHandler)
    }

    private fun createCameraCapturer(enumerator: Camera2Enumerator, eventsHandler: CameraVideoCapturer.CameraEventsHandler): VideoCapturer? {
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
    private lateinit var output: PrintWriter
    private lateinit var input: BufferedReader
    inner class ServerConnectionThread : Thread() {
        override fun run() {
            try {
                val port = 8080
                mServerSocket = ServerSocket(port)
                Log.d("Server", "Not connected; IP [$mLocalIP]:[$port]")
                try{
                    val socket = mServerSocket.accept();
                    output = PrintWriter(socket.getOutputStream())
                    input = BufferedReader(InputStreamReader(socket.getInputStream()))
                    Log.d("Server", "Connected")

                }catch (e : IOException)
                {
                    Log.d("Server","Could not accreadLineept")
                }
            }catch (e : IOException)
            {
                Log.d("Server","Could not create")
            }
            while(true) {
                val message = input.readLine().replace(", ","\r\n").removePrefix("[").removeSuffix("]")
                Log.d("Session Rcvd", "$message")
                mLocalPeer.setRemoteDescription(
                    CustomSpdObserver("LocalRemoteDesc"),
                    SessionDescription(SessionDescription.Type.OFFER, message)
                )
                mLocalPeer.createAnswer(object : CustomSpdObserver("Answer") {
                    override fun onCreateSuccess(sessionDescription: SessionDescription?) {
                        super.onCreateSuccess(sessionDescription)
                        mLocalPeer.setLocalDescription(
                            CustomSpdObserver("LocalLocalDesc"),
                            sessionDescription
                        )
                        if (sessionDescription != null) {
                            val lines = sessionDescription.description.lines().toString()
                            output.write("$lines\r\n")
                            output.flush()
                        }
                    }
                }, sdpConstraints)

                while(true)
                {
                    val spd = input.readLine()
                    val spdMid = input.readLine()
                    val spdMidLineIndex = input.readLine().toInt()
                    val iceCandidate = IceCandidate(spdMid, spdMidLineIndex, spd)
                    mLocalPeer.addIceCandidate(iceCandidate)
                }
            }
        }

    }



}