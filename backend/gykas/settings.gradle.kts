plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
rootProject.name = "gykas"
include("domain")
include("infrastructure")
include("application")
include("shared")
include("web")