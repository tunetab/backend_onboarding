package com.icerockdev.socketServer.config

import com.icerockdev.socketServer.user.SocketUser
import org.slf4j.Logger

data class SocketServerConfig(
    val host: String,
    val port: Int,
    val logger: Logger,
    val isDebug: Boolean = true,
    val bossThreads: Int = 8,
    val workerThreads: Int = 8,
    val upgradeTimeout: Int = 2000,
    val pingTimeout: Int = 10000,
    val pingInterval: Int = 20000,
    val getSocketUser: (token: String) -> SocketUser
)
