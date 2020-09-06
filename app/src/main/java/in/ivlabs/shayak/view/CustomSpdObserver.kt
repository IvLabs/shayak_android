package `in`.ivlabs.shayak.view

import android.util.Log
import org.webrtc.SdpObserver
import org.webrtc.SessionDescription

open class CustomSpdObserver(val tag : String) : SdpObserver {


    override fun onSetFailure(p0: String?) {
        Log.d(
            tag,
            "onSetFailure() called with: sessionDescription = [$p0]"
        )
    }

    override fun onSetSuccess() {
        Log.d(
            tag,
            "onSetSuccess() called"
        )
    }

    override fun onCreateSuccess(p0: SessionDescription?) {
        Log.d(
            tag,
            "onCreateSuccess() called with: sessionDescription = [$p0]"
        )
    }

    override fun onCreateFailure(p0: String?) {
        Log.d(
            tag,
            "onCreateFailure() called with: sessionDescription = [$p0]"
        )
    }
}