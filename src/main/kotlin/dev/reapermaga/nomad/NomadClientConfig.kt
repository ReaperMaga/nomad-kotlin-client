package dev.reapermaga.nomad

class NomadClientConfig {
    var address: String = "http://localhost:4646"
    var token: String? = null
    var clientVersion: NomadClientVersion = NomadClientVersion.V1
}

enum class NomadClientVersion {
    V1
}