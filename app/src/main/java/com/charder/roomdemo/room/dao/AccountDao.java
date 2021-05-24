package com.charder.roomdemo.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.charder.roomdemo.room.entity.Account;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface AccountDao {
    @Query("SELECT * FROM Account")
    List<Account> getAll();
    @Query("SELECT * FROM Account WHERE account = :account AND password = :password")
    Account loginCheck(String account , String password);
    @Query("SELECT * FROM Account WHERE account = :account")
    Account findByAccount(String account);
    @Query("SELECT * FROM Account WHERE id = :id")
    Account findById(int id);
    @Query("SELECT * FROM Account WHERE id IN (:ids)")
    List<Account> getAllByIds(List<Long> ids);
    @Insert(onConflict = REPLACE)
    List<Long> insert(Account... accounts);
    @Update
    Integer update(Account... accounts);
    @Delete
    Integer delete(Account account);

}
