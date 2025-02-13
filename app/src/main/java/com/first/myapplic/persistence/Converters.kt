package com.first.myapplic.persistence

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class Converters {
    private val gson = Gson()
    private val listType: Type = object : TypeToken<List<String>>() {}.type

    @TypeConverter
    fun fromListToString(list: List<String>?): String? {
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromStringToList(value: String?): List<String>? {
        return gson.fromJson(value, listType)
    }
}
