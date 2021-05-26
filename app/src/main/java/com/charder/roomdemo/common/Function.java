package com.charder.roomdemo.common;

public class Function {
    final static public Function func1 = new Function( 0 , "weight" , "kg");
    final static public Function func2 = new Function( 1 , "BMI" , "");
    final static public Function func3 = new Function( 2 , "bodyFate" , "%");
    final static public Function func4 = new Function( 3 , "bodyFatRightArm" , "kg");
    final static public Function func5 = new Function( 4 , "bodyFatLeftArm" , "kg");
    final static public Function func6 = new Function( 5 , "bodyFatTrunk" , "kg");
    final static public Function func7 = new Function( 6 , "bodyFatRightLeg" , "kg");
    final static public Function func8 = new Function( 7 , "bodyFatLeftLeg" , "kg");
    final static public Function func9 = new Function( 8 , "muscleMass" , "kg");
    final static public Function func10 = new Function( 9 , "muscleMassRightArm" , "kg");
    final static public Function func11 = new Function( 10 , "muscleMassLeftArm" , "kg");
    final static public Function func12 = new Function( 11 , "muscleMassTrunk" , "kg");
    final static public Function func13 = new Function( 12 , "muscleMassRightLeg" , "kg");
    final static public Function func14 = new Function( 13 , "muscleMassLeftLeg" , "kg");
    final static public Function func15 = new Function( 14 , "bmr" , "卡");
    final static public Function func16 = new Function( 15 , "boneMass" , "kg");
    final static public Function func17 = new Function( 16 , "smr" , "kg");
    final static public Function func18 = new Function( 17 , "vatLevel" , "Lv");
    final static public Function func19 = new Function( 18 , "bodyAge" , "");
    final static public Function func20 = new Function( 19 , "bodyWater" , "%");
    final static public Function func21 = new Function( 20 , "體態" , "");

    final static public Function[] Functions = {func1 ,func2 , func3 , func4, func5, func6, func7, func8, func9, func10, func11, func12, func13, func14, func15, func16, func17, func18, func19, func20, func21};

    static public Function getFunc(int funcId) {
        return Functions[funcId];
    }

    public Function(int funcId, String funcName, String unit) {
        this.funcId = funcId;
        this.funcName = funcName;
        this.unit = unit;
    }

    private int funcId;
    private String funcName;
    String unit;

    public int getFuncId() {
        return funcId;
    }

    public String getFuncName() {
        return funcName;
    }

    public String getUnit() {
        return unit;
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
