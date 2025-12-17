package dev.reapermaga.nomad.status

import dev.reapermaga.nomad.NomadClient

class NomadClientStatus(val client: NomadClient) {
    suspend fun leader(): String {
        return client.requestGet("/status/leader") ?: error("Failed to fetch status leader")
    }
    suspend fun peers(): List<String> {
        return client.requestGet("/status/peers") ?: error("Failed to fetch status peers")
    }
}

