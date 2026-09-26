package com.icerockdev.socketServer.user

import com.fasterxml.jackson.annotation.JsonIgnore

class SocketUser(val id: String) {
    @JsonIgnore
    fun getRoomId(): String {
        return "user_room_$id"
    }
}
