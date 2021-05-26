package com.charder.roomdemo.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.charder.roomdemo.room.entity.MeasuredPerson;
import com.charder.roomdemo.room.entity.MeasurementData;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MeasurementDataDao {
    @Query("SELECT * FROM MeasurementData Where MP_id = :MP_id")
    List<MeasurementData> getAllData(int MP_id);
    @Query("SELECT * FROM MeasurementData Where MP_id = :MP_id ORDER BY MeasurementData.date DESC LIMIT 0,1")
    MeasurementData getLastData(int MP_id);
    @Insert(onConflict = REPLACE)
    List<Long> insert(MeasurementData... measurementDataList);
    @Update
    Integer update(MeasurementData... measurementDataList);
    @Delete
    Integer delete(MeasurementData measurementData);
}
