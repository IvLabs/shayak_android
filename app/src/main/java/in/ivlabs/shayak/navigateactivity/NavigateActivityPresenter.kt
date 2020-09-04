package `in`.ivlabs.shayak.navigateactivity

class NavigateActivityPresenter (view: NavigateActivityViewInterface) : NavigateActivityPresenterInterface {

    private var mView: NavigateActivityViewInterface? = view

    override fun onViewCreated() {
        mView?.updateCameraState(false)
        mView?.updateRobotAudioLevel(10)
    }

    override fun updateJoystickInput(angle: Double, strength: Float) {
        TODO("Not yet implemented")
    }

    override fun setRobotAudioLevel(level: Float) {
        TODO("Not yet implemented")
    }

    override fun toggleTabVideo() {
        TODO("Not yet implemented")
    }

    override fun toggleRobotAudio() {
        TODO("Not yet implemented")
    }
}