import io.gitlab.arturbosch.detekt.Detekt

internal val libs = libs()

plugins {
    id("io.gitlab.arturbosch.detekt") apply false
}

dependencies {
    detekt(libs.detektCli)
}

detekt {
    config.from(files("${project.rootDir}/build-logic/src/main/resources/detekt/detekt.yml"))
}

tasks.withType<Detekt>().configureEach {
    reports {
        html {
            required.set(true)
            val directory = rootProject.layout.buildDirectory.dir("reports/detekt/${rootProject.name}/detekt.html")
            outputLocation.set(directory.get().asFile)
        }
        xml.required.set(false)
        txt.required.set(false)
        sarif.required.set(false)
        md.required.set(false)
    }
}
