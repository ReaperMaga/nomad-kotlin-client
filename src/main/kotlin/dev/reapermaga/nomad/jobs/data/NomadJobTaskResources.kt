package dev.reapermaga.nomad.jobs.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NomadJobTaskResources(
    @SerialName("CPU")
    val cpu: Int? = null,
    @SerialName("MemoryMB")
    val memory: Int? = null,
)