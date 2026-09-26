package com.icerockdev.application.tests.common.db.initializers

import com.icerockdev.application.tests.common.db.containers.DBContainerSetup
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext

class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
        TestPropertyValues.of(
            "spring.datasource.url=${DBContainerSetup.postgresContainer.getJdbcUrl()}",
            "spring.datasource.username=${DBContainerSetup.postgresContainer.username}",
            "spring.datasource.password=${DBContainerSetup.postgresContainer.password}",
        )
            .applyTo(configurableApplicationContext.environment)
    }
}
