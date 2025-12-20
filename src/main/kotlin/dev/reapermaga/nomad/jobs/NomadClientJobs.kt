package dev.reapermaga.nomad.jobs

import dev.reapermaga.nomad.NomadClient
import dev.reapermaga.nomad.allocations.data.NomadAllocation
import dev.reapermaga.nomad.jobs.data.NomadCreateJobResponse
import dev.reapermaga.nomad.jobs.data.NomadJob
import dev.reapermaga.nomad.jobs.data.NomadStopJob
import dev.reapermaga.nomad.jobs.dsl.NomadJobDsl

class NomadClientJobs(val client: NomadClient) {

    suspend fun list(): List<NomadJob> {
        return client.requestGet("/jobs") ?: error("Failed to fetch jobs")
    }

    suspend fun create(dsl: NomadJobDsl.() -> Unit): NomadCreateJobResponse {
        val jobCreateRequest = NomadJobDsl().apply(dsl).build()
        return client.requestPost("/jobs", jobCreateRequest)
    }

    suspend fun read(jobId: String): NomadJob? {
        return client.requestGet("/job/$jobId")
    }

    suspend fun stop(jobId: String): NomadStopJob {
        return client.requestDelete("/job/$jobId")
    }

    suspend fun purge(jobId: String): NomadStopJob {
        return client.requestDelete("/job/$jobId?purge=true")
    }

    suspend fun listAllocations(jobId: String): List<NomadAllocation> {
        return client.requestGet("/job/$jobId/allocations") ?: error("Failed to fetch job allocations")
    }
}

