package dev.reapermaga.nomad.jobs.dsl

class NomadJobMetaDsl {

    var values = mutableMapOf<String, String>()

    fun put(key: String, value: String) {
        values[key] = value
    }

}