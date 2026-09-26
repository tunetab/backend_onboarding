package com.icerockdev.validation.annotation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.annotation.AnnotationTarget.ANNOTATION_CLASS
import kotlin.annotation.AnnotationTarget.CONSTRUCTOR
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.TYPE_PARAMETER
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER
import org.apache.commons.validator.routines.EmailValidator
import kotlin.reflect.KClass
import org.hibernate.validator.internal.constraintvalidators.AbstractEmailValidator

/**
 * Annotation to validate email
 *
 * Example, validate field
 * @field:StrictEmail(message = "Invalid email")
 */
@Constraint(validatedBy = [StrictEmailValidator::class])
@Target(allowedTargets = [FUNCTION, FIELD, ANNOTATION_CLASS, CONSTRUCTOR, VALUE_PARAMETER, TYPE_PARAMETER])
@Retention(AnnotationRetention.RUNTIME)
annotation class StrictEmail(
    val message: String = "{javax.validation.constraints.Email.message}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)

class StrictEmailValidator : AbstractEmailValidator<StrictEmail?>() {
    private val validator: EmailValidator = EmailValidator.getInstance(false)

    override fun isValid(value: CharSequence?, context: ConstraintValidatorContext): Boolean {
        value ?: return true

        val isValid = super.isValid(value, context)

        return isValid && validator.isValid(value.toString())
    }
}
