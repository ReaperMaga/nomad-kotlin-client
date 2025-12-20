package dev.reapermaga.nomad.allocations.data

import dev.reapermaga.nomad.common.NomadResources
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class NomadAllocation(
    @SerialName("ID") val id: String,
    @SerialName("Name") val name: String,
    @SerialName("Namespace") val namespace: String,
    @SerialName("NodeID") val nodeId: String,
    @SerialName("JobID") val jobId: String,
    @SerialName("JobType") val jobType: String? = null,
    @SerialName("TaskGroup") val taskGroup: String,
    @SerialName("EvalID") val evalId: String,
    @SerialName("CreateTime") val createTime: Long,
    @SerialName("Resources") val resources: NomadResources? = null,
    @SerialName("ModifyTime") val modifyTime: Long,
    @SerialName("DesiredStatus") val desiredStatus: String,
    @SerialName("ClientStatus") val clientStatus: String,
    @SerialName("DeploymentStatus") val deploymentStatus: NomadAllocationDeploymentStatus? = null,
    @SerialName("TaskStates") val taskStates: Map<String, NomadAllocationTaskState>? = null,
    @SerialName("TaskResources") val taskResources: Map<String, NomadResources>? = null,
)

@Serializable
data class NomadAllocationTaskState(
    @SerialName("State") val state: String,
    @SerialName("Failed") val failed: Boolean,
    @SerialName("StartedAt") val startedAt: Instant?,
    @SerialName("FinishedAt") val finishedAt: Instant?,
)

@Serializable
data class NomadAllocationDeploymentStatus(
    @SerialName("Canary") val canary: Boolean,
    @SerialName("Healthy") val healthy: Boolean,
    @SerialName("Timestamp") val timestamp: Instant,
)