package com.manu.kode.engrama.data.repository

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.manu.kode.engrama.data.model.Division
import java.lang.reflect.Type

class DivisionDeserializer : JsonDeserializer<Division> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Division {
        return try {
            Division.valueOf(json.asString)
        } catch (e: Exception) {
            Division.PLUTON
        }
    }
}