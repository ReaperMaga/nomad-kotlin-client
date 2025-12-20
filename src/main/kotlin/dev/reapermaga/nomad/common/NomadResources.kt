package dev.reapermaga.nomad.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NomadResources(
    @SerialName("CPU")
    val cpu: Int? = null,
    @SerialName("MemoryMB")
    val memory: Int? = null,
    @SerialName("DiskMB")
    val diskMB: Int? = null,
    @SerialName("Networks")
    val networks: List<NomadNetwork>? = null,
)