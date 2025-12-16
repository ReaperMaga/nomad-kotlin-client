package dev.reapermaga.nomad.jobs.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class NomadJobAllocation(
    @SerialName("ID") val id: String,
    @SerialName("Name") val name: String,
    @SerialName("NodeID") val nodeId: String,
    @SerialName("DesiredStatus") val desiredStatus: String,
    @SerialName("ClientStatus") val clientStatus: String,
    @SerialName("TaskStates") val taskStates: Map<String, NomadJobAllocationTaskState>?,
)

@Serializable
data class NomadJobAllocationTaskState(
    @SerialName("State") val state: String,
    @SerialName("Failed") val failed: Boolean,
    @SerialName("StartedAt") val startedAt: Instant?,
    @SerialName("FinishedAt") val finishedAt: Instant?,
)