package com.svalero.workhub.util;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.Date;

public class DateConverter {
    @TypeConverter
    public static Date fromTimestamp(Long value){
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTomestamp(Date date){
        return date == null ? null : date.getTime();
    }

}
