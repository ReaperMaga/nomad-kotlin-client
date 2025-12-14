package dev.reapermaga.nomad.jobs.http

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class NomadReadJobResponse(
    @SerialName("ID")
    val id: String,
    @SerialName("Name")
    val name: String,
    @SerialName("Namespace")
    val namespace: String,
    @SerialName("Datacenters")
    val datacenters: List<String>,
    @SerialName("Status")
    val status: String,
    @SerialName("StatusDescription")
    val statusDescription: String,
    @SerialName("TaskGroups")
    val taskGroups: List<NomadReadJobTaskGroup>,
)

@Serializable
data class NomadReadJobTaskGroup(
    @SerialName("Name")
    val name: String,
    @SerialName("Tasks")
    val tasks: List<NomadReadJobTask>,
)

@Serializable
data class NomadReadJobTask(
    @SerialName("Name")
    val name: String,
    @SerialName("Driver")
    val driver: String,
    @SerialName("Resources")
    val resources: NomadJobTaskResources? = null,
)
