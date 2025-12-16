package dev.reapermaga.nomad.jobs

import dev.reapermaga.nomad.jobs.data.NomadCreateJobRequest
import dev.reapermaga.nomad.jobs.data.NomadCreateJobTask
import dev.reapermaga.nomad.jobs.data.NomadCreateJobTaskGroup
import dev.reapermaga.nomad.jobs.data.NomadJobTaskResources
import java.util.*

class NomadJobBuilder {

    var id: String = UUID.randomUUID().toString()
    var datacenters = listOf<String>()
    private var taskGroups: List<NomadJobBuilderTaskGroup> = listOf()

    fun groups(groups: List<NomadJobBuilderTaskGroup>) {
        this.taskGroups = groups
    }

    fun group(init: NomadJobBuilderTaskGroup.() -> Unit) {
        val group = NomadJobBuilderTaskGroup().apply(init)
        this.taskGroups += group
    }

    fun build(): NomadCreateJobRequest {
        val job = dev.reapermaga.nomad.jobs.data.NomadCreateJob(
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

class NomadJobBuilderTaskGroup {
    var name: String = "default"
    var tasks = listOf<NomadJobBuilderTask>()

    fun tasks(tasks: List<NomadJobBuilderTask>) {
        this.tasks = tasks
    }

    fun task(init: NomadJobBuilderTask.() -> Unit) {
        val task = NomadJobBuilderTask().apply(init)
        this.tasks += task
    }
}

class NomadJobBuilderTask {
    var name: String = "default-task"
    var driver: String = "docker"
    var config: Map<String, Any> = mapOf()

    var resources: NomadJobBuilderTaskResources? = null

    fun config(vararg pairs: Pair<String, Any>) {
        this.config = mapOf(*pairs)
    }

    fun resources(init: NomadJobBuilderTaskResources.() -> Unit) {
        this.resources = NomadJobBuilderTaskResources().apply(init)
    }

    fun docker(init: NomadJobBuilderTaskDocker.() -> Unit) {
        driver = "docker"
        val dockerConfig = NomadJobBuilderTaskDocker().apply(init)
        config(
            "image" to (dockerConfig.image ?: error("Docker image must be specified"))
        )
    }
}

class NomadJobBuilderTaskResources {
    var cpu: Int? = null
    var memory: Int? = null
}

class NomadJobBuilderTaskDocker {
    var image: String? = null

}