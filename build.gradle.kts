import org.jetbrains.kotlin.gradle.plugin.extraProperties

plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.serialization") version "2.3.0-RC"
    id("io.kotest") version "6.0.7"
    id("org.jetbrains.dokka") version "2.1.0"
    `maven-publish`
}

group = "dev.reapermaga"
version = extraProperties["global.version"]!! as String

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


java {
    withSourcesJar()
}

dokka { }

tasks.register<Jar>("dokkaHtmlJar") {
    dependsOn(tasks.dokkaGenerateHtml)
    from(layout.buildDirectory.dir("dokka/html"))
    archiveClassifier.set("javadoc")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            // Use the default Maven artifact coordinates (group, name, version).
            from(components["java"])
            artifact(tasks["dokkaHtmlJar"])

            groupId = project.group.toString()
            artifactId = project.name

            version = findProperty("build.version")?.toString()
                ?: System.getenv("BUILD_VERSION")
                        ?: extraProperties["global.version"]!! as String
        }
    }

    repositories {
        maven {
            name = "Averix"
            url = uri("https://repo.averix.tech/repository/maven-releases/")
            credentials {
                username = project.findProperty("averix.user")?.toString() ?: System.getenv("AVERIX_USER")
                password = project.findProperty("averix.key")?.toString() ?: System.getenv("AVERIX_KEY")
            }
        }
    }
}