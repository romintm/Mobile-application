package com.example.teamwork_management.dataClasses.db

import androidx.room.TypeConverter
import java.util.Date

class Converters {
        @TypeConverter
        fun fromTimestamp(value: Long?): Date? {
            return value?.let { Date(it) }
        }

        @TypeConverter
        fun dateToTimestamp(date: Date?): Long? {
            return date?.time
        }
    @TypeConverter
    fun fromStringList(value: String?): List<String> {
        return value?.split(",") ?: listOf()
    }

    @TypeConverter
    fun toStringList(list: List<String>?): String {
        return list?.joinToString(",") ?: ""
    }
    @TypeConverter
    fun fromIntList(value: String?): List<Int> {
        return value?.split(",")?.map { it.toInt() } ?: listOf()
    }

    @TypeConverter
    fun toIntList(list: List<Int>?): String {
        return list?.joinToString(",") ?: ""
    }
    }

