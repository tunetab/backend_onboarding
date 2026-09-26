package com.icerockdev.validation.annotation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass
import org.apache.commons.beanutils.BeanUtils

/**
 * Validation annotation to validate that 2 fields have the same value.
 * An array of fields and their matching confirmation fields can be supplied.
 *
 * Example, compare 1 pair of fields:
 * @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")
 *
 * Example, compare more than 1 pair of fields:
 * @FieldMatch.List([
 *      @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match"),
 *      @FieldMatch(first = "email", second = "confirmEmail", message = "The email fields must match")})
 * ])
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [FieldMatchValidator::class])
@MustBeDocumented
annotation class FieldMatch(
    val message: String = "{constraints.fieldmatch}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
    /**
     * @return The first field
     */
    val first: String,
    /**
     * @return The second field
     */
    val second: String
) {
    /**
     * Defines several `@FieldMatch` annotations on the same element
     *
     * @see FieldMatch
     */
    @Target(AnnotationTarget.CLASS, AnnotationTarget.FILE, AnnotationTarget.ANNOTATION_CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    @MustBeDocumented
    annotation class List(val value: Array<FieldMatch>)
}

class FieldMatchValidator : ConstraintValidator<FieldMatch, Any> {
    private var firstFieldName: String? = null
    private var secondFieldName: String? = null
    private var message: String? = null

    override fun initialize(constraintAnnotation: FieldMatch) {
        firstFieldName = constraintAnnotation.first
        secondFieldName = constraintAnnotation.second
        message = constraintAnnotation.message
    }

    override fun isValid(value: Any, constraintContext: ConstraintValidatorContext?): Boolean {
        val valid = try {
            val firstObj = BeanUtils.getProperty(value, firstFieldName)
            val secondObj = BeanUtils.getProperty(value, secondFieldName)

            firstObj == null && secondObj == null || firstObj != null && firstObj == secondObj
        } catch (ignore: Exception) {
            throw ignore
        }

        if (!valid) {
            constraintContext?.buildConstraintViolationWithTemplate(message)
                ?.addPropertyNode(firstFieldName)
                ?.addConstraintViolation()
                ?.disableDefaultConstraintViolation()
        }

        return valid
    }
}
