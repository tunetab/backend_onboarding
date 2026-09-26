package com.icerockdev.application.tests.common.db.annotations

import com.icerockdev.application.tests.common.db.containers.DBContainerSetup
import com.icerockdev.application.tests.common.db.initializers.Initializer
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@SpringBootTest
@ExtendWith(DBContainerSetup::class)
@ContextConfiguration(initializers = [Initializer::class])
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
annotation class SpringBootTestWithDB
