/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.validation.annotation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.ResolverStyle
import kotlin.reflect.KClass

/**
 * Validation annotation to validate date field that 2 fields has match pattern.
 *
 * Example, compare date by format:
 * @DateFormat(pattern = "dd-MM-yyyy")
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [DateFormatValidator::class])
@MustBeDocumented
annotation class DateFormat(
    val message: String = "{message.key}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
    val pattern: String
)

class DateFormatValidator : ConstraintValidator<DateFormat, String> {
    private lateinit var formatter: DateTimeFormatter

    override fun initialize(constraintAnnotation: DateFormat) {
        formatter = DateTimeFormatter
            .ofPattern(constraintAnnotation.pattern)
            .withResolverStyle(ResolverStyle.STRICT)
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) return true

        return try {
            formatter.parse(value)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }
}
