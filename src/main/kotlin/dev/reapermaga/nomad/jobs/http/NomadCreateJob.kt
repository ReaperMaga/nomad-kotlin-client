package dev.reapermaga.nomad.jobs.http

import dev.reapermaga.nomad.util.AnySerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NomadCreateJobRequest(
    @SerialName("Job")
    val job: NomadCreateJob,
)

@Serializable
data class NomadCreateJob(
    @SerialName("ID")
    val id: String,
    @SerialName("Datacenters")
    val datacenters: List<String>,
    @SerialName("TaskGroups")
    val taskGroups: List<NomadCreateJobTaskGroup>,
)

@Serializable
data class NomadCreateJobTaskGroup(
    @SerialName("Name")
    val name: String,
    @SerialName("Tasks")
    val tasks: List<NomadCreateJobTask>,
)

@Serializable
data class NomadCreateJobTask(
    @SerialName("Name")
    val name: String,
    @SerialName("Driver")
    val driver: String,
    @SerialName("Resources")
    val resources: NomadCreateJobTaskResources? = null,
    @SerialName("Config")
    val config: Map<String, @Serializable(with = AnySerializer::class) Any>,
)

@Serializable
data class NomadCreateJobTaskResources(
    @SerialName("CPU")
    val cpu: Int? = null,
    @SerialName("MemoryMB")
    val memory: Int? = null,
)


@Serializable
data class NomadCreateJobResponse(
    @SerialName("EvalID")
    val evalID: String,
    @SerialName("EvalCreateIndex")
    val evalCreateIndex: Long,
    @SerialName("JobModifyIndex")
    val jobModifyIndex: Long,
    @SerialName("Warnings")
    val warnings: String,
    @SerialName("Index")
    val index: Long,
    @SerialName("LastContact")
    val lastContact: Long,
    @SerialName("KnownLeader")
    val knownLeader: Boolean,
)