package `in`.ivlabs.shayak.view

import android.util.Log
import org.webrtc.*

open class CustomPeerConnectionObserver(val tag : String) : PeerConnection.Observer {


    override fun onIceCandidate(p0: IceCandidate?) {
        Log.d(
            tag,
            "onIceCandidate() called with: IceCandidate = [$p0]"
        )
    }

    override fun onDataChannel(p0: DataChannel?) {
        Log.d(
            tag,
            "onDataChannel() called with: DataChannel = [$p0]"
        )
    }

    override fun onIceConnectionReceivingChange(p0: Boolean) {
        Log.d(
            tag,
            "onIceConnectionReceivingChange() called with: Boolean = [$p0]"
        )
    }

    override fun onIceConnectionChange(p0: PeerConnection.IceConnectionState?) {
        Log.d(
            tag,
            "onIceConnectionChange() called with: IceConnectionState = [$p0]"
        )
    }

    override fun onIceGatheringChange(p0: PeerConnection.IceGatheringState?) {
        Log.d(
            tag,
            "onIceGatheringChange() called with: IceGatheringState = [$p0]"
        )
    }

    override fun onAddStream(p0: MediaStream?) {
        Log.d(
            tag,
            "onAddStream() called with: MediaStream = [$p0]"
        )
    }

    override fun onSignalingChange(p0: PeerConnection.SignalingState?) {
        Log.d(
            tag,
            "onSignalingChange() called with: SignalingState = [$p0]"
        )
    }

    override fun onIceCandidatesRemoved(p0: Array<out IceCandidate>?) {
        Log.d(
            tag,
            "onIceCandidatesRemoved() called with: IceCandidates = [$p0]"
        )
    }

    override fun onRemoveStream(p0: MediaStream?) {
        Log.d(
            tag,
            "onRemoveStream() called with: MediaStream = [$p0]"
        )
    }

    override fun onRenegotiationNeeded() {
        Log.d(
            tag,
            "onRenegotiationNeeded() called"
        )
    }

    override fun onAddTrack(p0: RtpReceiver?, p1: Array<out MediaStream>?) {
        Log.d(
            tag,
            "onRemoveStream() called with: MediaStream = [$p1], RtpReciever = [$p1]"
        )
    }
}