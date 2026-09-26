package com.icerockdev.util

import java.util.Optional

fun <T : Any> Optional<T>.toNullable(): T? = if (this.isPresent) this.get() else null
