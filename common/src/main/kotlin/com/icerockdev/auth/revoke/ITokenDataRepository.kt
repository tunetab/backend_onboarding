/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.auth.revoke

interface ITokenDataRepository<TUserKey: Any> {
    fun getAllNotExpired(): Map<TUserKey, Long>
    fun insertOrUpdate(key: TUserKey, revokeAt: Long): Boolean
    fun cleanUp()
}
