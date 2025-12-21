package dev.reapermaga.nomad.jobs.dsl

class NomadJobTaskGroupNetworkDsl {
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