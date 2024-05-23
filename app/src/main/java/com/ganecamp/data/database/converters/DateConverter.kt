package com.ganecamp.data.database.converters

import androidx.room.TypeConverter
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class DateConverter {

    @TypeConverter
    fun fromZonedDateTime(zonedDateTime: ZonedDateTime?): String {
        return zonedDateTime?.format(DateTimeFormatter.ISO_ZONED_DATE_TIME) ?: ""
    }

    @TypeConverter
    fun toZonedDateTime(zonedDateTimeString: String?): ZonedDateTime? {
        return if (zonedDateTimeString.isNullOrEmpty()) {
            null
        } else {
            ZonedDateTime.parse(zonedDateTimeString, DateTimeFormatter.ISO_ZONED_DATE_TIME)
        }
    }

}