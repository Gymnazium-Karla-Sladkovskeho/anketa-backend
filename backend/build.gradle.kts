plugins {
    kotlin("jvm") version "2.4.20"
    kotlin("plugin.serialization") version "2.4.20"
    id("io.ktor.plugin") version "3.6.0"
}

group = "cz.cvut.fit.gykas"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    // Ktor server (versions come from the io.ktor plugin's BOM)
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-config-yaml")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")

    // Exposed ORM (JDBC transport) + H2 embedded database
    implementation("org.jetbrains.exposed:exposed-core:1.5.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:1.5.1")
    implementation("com.h2database:h2:2.3.232")

    // Logging (Ktor uses SLF4J; logback is the backend)
    implementation("ch.qos.logback:logback-classic:1.5.18")

    // Tests
    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation(kotlin("test"))
}

application {
    // Entry point provided by Ktor's Netty EngineMain; modules are loaded from application.yaml
    mainClass.set("io.ktor.server.netty.EngineMain")
}

kotlin {
    jvmToolchain(21)
}
