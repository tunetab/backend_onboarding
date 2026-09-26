package com.icerockdev.tests.util

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

fun sleep(timeout: Long, unit: TimeUnit = TimeUnit.MILLISECONDS) {
    CountDownLatch(1).await(timeout, unit)
}
