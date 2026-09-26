rootProject.name = "back_onboarding"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

includeBuild("build-logic")
include(":application")
include(":modules:notification")
include(":modules:socket-io")
include(":common")
include(":modules:file")
include("tests")
