package dev.reapermaga.nomad.jobs.data

import dev.reapermaga.nomad.util.AnySerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NomadCreateJobRequest(
    @SerialName("Job") val job: NomadCreateJob,
)

@Serializable
data class NomadCreateJob(
    @SerialName("ID") val id: String,
    @SerialName("Type") val type: String,
    @SerialName("Datacenters") val datacenters: List<String>,
    @SerialName("TaskGroups") val taskGroups: List<NomadCreateJobTaskGroup>,
)


@Serializable
data class NomadCreateJobTaskGroup(
    @SerialName("Name") val name: String,
    @SerialName("Tasks") val tasks: List<NomadCreateJobTask>,
    @SerialName("Networks") val networks: List<NomadCreateJobTaskGroupNetwork> = listOf(),
)

@Serializable
data class NomadCreateJobTaskGroupNetwork(
    @SerialName("Mode") val mode: String? = null,
    @SerialName("DynamicPorts") val dynamicPorts: List<NomadCreateJobTaskGroupNetworkPort> = listOf(),
    @SerialName("ReservedPorts") val reservedPorts: List<NomadCreateJobTaskGroupNetworkPort> = listOf(),
)

@Serializable
data class NomadCreateJobTaskGroupNetworkPort(
    @SerialName("Label") val label: String,
    @SerialName("Value") val value: Int? = null,
    @SerialName("To") val to: Int? = null,
    @SerialName("HostNetwork") val hostNetwork: String? = null,
    @SerialName("IgnoreCollision") val ignoreCollision: Boolean? = null,
)

@Serializable
data class NomadCreateJobTask(
    @SerialName("Name") val name: String,
    @SerialName("Driver") val driver: String,
    @SerialName("Env") val env: Map<String, String> = mapOf(),
    @SerialName("Resources") val resources: NomadJobTaskResources? = null,
    @SerialName("Config") val config: Map<String, @Serializable(with = AnySerializer::class) Any>,
)


@Serializable
data class NomadCreateJobResponse(
    @SerialName("EvalID") val evalID: String,
    @SerialName("EvalCreateIndex") val evalCreateIndex: Long,
    @SerialName("JobModifyIndex") val jobModifyIndex: Long,
    @SerialName("Warnings") val warnings: String,
    @SerialName("Index") val index: Long,
    @SerialName("LastContact") val lastContact: Long,
    @SerialName("KnownLeader") val knownLeader: Boolean,
)