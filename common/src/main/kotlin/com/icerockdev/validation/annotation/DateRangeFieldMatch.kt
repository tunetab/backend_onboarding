package com.icerockdev.validation.annotation

import com.icerockdev.i18n.I18N
import com.icerockdev.util.DateTimeUtil
import com.icerockdev.util.DateTimeUtil.parseToISODate
import org.apache.commons.beanutils.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

/**
 * Validation annotation to validate that arrival dates are shorter than departure dates
 *
 * Example, compare to dates
 * @DateRange(message = LK.placementDateRange, fromDate = "arrivalDate", toDate = "departureDate")
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [DateRangeValidator::class])
@MustBeDocumented
annotation class DateRange(
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
    val message: String = "{message.key}",
    val fromDate: String,
    val toDate: String
)

class DateRangeValidator : ConstraintValidator<DateRange, Any> {
    @Autowired
    private lateinit var i18N: I18N

    private var fromDate: String? = null
    private var toDate: String? = null
    private var message: String? = null

    override fun initialize(constraintAnnotation: DateRange) {
        fromDate = constraintAnnotation.fromDate
        toDate = constraintAnnotation.toDate
        message = i18N.t(constraintAnnotation.message)
    }

    @Suppress("KotlinConstantConditions", "ComplexCondition")
    override fun isValid(value: Any, constraintContext: ConstraintValidatorContext?): Boolean {
        val valid = try {
            val firstObj = BeanUtils.getProperty(value, fromDate)
            val secondObj = BeanUtils.getProperty(value, toDate)

            if (firstObj == null && secondObj == null) true
            else if ((firstObj == null && secondObj != null) || (firstObj != null && secondObj == null)) false
            else {
                val fromISODate = firstObj.parseToISODate()
                val toISODate = secondObj.parseToISODate()

                DateTimeUtil.checkDateRange(
                    fromISODate = fromISODate,
                    toISODate = toISODate
                )
            }

        } catch (ignore: Exception) {
            throw ignore
        }

        if (!valid) {
            constraintContext?.buildConstraintViolationWithTemplate(message)
                ?.addPropertyNode(fromDate)
                ?.addConstraintViolation()
                ?.disableDefaultConstraintViolation()
        }

        return valid
    }
}
