package dev.reapermaga.nomad.nodes.http

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NomadNode(
    @SerialName("ID") val id: String,
    @SerialName("Name") val name: String,
    @SerialName("Address") val address: String? = null,
    @SerialName("Datacenter") val datacenter: String,
    @SerialName("Attributes") val attributes: Map<String, String>? = null,
    @SerialName("Status") val status: String,
    @SerialName("StatusDescription") val statusDescription: String,
)