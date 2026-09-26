import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef

internal val libs = libs()
internal val projectBuildDir = project.layout.buildDirectory.get().asFile.path

plugins {
    id("org.openapi.generator") apply false
    id("com.rameshkp.openapi-merger-gradle-plugin") apply false
}

dependencies {
    libs.jacksonModuleKotlin
}

internal fun getOpenApiMergeSettings(): MergeSettings? {
    val objectMapper = jacksonObjectMapper()

    val mergeSettingsJson = File("$rootDir/docs/openapi/openapi-merge-settings.json").readText()

    return objectMapper.readValue(mergeSettingsJson, jacksonTypeRef())
}

@JsonIgnoreProperties(ignoreUnknown = true)
internal data class MergeSettings(
    val inputs: List<InputFile>
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    internal data class InputFile(
        val inputFile: String
    )
}

getOpenApiMergeSettings()?.inputs?.forEachIndexed { index, inputFile ->
    val destinationFile = File("$projectBuildDir/generate-resources/openapi/${inputFile.inputFile}")
    destinationFile.parentFile.mkdirs()

    File("$rootDir/${inputFile.inputFile}")
        .copyTo(destinationFile, overwrite = true)
}

openApiGenerate {
    inputSpec.set("$projectBuildDir/generate-resources/openapi-merged/openapi.yaml")
    generatorName.set("kotlin")
    library.set("jvm-ktor")
    generateModelTests.set(false)
    generateModelDocumentation.set(false)
    generateApiTests.set(false)
    generateApiDocumentation.set(false)
    additionalProperties.set(
        mapOf(
            "serializationLibrary" to "jackson",
            "dateLibrary" to "string"
        )
    )
}

openApiMerger {
    inputDirectory.set(file("$projectBuildDir/generate-resources/openapi"))
    output {
        directory.set(file("$projectBuildDir/generate-resources/openapi-merged"))
        fileName.set("openapi")
        fileExtension.set("yaml")
    }

    openApi {
        openApiVersion.set("3.0.3")
        info {
            title.set("Open API Merged file")
            version.set("1.0.0")
        }
    }
}

tasks.openApiGenerate {
    dependsOn(tasks.getByName("mergeOpenApiFiles"))
}
