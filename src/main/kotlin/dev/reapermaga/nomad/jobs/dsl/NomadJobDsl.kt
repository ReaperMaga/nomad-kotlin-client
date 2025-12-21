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
    var type: String? = null
    var datacenters: List<String>? = null

    var meta: NomadJobMetaDsl? = null
    private var taskGroups: MutableList<NomadJobTaskGroupDsl> = mutableListOf()

    fun groups(groups: List<NomadJobTaskGroupDsl>) {
        this.taskGroups = groups.toMutableList()
    }

    fun meta(dsl: NomadJobMetaDsl.() -> Unit) {
        this.meta = NomadJobMetaDsl().apply(dsl)
    }

    fun group(dsl: NomadJobTaskGroupDsl.() -> Unit) {
        this.taskGroups.add(NomadJobTaskGroupDsl().apply(dsl))
    }

    fun build(): NomadCreateJobRequest {
        val job = NomadCreateJob(
            id = this.id,
            type = this.type,
            datacenters = this.datacenters,
            meta = this.meta?.values,
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


class NomadJobTaskResourcesDsl {
    var cpu: Int? = null
    var memory: Int? = null
}

