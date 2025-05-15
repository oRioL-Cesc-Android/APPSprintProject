package com.TravelPlanner.data.database.util

import androidx.room.TypeConverter

class StringListConverter {

    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString(separator = ";;") // puedes usar otro separador seguro
    }

    @TypeConverter
    fun toList(data: String): List<String> {
        return if (data.isEmpty()) emptyList() else data.split(";;")
    }
}
