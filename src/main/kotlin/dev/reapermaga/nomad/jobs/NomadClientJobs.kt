package dev.reapermaga.nomad.jobs

import dev.reapermaga.nomad.NomadClient
import dev.reapermaga.nomad.jobs.http.NomadCreateJobResponse
import dev.reapermaga.nomad.jobs.http.NomadJobAllocation
import dev.reapermaga.nomad.jobs.http.NomadListJobsRequest
import dev.reapermaga.nomad.jobs.http.NomadReadJobResponse
import dev.reapermaga.nomad.json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.coroutines.executeAsync

class NomadClientJobs(val client: NomadClient) {

    suspend fun list(): List<NomadListJobsRequest> {
        val request = Request.Builder()
            .url("${client.baseUrl}/jobs")
            .get()
            .build()
        client.httpClient.newCall(request).executeAsync().use {
            if (!it.isSuccessful) {
                error("Request failed with status code ${it.code}")
            }
            val responseBody = it.body.string()
            return json.decodeFromString<List<NomadListJobsRequest>>(responseBody)
        }
    }

    suspend fun create(builder: NomadJobBuilder.() -> Unit): NomadCreateJobResponse {
        val jobCreateRequest = NomadJobBuilder().apply(builder).build()
        val request = Request.Builder()
            .url("${client.baseUrl}/jobs")
            .post(json.encodeToString(jobCreateRequest).toRequestBody("application/json".toMediaType()))
            .build()
        client.httpClient.newCall(request).executeAsync().use {
            if (!it.isSuccessful) {
                error("Request failed with status code ${it.code}")
            }
            return json.decodeFromString<NomadCreateJobResponse>(it.body.string())
        }
    }

    suspend fun read(jobId: String): NomadReadJobResponse? {
        val request = Request.Builder()
            .url("${client.baseUrl}/job/$jobId")
            .get()
            .build()
        client.httpClient.newCall(request).executeAsync().use {
            if (it.code == 404) return null
            if (!it.isSuccessful) {
                error("Request failed with status code ${it.code}")
            }
            return json.decodeFromString<NomadReadJobResponse>(it.body.string())
        }
    }

    suspend fun listAllocations(jobId: String): List<NomadJobAllocation> {
        val request = Request.Builder()
            .url("${client.baseUrl}/job/${jobId}/allocations")
            .get()
            .build()
        client.httpClient.newCall(request).executeAsync().use {
            if (!it.isSuccessful) {
                error("Request failed with status code ${it.code}")
            }
            return json.decodeFromString<List<NomadJobAllocation>>(it.body.string())
        }
    }
}

