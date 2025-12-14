package dev.reapermaga.nomad

import dev.reapermaga.nomad.jobs.http.NomadListJobsRequest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.should
import io.kotest.matchers.string.contain
import io.kotest.matchers.types.beInstanceOf

val client = NomadClient()

class NomadClientTest : StringSpec({
    "statusLeader should return leader address" {
        val leader = client.statusLeader()
        leader should contain(":4647")
    }
    "listJob should return list of jobs" {
        val client = NomadClient()
        val jobs = client.jobs.list()
        println("Jobs: $jobs")
        jobs should beInstanceOf<List<NomadListJobsRequest>>()
    }
    "create job" {
        val client = NomadClient()
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
})