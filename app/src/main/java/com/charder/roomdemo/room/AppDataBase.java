package com.charder.roomdemo.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.charder.roomdemo.room.dao.AccountDao;
import com.charder.roomdemo.room.entity.Account;

@Database(entities = {Account.class} , version = 1)
@TypeConverters(Converters.class)
public abstract class AppDataBase extends RoomDatabase {
    public abstract AccountDao accountDao();
}
