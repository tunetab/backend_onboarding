rootProject.name = "GRADLE_PROJECT_NAME"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

includeBuild("build-logic")
include(":application")
include(":modules:notification")
include(":modules:socket-io")
include(":common")
include(":modules:file")
include("tests")
