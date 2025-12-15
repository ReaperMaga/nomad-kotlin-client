package dev.reapermaga.nomad

import dev.reapermaga.nomad.jobs.http.NomadListJobsRequest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.should
import io.kotest.matchers.string.contain
import io.kotest.matchers.types.beInstanceOf
import kotlinx.coroutines.delay
import java.util.UUID

val client = NomadClient()

class NomadClientTest : StringSpec({
    "statusLeader should return leader address" {
        val leader = client.statusLeader()
        leader should contain(":4647")
    }
    "listJob should return list of jobs" {
        val jobs = client.jobs.list()
        println("Jobs: $jobs")
        jobs should beInstanceOf<List<NomadListJobsRequest>>()
    }
    "create job" {
        val job = client.jobs.create {
            id = "example-job"
            datacenters = listOf("dc1")
            group {
                name = "example-group"
                task {
                    name = "example-task"
                    resources {
                        cpu = 2000
                        memory = 512
                    }
                    docker {
                        image = "redis:latest"
                    }
                }
            }
        }
        println("Job: ${job.evalID}")
    }
    "read job" {
        val createdJob = createExampleJob()
        val job = client.jobs.read(createdJob)
        println("Job: $job")
    }
    "list job allocations" {
        val createdJobId = createExampleJob("test")
        var allocations = client.jobs.listAllocations(createdJobId)
        while (allocations.isNotEmpty() && allocations.first().clientStatus == "pending") {
            println("Waiting for allocation to be running...")
            delay(1000)
            allocations = client.jobs.listAllocations(createdJobId)
        }
        println("Job: $allocations")
    }
})

private suspend fun createExampleJob(jobId: String = UUID.randomUUID().toString()): String {
    client.jobs.create {
        id = jobId
        datacenters = listOf("dc1")
        group {
            name = "example-group"
            task {
                name = "example-task"
                resources {
                    cpu = 2000
                    memory = 512
                }
                docker {
                    image = "redis:latest"
                }
            }
        }
    }
    return jobId
}