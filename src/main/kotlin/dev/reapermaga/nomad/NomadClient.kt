package dev.reapermaga.nomad

import dev.reapermaga.nomad.jobs.NomadClientJobs
import dev.reapermaga.nomad.nodes.NomadClientNodes
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.coroutines.executeAsync

val json = Json {
    ignoreUnknownKeys = true
}

class NomadClient(initConfig: NomadClientConfig.() -> Unit = {}) {

    private val config = NomadClientConfig().apply(initConfig)

    val httpClient = OkHttpClient.Builder().build()

    val baseUrl: String
        get() = config.address.trimEnd('/').plus("/${config.clientVersion.name.lowercase()}")
            ?: error("address can't be empty")

    val jobs = NomadClientJobs(this)
    val nodes = NomadClientNodes(this)

    suspend fun statusLeader(): String {
        val request = Request.Builder()
            .url("$baseUrl/status/leader")
            .get()
            .build()
        httpClient.newCall(request).executeAsync().use {
            if (!it.isSuccessful) {
                error("Request failed with status code ${it.code}")
            }
            return it.body.string()
        }
    }

}




