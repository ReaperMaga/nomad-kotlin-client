package dev.reapermaga.nomad

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.beGreaterThan
import io.kotest.matchers.ints.beGreaterThanOrEqualTo
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.contain
import kotlinx.coroutines.delay
import java.util.*

val client = NomadClient()

class NomadClientTest : StringSpec({
    "status leader should return leader address" {
        val leader = client.status.leader()
        leader should contain(":4647")
    }
    "status peers should return peers" {
        val peers = client.status.peers()
        peers shouldBe listOf("127.0.0.1:4647")
    }
    "listJob should return list of jobs" {
        createExampleJob()
        val jobs = client.jobs.list()
        println("Jobs: $jobs")
        jobs.size shouldBe beGreaterThanOrEqualTo(1)
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
        job.shouldNotBeNull()
    }
    "read job null" {
        val job = client.jobs.read("non-existent-job-id")
        job.shouldBeNull()
    }
    "list job allocations" {
        val createdJobId = createExampleJob("test")
        var allocations = client.jobs.listAllocations(createdJobId)
        var attempts = 5
        while ((allocations.isEmpty() || allocations.first().clientStatus == "pending") && attempts-- > 0) {
            println("Waiting for allocation to be running...")
            delay(1000)
            allocations = client.jobs.listAllocations(createdJobId)
        }
        if (attempts > 0) {
            allocations.size shouldBe beGreaterThan(1)
        }
    }
    "stop job" {
        val createdJobId = createExampleJob()
        val stopResponse = client.jobs.stop(createdJobId)
        stopResponse.evalId.shouldNotBeNull()
    }
    "purge job" {
        val createdJobId = createExampleJob()
        val purgeResponse = client.jobs.purge(createdJobId)
        println(purgeResponse)
    }
    "list nodes" {
        val nodes = client.nodes.list()
        nodes.size should beGreaterThan(0)
    }
    "read node" {
        val node = client.nodes.read("f8bb2185-c7ea-e7de-2db0-00f3c5264c86")
        node.shouldNotBeNull()
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
