import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.the

configurations.create("testArchive")

tasks.register<Jar>("testArchive") {
    archiveBaseName.set(project.name + "-test")
    from(project.the<SourceSetContainer>()["test"].output)
}

artifacts {
    add("testArchive", tasks["testArchive"])
}
