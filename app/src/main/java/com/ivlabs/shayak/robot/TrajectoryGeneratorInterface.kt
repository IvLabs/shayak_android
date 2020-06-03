package com.ivlabs.shayak.robot

interface TrajectoryGeneratorInterface {

    fun setTarget(speed : Float, turningRadius : Float)
    fun getWheelVelocity() : WheelVelocities


}