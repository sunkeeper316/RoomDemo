package com.charder.roomdemo.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.charder.roomdemo.room.dao.AccountDao;
import com.charder.roomdemo.room.dao.MeasuredPersonDao;
import com.charder.roomdemo.room.dao.MeasurementDataDao;
import com.charder.roomdemo.room.dao.MeasurementFuncDao;
import com.charder.roomdemo.room.entity.Account;
import com.charder.roomdemo.room.entity.MeasuredPerson;
import com.charder.roomdemo.room.entity.MeasurementData;
import com.charder.roomdemo.room.entity.MeasurementFunc;

@Database(entities = {Account.class , MeasuredPerson.class , MeasurementData.class , MeasurementFunc.class} , version = 2)
@TypeConverters(Converters.class)
public abstract class AppDataBase extends RoomDatabase {
    public abstract AccountDao accountDao();
    public abstract MeasuredPersonDao measuredPersonDao();
    public abstract MeasurementDataDao measurementDataDao();
    public abstract MeasurementFuncDao measurementFuncDao();
}
