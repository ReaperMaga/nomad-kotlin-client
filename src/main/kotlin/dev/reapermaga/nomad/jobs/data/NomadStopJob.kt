package dev.reapermaga.nomad.jobs.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NomadStopJob(
    @SerialName("EvalID")
    val evalId: String,
    @SerialName("EvalCreateIndex")
    val evalCreateIndex: Int
)
