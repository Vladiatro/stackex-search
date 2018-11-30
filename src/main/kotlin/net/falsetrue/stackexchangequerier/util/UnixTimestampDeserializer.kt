package net.falsetrue.stackexchangequerier.util

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.LocalDateTime
import java.time.ZoneOffset

class UnixTimestampDeserializer: JsonDeserializer<LocalDateTime>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): LocalDateTime {
        val timestamp = p!!.longValue
        return LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC)
    }
}