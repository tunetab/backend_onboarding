package com.icerockdev.application.common

object Constants {
    // DB fields
    const val MAX_PHONE = 13
    const val MAX_EMAIL = 50
    const val MAX_PASSWORD = 60 // BCrypt generate string for 59-60 characters
    const val MIN_PASSWORD = 6
    const val MAX_TOKEN = 350

    // PASSWORD RESTORE
    const val TOKEN_EXPIRED_MINUTES = 1440 // 24 hours
    const val CODE_EXPIRED_MINUTES = 5
    const val CODE_SIZE = 6
    const val UNIQUE_TOKEN_GENERATE_MAX_ATTEMPTS = 5

    // Links
    const val RESET_PASSWORD_DEEPLINK = "{appName}://{appName}/auth/restore-password/{token}"

    // Patterns
    const val PHONE_NUMBER = "[0-9]{11}"
    // пароль (спецсимвол, заглавная, цифра, латиница, 8+ символов)
    const val PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,50}$"
    // Ф,И,О в кириллице
    const val CONTACT_NAME_REGEX = "^(?!.*[- ]{2})(?=.*[A-Za-zА-Яа-я])[A-Za-zА-Яа-я][A-Za-zА-Яа-я -]{2,33}$"
    // for s3 keys
    const val IMAGE_KEY = "^[0-9a-f]{8}/[0-9a-f]{4}/[0-9a-f]{4}/[0-9a-f]{4}/[0-9a-f]{12}$"
    // время формата (15:00)
    const val TIME_REGEX = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]\$"
}
