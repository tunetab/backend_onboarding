plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    // Add access to version catalog in convention plugins
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.detektGradlePlugin)
    implementation(libs.liquibaseGradlePlugin)
    implementation(libs.dotenvPlugin)
    implementation(libs.openApiGeneratorPlugin)
    implementation(libs.jacksonModuleKotlin)
    implementation(libs.mergeOpenApiGradlePlugin)
}
