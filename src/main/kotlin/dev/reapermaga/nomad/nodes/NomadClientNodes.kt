package dev.reapermaga.nomad.nodes

import dev.reapermaga.nomad.NomadClient
import dev.reapermaga.nomad.allocations.data.NomadAllocation
import dev.reapermaga.nomad.nodes.data.NomadNode

class NomadClientNodes(val client: NomadClient) {

    suspend fun list(): List<NomadNode> {
        return client.requestGet("/nodes") ?: error("Failed to fetch nodes")
    }

    suspend fun read(nodeId: String): NomadNode? {
        return client.requestGet("/node/$nodeId")
    }

    suspend fun listAllocations(nodeId: String): List<NomadAllocation> {
        return client.requestGet("/node/$nodeId/allocations") ?: emptyList()
    }
}

