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

    /**
     * Creates a new Nomad job using the provided DSL.
     *
     * Following fields are required in the DSL:
     * - Job ID (string)
     * - Job Type (e.g., "service", "batch", etc.)
     * - Datacenters (list of datacenter names)
     * - At least one Task Group with at least one Task
     *
     * @param dsl A lambda function that defines the job configuration using NomadJobDsl.
     * @return A NomadCreateJobResponse containing details about the created job.
     */
    suspend fun create(dsl: NomadJobDsl.() -> Unit): NomadCreateJobResponse {
        val jobCreateRequest = NomadJobDsl().apply(dsl).build()
        return client.requestPost("/jobs", jobCreateRequest)
    }

    /**
     * Updates an existing Nomad job identified by jobId using the provided DSL.
     *
     * Following fields are required in the DSL:
     * - Job ID (string)
     * - Job Type (e.g., "service", "batch", etc.)
     * - Datacenters (list of datacenter names)
     * - At least one Task Group with at least one Task
     *
     * @param jobId The ID of the job to update.
     * @param dsl A lambda function that defines the updated job configuration using NomadJobDsl.
     * @return A NomadCreateJobResponse containing details about the updated job.
     */
    suspend fun update(jobId: String, dsl: NomadJobDsl.() -> Unit): NomadCreateJobResponse {
        val jobCreateRequest = NomadJobDsl().apply(dsl).build()
        return client.requestPost("/job/$jobId", jobCreateRequest)
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

