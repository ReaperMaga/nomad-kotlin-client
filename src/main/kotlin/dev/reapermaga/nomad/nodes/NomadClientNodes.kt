package dev.reapermaga.nomad.nodes

import dev.reapermaga.nomad.NomadClient
import dev.reapermaga.nomad.json
import dev.reapermaga.nomad.nodes.http.NomadNode
import okhttp3.Request
import okhttp3.coroutines.executeAsync

class NomadClientNodes(val client: NomadClient) {

    suspend fun list(): List<NomadNode> {
        val request = Request.Builder()
            .url("${client.baseUrl}/nodes")
            .get()
            .build()
        client.httpClient.newCall(request).executeAsync().use {
            if (!it.isSuccessful) {
                error("Request failed with status code ${it.code}")
            }
            return json.decodeFromString<List<NomadNode>>(it.body.string())
        }
    }

    suspend fun read(nodeId: String): NomadNode? {
        val request = Request.Builder()
            .url("${client.baseUrl}/node/$nodeId")
            .get()
            .build()
        client.httpClient.newCall(request).executeAsync().use {
            if (it.code == 404) return null
            if (!it.isSuccessful) {
                error("Request failed with status code ${it.code}")
            }
            return json.decodeFromString<NomadNode>(it.body.string())
        }
    }
}

