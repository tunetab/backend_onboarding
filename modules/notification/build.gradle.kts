@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlinSpringPlugin)
    alias(libs.plugins.kotlinJpaPlugin)
    alias(libs.plugins.springDependencyManagementPlugin)
    id("build-logic.lint-convention")
    id("build-logic.test-share-classes-convention")
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    annotationProcessor(libs.springBootConfigurationProcessor)

    // Tests
    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.bundles.testLibrary)

    // Notifications
    implementation(libs.icerockEmailService)
    implementation(libs.icerockFcmPushService)
    implementation(libs.icerockSmsService)

    // Detekt
    implementation(libs.detektGradlePlugin)

    // Common
    implementation(projects.common)
}

configurations.compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
}
