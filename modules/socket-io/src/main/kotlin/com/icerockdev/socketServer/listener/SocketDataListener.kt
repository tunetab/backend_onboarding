package com.icerockdev.socketServer.listener

import com.corundumstudio.socketio.AckRequest
import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.listener.DataListener
import com.icerockdev.api.response.AbstractResponse
import com.icerockdev.api.response.ErrorResponse
import com.icerockdev.exception.ErrorDetail
import com.icerockdev.exception.UserException
import com.icerockdev.socketServer.handshake.getUserId
import com.icerockdev.socketServer.user.SocketUser
import org.springframework.http.HttpStatus

class SocketDataListener<T>(val callback: (data: T, socketUser: SocketUser) -> AbstractResponse?) : DataListener<T> {
    override fun onData(client: SocketIOClient, data: T, ackSender: AckRequest) {
        try {
            val userId = client.handshakeData.getUserId()
            if (userId === null) {
                client.disconnect()
                return
            }

            callback(data, SocketUser(id = userId))?.also {
                ackSender.sendAckData(it)
            }
        } catch (e: UserException) {
            ackSender.sendAckData(e.getErrorResponse())
        } catch (e: Throwable) {
            ackSender.sendAckData(
                ErrorResponse().also {
                    ErrorDetail(
                        message = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase
                    )
                }
            )
        }
    }
}
