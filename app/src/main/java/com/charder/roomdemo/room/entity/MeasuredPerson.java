package com.charder.roomdemo.room.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "MeasuredPerson",
        foreignKeys = {@ForeignKey(entity = Account.class,
        parentColumns = "id",
        childColumns = "account_id",
        onDelete = ForeignKey.CASCADE)
})
public class MeasuredPerson implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int account_id;

    private String idCode;
    private String name;
    private int heightX10;
    private Boolean gender;  // true = man
    private Date birthday;
    private Date createTime;

    @Ignore
    private Date lastDate;
    @Ignore
    private int lastX10;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeightX10() {
        return heightX10;
    }

    public void setHeightX10(int heightX10) {
        this.heightX10 = heightX10;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    public int getLastX10() {
        return lastX10;
    }

    public void setLastX10(int lastX10) {
        this.lastX10 = lastX10;
    }
}
