package dev.reapermaga.nomad.jobs.dsl

import dev.reapermaga.nomad.common.NomadNetwork
import dev.reapermaga.nomad.common.NomadPort
import dev.reapermaga.nomad.common.NomadResources
import dev.reapermaga.nomad.jobs.data.NomadCreateJob
import dev.reapermaga.nomad.jobs.data.NomadCreateJobRequest
import dev.reapermaga.nomad.jobs.data.NomadCreateJobTask
import dev.reapermaga.nomad.jobs.data.NomadCreateJobTaskGroup
import java.util.*

class NomadJobDsl {

    var id: String = UUID.randomUUID().toString()
    var type = "service"
    var datacenters = listOf<String>()
    private var taskGroups: List<NomadJobTaskGroupDsl> = listOf()

    fun groups(groups: List<NomadJobTaskGroupDsl>) {
        this.taskGroups = groups
    }

    fun group(dsl: NomadJobTaskGroupDsl.() -> Unit) {
        val group = NomadJobTaskGroupDsl().apply(dsl)
        this.taskGroups += group
    }

    fun build(): NomadCreateJobRequest {
        val job = NomadCreateJob(
            id = this.id,
            type = this.type,
            datacenters = this.datacenters,
            taskGroups = this.taskGroups.map { group ->
                NomadCreateJobTaskGroup(
                    name = group.name,
                    networks = group.networks.map { network ->
                        NomadNetwork(
                            mode = network.mode,
                            dynamicPorts = network.dynamicPorts.map { port ->
                                NomadPort(
                                    label = port.label,
                                    value = port.value,
                                    to = port.to,
                                    hostNetwork = port.hostNetwork,
                                    ignoreCollision = port.ignoreCollision,
                                )
                            },
                            reservedPorts = network.reservedPorts.map { port ->
                                NomadPort(
                                    label = port.label,
                                    value = port.value,
                                    to = port.to,
                                    hostNetwork = port.hostNetwork,
                                    ignoreCollision = port.ignoreCollision,
                                )
                            },
                        )
                    },
                    tasks = group.tasks.map { task ->
                        NomadCreateJobTask(
                            name = task.name,
                            driver = task.driver,
                            config = task.config,
                            env = task.environment,
                            resources = task.resources?.let { res ->
                                NomadResources(
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

    var networks: List<NomadJobTasKGroupNetworkDsl> = listOf()

    fun network(dsl: NomadJobTasKGroupNetworkDsl.() -> Unit) {
        val network = NomadJobTasKGroupNetworkDsl().apply(dsl)
        this.networks += network
    }

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

class NomadJobTaskResourcesDsl {
    var cpu: Int? = null
    var memory: Int? = null
}

class NomadJobTaskDockerDsl {
    var image: String? = null

    var ports = listOf<String>()

    var networkMode: String? = null
}

class NomadJobTasKGroupNetworkDsl {
    var mode: String? = null
    var dynamicPorts = listOf<NomadJobTaskGroupNetworkPortDsl>()
    var reservedPorts = listOf<NomadJobTaskGroupNetworkPortDsl>()


    fun dynamicPort(dsl: NomadJobTaskGroupNetworkPortDsl.() -> Unit) {
        val port = NomadJobTaskGroupNetworkPortDsl().apply(dsl)
        this.dynamicPorts += port
    }

    fun reservedPort(dsl: NomadJobTaskGroupNetworkPortDsl.() -> Unit) {
        val port = NomadJobTaskGroupNetworkPortDsl().apply(dsl)
        this.reservedPorts += port
    }
}

class NomadJobTaskGroupNetworkPortDsl {
    var label: String = "http"
    var value: Int? = null
    var to: Int? = null
    var hostNetwork: String? = null
    var ignoreCollision: Boolean? = null
}