package com.icerockdev.schedule

import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface Scheduler {
    fun schedule()

    companion object {
        val logger: Logger = LoggerFactory.getLogger(Scheduler::class.java)
    }
}
