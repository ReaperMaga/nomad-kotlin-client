package dev.reapermaga.nomad.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NomadNetwork(
    @SerialName("Mode") val mode: String? = null,
    @SerialName("IP") val ip: String? = null,
    @SerialName("DynamicPorts") val dynamicPorts: List<NomadPort> = listOf(),
    @SerialName("ReservedPorts") val reservedPorts: List<NomadPort> = listOf(),
)

@Serializable
data class NomadPort(
    @SerialName("Label") val label: String,
    @SerialName("Value") val value: Int? = null,
    @SerialName("To") val to: Int? = null,
    @SerialName("HostNetwork") val hostNetwork: String? = null,
    @SerialName("IgnoreCollision") val ignoreCollision: Boolean? = null,
)