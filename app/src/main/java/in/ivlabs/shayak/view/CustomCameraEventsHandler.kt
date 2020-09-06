package `in`.ivlabs.shayak.view

import org.webrtc.CameraVideoCapturer.CameraEventsHandler

class CustomCameraEventsHandler : CameraEventsHandler {
    override fun onCameraError(p0: String?) {
        TODO("Not yet implemented: Error")
    }

    override fun onCameraOpening(p0: String?) {
        //TODO("Not yet implemented")
    }

    override fun onCameraDisconnected() {
        //TODO("Not yet implemented")
    }

    override fun onCameraFreezed(p0: String?) {
       // TODO("Not yet implemented")
    }

    override fun onFirstFrameAvailable() {
        //TODO("Not yet implemented")
    }

    override fun onCameraClosed() {
        //TODO("Not yet implemented")
    }
}