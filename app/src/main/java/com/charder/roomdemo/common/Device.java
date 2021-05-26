package com.charder.roomdemo.common;

import com.charder.roomdemo.room.entity.MeasurementFunc;

public class Device {
    final static public String deviceName = "U310";

    static public MeasurementFunc[] U310Func( int dataId){
        MeasurementFunc[] measurementFuncs = new MeasurementFunc[21];
        for (int i = 0 ; i< measurementFuncs.length ; i++ ){
            measurementFuncs[i] = new MeasurementFunc();
            measurementFuncs[i].setData_id(dataId);
            measurementFuncs[i].setFunc_id(i);
        }
        return measurementFuncs;
    }
}

// u310 有20個數據 包含體態計算後+1
//    private float weight; //體重
//    private float bmi; //bmi
//
//    private float bodyFat; //體脂率
//    private float bodyFatRightArm; //體脂 右手
//    private float bodyFatLeftArm; //體脂 左手
//    private float bodyFatTrunk; //體脂 軀幹
//    private float bodyFatRightLeg; //體脂 右腳
//    private float bodyFatLeftLeg; //體脂 左腳
//
//    private float muscleMass; //肌肉量
//    private float muscleMassRightArm; //肌肉量 右手
//    private float muscleMassLeftArm; //肌肉量 左手
//    private float muscleMassTrunk; //肌肉量 軀幹
//    private float muscleMassRightLeg; //肌肉量 右腳
//    private float muscleMassLeftLeg; //肌肉量 左腳
//
//    private int bmr; //基礎代謝
//    private float boneMass; //骨量
//    private float smr; //骨骼肌肉
//
//    private int vatLevel; // 內臟脂肪
//    private int bodyAge; //體年齡
//    private float bodyWater; // 體水分
