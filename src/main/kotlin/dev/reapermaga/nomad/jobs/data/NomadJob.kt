package dev.reapermaga.nomad.jobs.data

import dev.reapermaga.nomad.common.NomadResources
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class NomadJob(
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
    @SerialName("Type")
    val type: String,
    @SerialName("StatusDescription")
    val statusDescription: String,
    @SerialName("TaskGroups")
    val taskGroups: List<NomadJobTaskGroup>? = null,
    @SerialName("Meta")
    val meta: Map<String, String>? = null,
)

@Serializable
data class NomadJobTaskGroup(
    @SerialName("Name")
    val name: String,
    @SerialName("Tasks")
    val tasks: List<NomadJobTask>,
)

@Serializable
data class NomadJobTask(
    @SerialName("Name")
    val name: String,
    @SerialName("Driver")
    val driver: String,
    @SerialName("Resources")
    val resources: NomadResources? = null,
)
