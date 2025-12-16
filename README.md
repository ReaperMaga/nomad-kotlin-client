# nomad-kotlin-client

A small, typed Kotlin (JVM) client for HashiCorp Nomad. It wraps Nomad's HTTP API using OkHttp and kotlinx.serialization and provides a simple DSL for common operations like listing/reading jobs, creating jobs, and inspecting nodes.

- Minimal, coroutine-friendly API (suspend functions)
- Job builder DSL (tasks, docker config, resources)
- Basic endpoints implemented: jobs, nodes, status leader
- Ships with a Dokka Javadoc artifact

## Install

This library is published to the Averix Maven repository: `https://repo.averix.tech/repository/maven-releases/`.

### Gradle (Kotlin DSL)

Add the repository and dependency:

```kotlin
// build.gradle.kts
repositories {
    mavenCentral()
    maven {
        name = "Averix"
        url = uri("https://repo.averix.tech/repository/maven-releases/")
    }
}

// build.gradle.kts
dependencies {
    implementation("dev.reapermaga:nomad-kotlin-client:0.1.0")
}
```

### Gradle (Groovy)

```groovy
// settings.gradle or build.gradle
repositories {
    mavenCentral()
    maven {
        name = 'Averix'
        url = uri('https://repo.averix.tech/repository/maven-releases/')
    }
}

dependencies {
    implementation 'dev.reapermaga:nomad-kotlin-client:0.1.0'
}
```

### Maven

Add the repository and dependency to your `pom.xml`:

```xml
<repositories>
  <repository>
    <id>Averix</id>
    <url>https://repo.averix.tech/repository/maven-releases/</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>dev.reapermaga</groupId>
    <artifactId>nomad-kotlin-client</artifactId>
    <version>LATEST_VERSION</version>
  </dependency>
</dependencies>
```

## Quick start

```kotlin
suspend fun main() {
    val client = dev.reapermaga.nomad.NomadClient {
        address = "http://localhost:4646"   // Nomad HTTP API base
        // token = "..."                     // optional ACL token if needed
        // clientVersion = NomadClientVersion.V1
    }

    // Check cluster leader
    println(client.statusLeader())

    // List jobs
    val jobs = client.jobs.list()
    println("jobs = $jobs")

    // Create a simple job using the DSL
    val createResp = client.jobs.create {
        id = "example-job"
        datacenters = listOf("dc1")
        group {
            name = "example-group"
            task {
                name = "redis"
                resources {
                    cpu = 2000
                    memory = 512
                }
                docker {
                    image = "redis:latest"
                }
            }
        }
    }
    println("evalId = ${createResp.evalID}")
}
```

See `src/test/kotlin/.../NomadClientTest.kt` for more examples: reading a job, polling allocations, listing nodes, etc.

## Documentation

- API docs are published as a Javadoc (Dokka HTML) artifact alongside the library.
- To generate docs locally: run `./gradlew dokkaHtml` and open `build/dokka/html/index.html`.

## Notes

- Coroutines: public APIs are `suspend`. Your application should include `org.jetbrains.kotlinx:kotlinx-coroutines-core` to launch coroutines.
- Serialization: uses `kotlinx-serialization-json` with `ignoreUnknownKeys = true`.
- HTTP: uses OkHttp 5.x. Network calls will throw if the response is not successful (non-2xx) except for 404 on certain read endpoints, which return `null`.

## Versioning

This library is currently pre-1.0. Expect minor breaking changes as APIs evolve. Latest version at the time of writing: `0.1.0`.

## License

TBD (add your project license here).
