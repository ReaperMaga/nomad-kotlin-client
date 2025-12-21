package dev.reapermaga.nomad.jobs.dsl

class NomadJobTaskDsl {
    var name: String = "default-task"
    var driver: String = "docker"
    var config: MutableMap<String, Any> = mutableMapOf()

    var environment: Map<String, String> = mapOf()

    var resources: NomadJobTaskResourcesDsl? = null

    fun config(vararg pairs: Pair<String, Any>) {
        for (pair in pairs) {
            this.config[pair.first] = pair.second
        }
    }

    fun resources(dsl: NomadJobTaskResourcesDsl.() -> Unit) {
        this.resources = NomadJobTaskResourcesDsl().apply(dsl)
    }

    fun docker(dsl: NomadJobTaskDockerDsl.() -> Unit) {
        driver = "docker"
        val dockerConfig = NomadJobTaskDockerDsl().apply(dsl)
        config(
            "image" to (dockerConfig.image ?: error("Docker image must be specified")),
            "ports" to dockerConfig.ports,
        )
        dockerConfig.networkMode?.let {
            config("network_mode" to it)
        }
    }
}

class NomadJobTaskDockerDsl {
    var image: String? = null

    var ports = listOf<String>()

    var networkMode: String? = null
}