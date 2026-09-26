package com.icerockdev.application.tests.application.i18n

import com.icerockdev.application.tests.common.db.annotations.SpringBootTestWithDB
import com.icerockdev.i18n.I18N
import com.icerockdev.i18n.LK
import java.util.Locale
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@SpringBootTestWithDB
class I18NTests {
    @Autowired
    private lateinit var i18N: I18N

    @Test
    fun localizationTest() {
        assertEquals(expected = "User not found", i18N.t(LK.userNotFound, locale = Locale.US))
        assertEquals(expected = "Пользователь не найден", i18N.t(LK.userNotFound, locale = Locale.forLanguageTag("ru")))
    }
}
