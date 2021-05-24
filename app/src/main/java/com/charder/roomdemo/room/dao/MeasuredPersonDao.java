package com.charder.roomdemo.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.charder.roomdemo.room.entity.Account;
import com.charder.roomdemo.room.entity.MeasuredPerson;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MeasuredPersonDao {

    @Query("SELECT * FROM MeasuredPerson Where account_id = :account_id")
    List<MeasuredPerson> getAllMP(int account_id);
    @Insert(onConflict = REPLACE)
    List<Long> insert(MeasuredPerson... measuredPersons);
    @Update
    Integer update(MeasuredPerson... measuredPersons);
    @Delete
    Integer delete(MeasuredPerson measuredPerson);
}
