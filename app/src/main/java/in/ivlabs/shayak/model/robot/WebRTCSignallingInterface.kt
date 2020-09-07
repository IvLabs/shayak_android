package `in`.ivlabs.shayak.model.robot

import org.webrtc.IceCandidate
import org.webrtc.SessionDescription

interface WebRTCSignallingTransmitInterface {

    fun sendOffer(sessionDescription: SessionDescription)
    fun sendAnswer(sessionDescription: SessionDescription)
    fun sendICECandidate(iceCandidate: IceCandidate)
}

interface WebRTCSignallingReceiveCallbackInterface {

    fun offerReceive(sessionDescription: SessionDescription)
    fun answerReceive(sessionDescription: SessionDescription)
    fun iceCandidateReceive(iceCandidate: IceCandidate)
}