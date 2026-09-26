import co.uzzu.dotenv.gradle.DotEnvRoot

internal val libs = libs()

plugins {
    id("org.liquibase.gradle") apply false
}

dependencies {
    liquibaseRuntime(libs.liquibaseCore)
    liquibaseRuntime(libs.postgresql)
    liquibaseRuntime(libs.picocli)
    liquibaseRuntime(libs.snakeyaml)
}

// find all projects resource dir with db migrations
val resourcesDir = rootProject.allprojects
    .filter { File("${it.projectDir}/src/main/resources/db").exists() }
    .joinToString(":") { "${it.projectDir}/src/main/resources" }

liquibase {
    val env = parent?.extensions?.get("env") as DotEnvRoot
    val dataBaseHost = env.allVariables.get("DATABASE_HOST")
    val dataBasePort = env.allVariables.get("DATABASE_PORT")
    val dataBaseName = env.allVariables.get("DATABASE_NAME")
    val dataBaseUser = env.allVariables.get("DATABASE_USER")
    val dataBasePass = env.allVariables.get("DATABASE_PASS")

    activities {
        register("main") {
            arguments = mapOf(
                "logLevel" to "info",
                "changelogFile" to "db/changelog/db.changelog-master.yaml",
                "url" to "jdbc:postgresql://$dataBaseHost:$dataBasePort/$dataBaseName",
                "username" to dataBaseUser,
                "password" to dataBasePass,
                "classpath" to resourcesDir,
                "driver" to "org.postgresql.Driver"
            )
        }
    }
}

tasks {
    register("liquibaseNewChangeLog") {
        group = "liquibase"
        doLast {
            val path = "$projectDir/src/main/resources"
            val unixTime = System.currentTimeMillis() / 1000
            val fileName = project.findProperty("liquibaseCommandValue")
                ?: throw IllegalArgumentException("No value passed to liquibaseCommandValue")
            val inputFile = file("$path/db/changelog/db.changelog-template.yaml")
            val outputFile = file("$path/db/changelog/changes/db.changelog-$unixTime-$fileName.yaml")
            outputFile.writeText(inputFile.readText().replace("{changelog-id}", fileName.toString()))
        }
    }
}
