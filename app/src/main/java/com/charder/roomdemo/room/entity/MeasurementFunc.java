package com.charder.roomdemo.room.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "MeasurementFunc",
        foreignKeys = {@ForeignKey(entity = MeasurementData.class,
                parentColumns = "id",
                childColumns = "data_id",
                onDelete = ForeignKey.CASCADE)
        })
public class MeasurementFunc implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int data_id;
    private int func_id;
    private int numberX10;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getData_id() {
        return data_id;
    }

    public void setData_id(int data_id) {
        this.data_id = data_id;
    }

    public int getFunc_id() {
        return func_id;
    }

    public void setFunc_id(int func_id) {
        this.func_id = func_id;
    }

    public int getNumberX10() {
        return numberX10;
    }

    public void setNumberX10(int numberX10) {
        this.numberX10 = numberX10;
    }
}
