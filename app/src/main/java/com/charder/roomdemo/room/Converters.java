package com.charder.roomdemo.room;

import androidx.room.TypeConverter;

import java.util.Date;

public class Converters {
    @TypeConverter
    public Date fromTimestamp(Long valus) {
        return new Date(valus);
    }
    @TypeConverter
    public Long dateToTimestamp(Date date) {
        return date.getTime();
    }
}
