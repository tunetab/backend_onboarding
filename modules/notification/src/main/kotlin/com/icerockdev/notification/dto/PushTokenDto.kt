package com.icerockdev.notification.dto

import java.util.UUID

internal class PushTokenDto(
    var id: UUID = UUID.randomUUID(),
    var token: String,
    var entityId: UUID
)
