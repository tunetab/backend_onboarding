@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlinSpringPlugin)
    alias(libs.plugins.kotlinJpaPlugin)
    alias(libs.plugins.springBootPlugin)
    alias(libs.plugins.springDependencyManagementPlugin)
    alias(libs.plugins.kotlinGradleAllOpenPlugin)
    alias(libs.plugins.kotlinGradleNoArgPlugin)
    id("build-logic.lint-convention")
    id("build-logic.migration-convention")
    id("build-logic.test-share-classes-convention")
}

allOpen {
    annotations("jakarta.persistence.Entity")
}

noArg {
    annotations("jakarta.persistence.Entity")
}

group = "com.icerockdev"
version = "0.1.0"


dependencies {
    annotationProcessor(libs.springBootConfigurationProcessor)

    // Tests
    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.bundles.testLibrary)

    // Liquibase
    implementation(libs.liquibaseCore)

    // Common
    implementation(projects.common)

    // Springdoc Web Mvc Ui
    implementation(libs.springdocWebMvcUi)
}

configurations.compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
}

tasks {
    bootJar {
        archiveFileName.set("app.jar")
        destinationDirectory.set(file("${project.rootDir}/build"))
    }
}
