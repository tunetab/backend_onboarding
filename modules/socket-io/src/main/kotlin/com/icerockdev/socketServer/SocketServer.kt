package com.icerockdev.socketServer

import com.corundumstudio.socketio.AuthorizationListener
import com.corundumstudio.socketio.Configuration
import com.corundumstudio.socketio.SocketConfig
import com.corundumstudio.socketio.SocketIOServer
import com.corundumstudio.socketio.listener.ConnectListener
import com.corundumstudio.socketio.listener.DisconnectListener
import com.icerockdev.socketServer.config.SocketServerConfig
import com.icerockdev.socketServer.handshake.getToken
import com.icerockdev.socketServer.handshake.getUserId
import com.icerockdev.socketServer.handshake.setUserId
import com.icerockdev.socketServer.listener.SocketDataListener
import com.icerockdev.socketServer.user.SocketUser
import java.io.Closeable
import java.io.IOException
import kotlin.reflect.KClass

class SocketServer(
    private val socketServerConfig: SocketServerConfig
) : Closeable {
    private val server: SocketIOServer

    init {
        val config = Configuration()
        config.hostname = socketServerConfig.host
        config.port = socketServerConfig.port

        val socketConfig = SocketConfig()
        socketConfig.isReuseAddress = true
        socketConfig.isTcpKeepAlive = true
        socketConfig.isTcpNoDelay = true
        config.bossThreads = socketServerConfig.bossThreads
        config.workerThreads = socketServerConfig.workerThreads
        config.socketConfig = socketConfig
        config.isRandomSession = true
        config.authorizationListener = getAuthListener()
        config.upgradeTimeout = socketServerConfig.upgradeTimeout
        config.pingTimeout = socketServerConfig.pingTimeout
        config.pingInterval = socketServerConfig.pingInterval

        server = SocketIOServer(config)

        server.addConnectListener(getConnectListener())
        server.addDisconnectListener(getDisconnectListener())
    }

    fun start() {
        server.start()
        socketServerConfig.logger.info(
            "SocketServer started at ${server.configuration.hostname}:${server.configuration.port}"
        )
        socketServerConfig.logger.info("SocketServer DEBUG = ${socketServerConfig.isDebug}")
    }

    fun sendEvent(socketUser: SocketUser, event: String, data: Any): Boolean {
        if (server.getRoomOperations(socketUser.getRoomId()).clients.isNotEmpty()) {
            server.getRoomOperations(socketUser.getRoomId()).sendEvent(event, data)
            return true
        }

        return false
    }

    fun <T : Any> addEventListener(eventName: String, eventClass: KClass<T>, listener: SocketDataListener<T>) {
        server.addEventListener(eventName, eventClass.java, listener)
    }

    @Throws(IOException::class)
    override fun close() {
        server.stop()
        socketServerConfig.logger.info(
            "SocketServer at ${server.configuration.hostname}:${server.configuration.port} stopped"
        )
    }

    private fun getAuthListener(): AuthorizationListener {
        return AuthorizationListener { handshakeData ->
            val token: String? = handshakeData.getToken()
            if (token === null) {
                debug("Empty token")
                return@AuthorizationListener false
            }

            try {
                handshakeData.setUserId(socketUser = socketServerConfig.getSocketUser(token))
            } catch (e: Exception) {
                debug("Validation token error: " + e.message)
                return@AuthorizationListener false
            }

            return@AuthorizationListener true
        }
    }

    private fun getConnectListener(): ConnectListener {
        return ConnectListener { client ->
            val userId: String? = client.handshakeData.getUserId()
            if (userId === null) {
                socketServerConfig.logger.debug("User id empty")
                client.disconnect()
                return@ConnectListener
            }

            client.joinRoom(SocketUser(id = userId).getRoomId())

            debug("connected session: ${client.sessionId} userId: $userId")
        }
    }

    private fun getDisconnectListener(): DisconnectListener {
        return DisconnectListener { client ->
            val userId = client.handshakeData.getUserId()
            if (userId === null) {
                return@DisconnectListener
            }

            debug("disconnect session: ${client.sessionId} userId: $userId")
        }
    }

    private fun debug(message: String) {
        if (!socketServerConfig.isDebug) {
            return
        }

        socketServerConfig.logger.debug(message)
    }
}
