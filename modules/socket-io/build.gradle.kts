@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlinSpringPlugin)
    alias(libs.plugins.springDependencyManagementPlugin)
    id("build-logic.lint-convention")
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    // Socket IO
    implementation(libs.socketIO)
    // Detekt
    implementation(libs.detektGradlePlugin)

    // Common
    implementation(projects.common)
}

configurations.compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
}
