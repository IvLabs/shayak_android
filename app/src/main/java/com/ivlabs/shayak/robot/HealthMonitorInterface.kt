package com.ivlabs.shayak.robot

interface HealthMonitorInterface {
    fun isHealthy()
    fun getErrorCode() : ErrorCode
}