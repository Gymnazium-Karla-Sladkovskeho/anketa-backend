plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
    // Loads ../.env into the environment of run/test tasks, so application.yaml's
    // "$VAR" substitution and docker-compose share one source of truth: the .env
    // file in the repository root. (Real environment variables still work.)
    id("io.github.dsdolzhenko.dotenv") version "0.1.0"
}

dotenv {
    files.set(listOf("../.env"))
}

group = "cz.cvut.fit"
version = "1.0.0-SNAPSHOT"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    // Ktor server
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.netty)
    implementation(ktorLibs.server.config.yaml)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.serialization.kotlinx.json)
    implementation(ktorLibs.server.di)
    implementation(ktorLibs.server.statusPages)
    implementation(ktorLibs.server.auth)
    implementation(ktorLibs.server.auth.jwt)
    implementation(ktorLibs.server.swagger)
    implementation(ktorLibs.server.routingOpenapi)
    implementation("de.mkammerer:argon2-jvm:2.12")

    // Database: Exposed (DAO/ORM API) + PostgreSQL
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.postgresql)
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:1.5.0")

    // Logging
    implementation(libs.logback.classic)

    // Tests
    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
}
