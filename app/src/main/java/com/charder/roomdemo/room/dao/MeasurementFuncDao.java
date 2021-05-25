package com.charder.roomdemo.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.charder.roomdemo.room.entity.MeasuredPerson;
import com.charder.roomdemo.room.entity.MeasurementData;
import com.charder.roomdemo.room.entity.MeasurementFunc;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MeasurementFuncDao {
    @Query("SELECT * FROM MeasurementFunc Where data_id = :data_id")
    List<MeasurementFunc> getAllData(int data_id);
    @Insert(onConflict = REPLACE)
    List<Long> insert(MeasurementFunc... measurementFuncs);
    @Update
    Integer update(MeasurementFunc... measurementFuncs);
    @Delete
    Integer delete(MeasurementFunc measurementFunc);
}
