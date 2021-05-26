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
    @Query("SELECT " +
            "MeasuredPerson.id ," +
            " MeasuredPerson.account_id," +
            " MeasuredPerson.birthday," +
            " MeasuredPerson.gender," +
            " MeasuredPerson.heightX10," +
            " MeasuredPerson.idCode," +
            " MeasuredPerson.name," +
            "MeasuredPerson.createTime"+
            " FROM Account INNER JOIN MeasuredPerson ON Account.id = MeasuredPerson.account_id  WHERE Account.id = :account_id OR Account.permission = 2 ")
    List<MeasuredPerson> getPermission1MP(int account_id);
    @Query("SELECT * FROM MeasuredPerson Where account_id = :account_id")
    List<MeasuredPerson> getPermission2MP(int account_id);
    @Query("SELECT * FROM MeasuredPerson ")
    List<MeasuredPerson> getAllMP();
    @Insert(onConflict = REPLACE)
    List<Long> insert(MeasuredPerson... measuredPersons);
    @Update
    Integer update(MeasuredPerson... measuredPersons);
    @Delete
    Integer delete(MeasuredPerson measuredPerson);
}
