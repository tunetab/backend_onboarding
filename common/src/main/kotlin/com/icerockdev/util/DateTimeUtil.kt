package com.icerockdev.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object DateTimeUtil {
    // formatters

    val DEFAULT_DATE_TIME_FORMATTER: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    val ISO_DATE_TIME_FORMATTER: DateTimeFormatter =
        DateTimeFormatter.ISO_LOCAL_DATE_TIME

    // now utils

    fun now(): LocalDateTime = LocalDateTime.now()

    fun getTimestamp(): Long = Instant.now().epochSecond

    fun getTimestampMillis(): Long = Instant.now().toEpochMilli()

    // format utils

    fun String.parseToISODate(): LocalDateTime = LocalDateTime.parse(this)

    fun String.toLocalDateTime(
        formatter: DateTimeFormatter = ISO_DATE_TIME_FORMATTER
    ): LocalDateTime = LocalDateTime.parse(this, formatter)

    fun LocalDateTime.formatDateTime(
        formatter: DateTimeFormatter = DEFAULT_DATE_TIME_FORMATTER
    ): String = this.format(formatter)

    fun LocalDateTime.atStartOfDay(): LocalDateTime = this.toLocalDate().atStartOfDay()

    fun LocalDateTime.atEndOfDay(): LocalDateTime = this.toLocalDate().atTime(LocalTime.MAX)

    fun LocalDateTime.between(
        start: LocalDateTime,
        end: LocalDateTime
    ): Boolean = this.isAfter(start) && this.isBefore(end)

    fun LocalDateTime.betweenOrEqual(
        start: LocalDateTime,
        end: LocalDateTime
    ): Boolean = !this.isBefore(start) && !this.isAfter(end)

    fun checkDateRange(
        fromISODate: LocalDateTime,
        toISODate: LocalDateTime
    ): Boolean {
        val now = LocalDateTime.now()

        val notInPast = fromISODate.isAfter(now) ||
                fromISODate.isEqual(now) ||
                toISODate.isAfter(now)

        val correctOrder = fromISODate.isBefore(toISODate)

        return notInPast && correctOrder
    }
}
