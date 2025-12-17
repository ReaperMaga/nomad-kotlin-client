package dev.reapermaga.nomad.jobs.dsl

import dev.reapermaga.nomad.jobs.data.*
import java.util.*

class NomadJobDsl {

    var id: String = UUID.randomUUID().toString()
    var datacenters = listOf<String>()
    private var taskGroups: List<NomadJobTaskGroupDsl> = listOf()

    fun groups(groups: List<NomadJobTaskGroupDsl>) {
        this.taskGroups = groups
    }

    fun group(init: NomadJobTaskGroupDsl.() -> Unit) {
        val group = NomadJobTaskGroupDsl().apply(init)
        this.taskGroups += group
    }

    fun build(): NomadCreateJobRequest {
        val job = NomadCreateJob(
            id = this.id,
            datacenters = this.datacenters,
            taskGroups = this.taskGroups.map { group ->
                NomadCreateJobTaskGroup(
                    name = group.name,
                    tasks = group.tasks.map { task ->
                        NomadCreateJobTask(
                            name = task.name,
                            driver = task.driver,
                            config = task.config,
                            resources = task.resources?.let { res ->
                                NomadJobTaskResources(
                                    cpu = res.cpu,
                                    memory = res.memory,
                                )
                            },
                        )
                    },
                )
            }
        )
        return NomadCreateJobRequest(job)
    }

}

class NomadJobTaskGroupDsl {
    var name: String = "default"
    var tasks = listOf<NomadJobTaskDsl>()

    fun tasks(tasks: List<NomadJobTaskDsl>) {
        this.tasks = tasks
    }

    fun task(dsl: NomadJobTaskDsl.() -> Unit) {
        val task = NomadJobTaskDsl().apply(dsl)
        this.tasks += task
    }
}

class NomadJobTaskDsl {
    var name: String = "default-task"
    var driver: String = "docker"
    var config: Map<String, Any> = mapOf()

    var resources: NomadJobTaskResourcesDsl? = null

    fun config(vararg pairs: Pair<String, Any>) {
        this.config = mapOf(*pairs)
    }

    fun resources(dsl: NomadJobTaskResourcesDsl.() -> Unit) {
        this.resources = NomadJobTaskResourcesDsl().apply(dsl)
    }

    fun docker(dsl: NomadJobTaskDockerDsl.() -> Unit) {
        driver = "docker"
        val dockerConfig = NomadJobTaskDockerDsl().apply(dsl)
        config(
            "image" to (dockerConfig.image ?: error("Docker image must be specified"))
        )
    }
}

class NomadJobTaskResourcesDsl {
    var cpu: Int? = null
    var memory: Int? = null
}

class NomadJobTaskDockerDsl {
    var image: String? = null

}