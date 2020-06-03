package com.ivlabs.shayak.robot

interface StateMonitorInterface {
    fun getState(wheelVelocities: WheelVelocities) : RobotState
    fun updateWheelVelocities(wheelVelocities: WheelVelocities)
}