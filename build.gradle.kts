plugins {
    kotlin("jvm") version "2.3.0-RC"
    kotlin("plugin.serialization") version "2.3.0-RC"
    id("io.kotest") version "6.0.7"
}

group = "dev.reapermaga"
version = "0.1.0"

repositories {
    mavenCentral()
}

val kotestVersion = "6.0.7"

dependencies {
    implementation("com.squareup.okhttp3:okhttp:5.3.0")
    implementation("com.squareup.okhttp3:okhttp-coroutines:5.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    testImplementation("io.kotest:kotest-framework-engine:$kotestVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation(kotlin("test"))

}

tasks.test {
    useJUnitPlatform()
}