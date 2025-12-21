package dev.reapermaga.nomad.jobs.dsl

class NomadJobTaskGroupDsl {
    var name: String = "default"
    var tasks = listOf<NomadJobTaskDsl>()

    var networks: List<NomadJobTaskGroupNetworkDsl> = listOf()

    fun network(dsl: NomadJobTaskGroupNetworkDsl.() -> Unit) {
        val network = NomadJobTaskGroupNetworkDsl().apply(dsl)
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