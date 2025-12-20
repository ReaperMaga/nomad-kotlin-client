package dev.reapermaga.nomad.allocations

import dev.reapermaga.nomad.NomadClient
import dev.reapermaga.nomad.allocations.data.NomadAllocation

class NomadClientAllocations(val client: NomadClient) {

    suspend fun list(): List<NomadAllocation> {
        return client.requestGet<List<NomadAllocation>>("/allocations") ?: emptyList()
    }

    suspend fun read(allocId: String): NomadAllocation? {
        return client.requestGet<NomadAllocation>("/allocation/$allocId")
    }
}