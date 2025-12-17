package dev.reapermaga.nomad

import dev.reapermaga.nomad.jobs.NomadClientJobs
import dev.reapermaga.nomad.nodes.NomadClientNodes
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.coroutines.executeAsync


class NomadClient(initConfig: NomadClientConfig.() -> Unit = {}) {

    private val config = NomadClientConfig().apply(initConfig)

    val json = Json {
        ignoreUnknownKeys = true
    }

    val httpClient = OkHttpClient.Builder().build()

    val baseUrl: String
        get() = config.address.trimEnd('/').plus("/${config.clientVersion.name.lowercase()}")
            ?: error("address can't be empty")

    val jobs = NomadClientJobs(this)
    val nodes = NomadClientNodes(this)

    suspend inline fun <reified T> requestGet(url: String): T? {
        val request = Request.Builder()
            .url("${baseUrl}$url")
            .get()
            .build()
        httpClient.newCall(request).executeAsync().use {
            if (it.code == 404) return null
            if (!it.isSuccessful) {
                error("Request failed with status code ${it.code}")
            }
            return json.decodeFromString(it.body.string())
        }
    }

    suspend inline fun <reified T, reified I> requestPost(url: String, body: I): T {
        val request = Request.Builder()
            .url("${baseUrl}$url")
            .post(json.encodeToString(body).toRequestBody("application/json".toMediaType()))
            .build()
        httpClient.newCall(request).executeAsync().use {
            if (!it.isSuccessful) {
                error("Request failed with status code ${it.code}")
            }
            return json.decodeFromString(it.body.string())
        }
    }

    suspend inline fun <reified T> requestDelete(url: String): T {
        val request = Request.Builder()
            .url("${baseUrl}$url")
            .delete()
            .build()
        httpClient.newCall(request).executeAsync().use {
            if (!it.isSuccessful) {
                error("Request failed with status code ${it.code}")
            }
            return json.decodeFromString(it.body.string())
        }
    }

    suspend fun statusLeader(): String {
        return requestGet("/status/leader") ?: error("Failed to fetch leader status")
    }

}




