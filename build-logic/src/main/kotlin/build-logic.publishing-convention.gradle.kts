import java.net.URI

plugins {
    `maven-publish`
}

publishing {
    publications {
        create<MavenPublication>("default") {
            from(components["kotlin"])
            pom {
                description.set(findProperty("mavenProjectDescription") as String)
                url.set(findProperty("mavenProjectDocumentationUrl") as String)
            }
        }
    }
    repositories {
        maven {
            isAllowInsecureProtocol = true
            url = URI(findProperty("mavenHost") as String)
            credentials {
                username = findProperty("mavenUser") as String
                password = findProperty("mavenPass") as String
            }
        }
    }
}
