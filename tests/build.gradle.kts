import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.openApiGeneratorPlugin)
    id("build-logic.test-convention")
    id("build-logic.openapi-generator-convention")
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    // Tests
    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.bundles.testLibrary)
    testImplementation(libs.bundles.testLibraryRuntimeOnly)

    // Application
    testImplementation(projects.application)
    testImplementation(project(projects.application.dependencyProject.path, "testArchive"))

    // Notification module
    testImplementation(projects.modules.notification)
    testImplementation(project(projects.modules.notification.dependencyProject.path, "testArchive"))

    // File module
    testImplementation(projects.modules.file)
    testImplementation(project(projects.modules.file.dependencyProject.path, "testArchive"))

    // Common
    testImplementation(projects.common)
}

kotlin.sourceSets.getByName("test") {
    val directory = layout.buildDirectory.dir("generate-resources/main/src/main/kotlin/org/openapitools/client/models")
    kotlin.srcDir(directory.get().asFile)
}

tasks.test {
    dependsOn(tasks.withType(GenerateTask::class))
}

tasks.compileTestKotlin {
    dependsOn(tasks.withType(GenerateTask::class))
}

