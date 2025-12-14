package dev.reapermaga.nomad.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object AnySerializer : KSerializer<Any> {
    override val descriptor = buildClassSerialDescriptor("Any")

    override fun serialize(encoder: Encoder, value: Any) {
        when (value) {
            is String -> encoder.encodeString(value)
            else -> error("Unsupported type: ${value::class}")
        }
    }

    override fun deserialize(decoder: Decoder): Any {
        return decoder.decodeString()
    }

}