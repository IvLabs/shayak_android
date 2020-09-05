package `in`.ivlabs.shayak.model.robot

import `in`.ivlabs.shayak.FakeRobot.FakeRobot

class RobotRepository()
{
    fun getRobotConnection() : RobotConnectionInterface
    {
        return FakeRobot()
    }

    fun getHealthMonitor() : HealthMonitorInterface
    {
        return FakeRobot()
    }

    fun getRobotDatabase() : RobotDatabaseInterface
    {
        return FakeRobot()
    }

    fun getCommunicationInterface() : CommunicationInterface
    {
        return FakeRobot()
    }
}