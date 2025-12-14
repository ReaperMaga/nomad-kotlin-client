package dev.reapermaga.nomad.jobs.http

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NomadListJobsRequest(
    @SerialName("ID")
    val id: String,
    @SerialName("Name")
    val name: String,
    @SerialName("Type")
    val type: String,
    @SerialName("Priority")
    val priority: Int,
    @SerialName("Status")
    val status: String,
    @SerialName("JobSummary")
    val jobSummary: NomadJobSummary,
)

@Serializable
data class NomadJobSummary(
    @SerialName("Namespace")
    val namespace: String,
)