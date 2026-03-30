package io.github.taetae98coding.diary.core.weather.network.api.serializer

import kotlin.time.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object InstantSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("UnixInstant", PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder): Instant {
        return Instant.fromEpochSeconds(decoder.decodeLong())
    }

    override fun serialize(
        encoder: Encoder,
        value: Instant,
    ) {
        encoder.encodeLong(value.epochSeconds)
    }
}
