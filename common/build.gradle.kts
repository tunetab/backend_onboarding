@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlinSpringPlugin)
    alias(libs.plugins.springDependencyManagementPlugin)
    alias(libs.plugins.kotlinGradleAllOpenPlugin)
    alias(libs.plugins.kotlinGradleNoArgPlugin)
    id("build-logic.lint-convention")
}

allOpen {
    annotations("jakarta.persistence.Entity")
}

noArg {
    annotations("jakarta.persistence.Entity")
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    // Kotlin
    api(libs.kotlinReflect)

    // Spring Boot
    api(libs.springBootStarterWeb)
    api(libs.springBootStarterValidation)
    api(libs.springBootStarterDataJpa)
    api(libs.springBootStarterSecurity)
    api(libs.springBootStarterAop)

    // Postgres
    api(libs.postgresql)

    // JPA JsonBinaryType
    api(libs.hibernateType)

    // Coroutines
    implementation(libs.kotlinCoroutinesCore)

    // Detekt
    implementation(libs.detektGradlePlugin)

    // Libs
    implementation(libs.apacheBeanUtils)
    api(libs.apacheCommonsValidator)

    // Jackson
    api(libs.jacksonModuleKotlin)

    // JWT
    api(libs.jsonWebTokenApi)
    api(libs.jsonWebTokenImpl)
    api(libs.jsonWebTokenJackson)

    // Logging
    api(libs.logbackGelf)

    // Javax
    api(libs.javaxActivation)
}
