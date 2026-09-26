package com.icerockdev.socketServer.handshake

import com.corundumstudio.socketio.HandshakeData
import com.icerockdev.socketServer.user.SocketUser

internal fun HandshakeData.getToken(): String? {
    return urlParams["token"]?.get(0)
}

internal fun HandshakeData.setUserId(socketUser: SocketUser) {
    urlParams["uid"] = listOf(socketUser.id)
}

internal fun HandshakeData.getUserId(): String? {
    return urlParams["uid"]?.get(0)
}
