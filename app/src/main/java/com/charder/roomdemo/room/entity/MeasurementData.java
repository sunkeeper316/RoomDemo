package com.charder.roomdemo.room.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "MeasurementData",
        foreignKeys = {@ForeignKey(entity = MeasuredPerson.class,
                parentColumns = "id",
                childColumns = "MP_id",
                onDelete = ForeignKey.CASCADE)
        })
public class MeasurementData {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int MP_id;
    private Date date;
    private int deviceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMP_id() {
        return MP_id;
    }

    public void setMP_id(int MP_id) {
        this.MP_id = MP_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }
}
