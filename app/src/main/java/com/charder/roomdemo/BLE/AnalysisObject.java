package com.charder.roomdemo.BLE;

import android.util.Log;

public class AnalysisObject {

    private static final String DeviceName = "U310";
    private static final String SetIndicateUuid = "0000fff1-0000-1000-8000-00805f9b34fb";
    private static final String SetWriteUuid = "0000fff2-0000-1000-8000-00805f9b34fb";

    private static final byte[] ResponseDataOK = {(byte) 0xAA , (byte) 0x02 , (byte) 0x06, (byte) 0x06 };
    private static final byte[] ResponseDataError = {(byte) 0xAA , (byte) 0x02 , (byte) 0x15, (byte) 0x15 };


    private static float height = 180;
    private static int gender = 1;
    private static int age = 25;

    private float weight; //體重
    private float bmi; //bmi

    private float bodyFat; //體脂率
    private float bodyFatRightArm; //體脂 右手
    private float bodyFatLeftArm; //體脂 左手
    private float bodyFatTrunk; //體脂 軀幹
    private float bodyFatRightLeg; //體脂 右腳
    private float bodyFatLeftLeg; //體脂 左腳

    private float muscleMass; //肌肉量
    private float muscleMassRightArm; //肌肉量 右手
    private float muscleMassLeftArm; //肌肉量 左手
    private float muscleMassTrunk; //肌肉量 軀幹
    private float muscleMassRightLeg; //肌肉量 右腳
    private float muscleMassLeftLeg; //肌肉量 左腳

    private int bmr; //基礎代謝
    private float boneMass; //骨量
    private float smr; //骨骼肌肉

    private int vatLevel; // 內臟脂肪
    private int bodyAge; //體年齡
    private float bodyWater; // 體水分

    public static float getHeight() {
        return height;
    }

    public static int getGender() {
        return gender;
    }

    public static int getAge() {
        return age;
    }

    public float getBodyFat() {
        return bodyFat;
    }

    public float getBodyFatRightArm() {
        return bodyFatRightArm;
    }

    public float getBodyFatLeftArm() {
        return bodyFatLeftArm;
    }

    public float getBodyFatTrunk() {
        return bodyFatTrunk;
    }

    public float getBodyFatRightLeg() {
        return bodyFatRightLeg;
    }

    public float getBodyFatLeftLeg() {
        return bodyFatLeftLeg;
    }

    public float getMuscleMass() {
        return muscleMass;
    }

    public float getMuscleMassRightArm() {
        return muscleMassRightArm;
    }

    public float getMuscleMassLeftArm() {
        return muscleMassLeftArm;
    }

    public float getMuscleMassTrunk() {
        return muscleMassTrunk;
    }

    public float getMuscleMassRightLeg() {
        return muscleMassRightLeg;
    }

    public float getMuscleMassLeftLeg() {
        return muscleMassLeftLeg;
    }

    public int getBmr() {
        return bmr;
    }

    public float getBoneMass() {
        return boneMass;
    }

    public float getSmr() {
        return smr;
    }

    public int getVatLevel() {
        return vatLevel;
    }

    public int getBodyAge() {
        return bodyAge;
    }

    public float getBodyWater() {
        return bodyWater;
    }

    private boolean mCheckGroup1 = false; //檢查GROUP1數據是否有接收到
    private boolean mCheckGroup2 = false; //檢查GROUP2數據是否有接收到
    private boolean mCheckGroup3 = false; //檢查GROUP3數據是否有接收到
    private boolean mCheckGroup4 = false; //檢查GROUP3數據是否有接收到
    private boolean mCheckGroup5 = false; //檢查GROUP3數據是否有接收到

    private boolean mCheckLegal = true; //檢查藍芽傳輸訊息是否合法

    public AnalysisObject() {
    }
    public static byte[] getResponseDataOK() {
        return ResponseDataOK;
    }

    public static byte[] getResponseDataError() {
        return ResponseDataError;
    }

    public static String getDeviceName() {
        return DeviceName;
    }

    public static String getSetIndicateUuid() {
        return SetIndicateUuid;
    }

    public static String getSetWriteUuid() {
        return SetWriteUuid;
    }


    public float getWeight() {
        return weight;
    }

//    public float getBmi() {
//        float mHeight =(float) (height / 100.0);
//        float fBmi = this.weight / (mHeight * mHeight);
//        int iBmi = (int)( fBmi * 10 );
//        this.bmi = ( (float) iBmi ) / 10 ;
//
//        return bmi;
//    }


    public static void setHeight(float height) {
         AnalysisObject.height=height;
    }

    public static void setGender(int gender) {
         AnalysisObject.gender=gender;
    }

    public static void setAge(int age) {
         AnalysisObject.age=age;
    }


    public int analysisResult(byte[] data) {

        if (data[0] != (byte) 0xAA){
            return  AnalysisErrorCode.AnalysisError_01;
        }

        //接收第一行數據
        if (data[0] == (byte) 0xAA && data[1] == (byte) 0x04 && data[2] == (byte) 0x43) {
            Log.e("user" , "mCheckGroup1");
            weight = (toInt(data[3]) * 0x100 + toInt(data[4])) / 10.0f;
            mCheckGroup1 = true;

        }

        //接收第二行數據
        if (data[0] == (byte) 0xAA && data[1] == (byte) 0x0E && data[2] == (byte) 0x44) {
            Log.e("user" , "mCheckGroup2");
            bodyFat = (toInt(data[3]) * 0x100 + toInt(data[4])) / 10.0f;
            bodyFatRightArm = (toInt(data[5]) * 0x100 + toInt(data[6])) / 10.0f;
            bodyFatLeftArm = (toInt(data[7]) * 0x100 + toInt(data[8])) / 10.0f;
            bodyFatTrunk = (toInt(data[9]) * 0x100 + toInt(data[10])) / 10.0f;
            bodyFatRightLeg = (toInt(data[11]) * 0x100 + toInt(data[12])) / 10.0f;
            bodyFatLeftLeg = (toInt(data[13]) * 0x100 + toInt(data[14])) / 10.0f;
            mCheckGroup2 = true;

        }

        //接收第三行數據
        if (data[0] == (byte) 0xAA && data[1] == (byte) 0x0E && data[2] == (byte) 0x45) {
            Log.e("user" , "mCheckGroup3");
            muscleMass = (toInt(data[3]) * 0x100 + toInt(data[4])) / 10.0f;
            muscleMassRightArm = (toInt(data[5]) * 0x100 + toInt(data[6])) / 10.0f;
            muscleMassLeftArm = (toInt(data[7]) * 0x100 + toInt(data[8])) / 10.0f;
            muscleMassTrunk = (toInt(data[9]) * 0x100 + toInt(data[10])) / 10.0f;
            muscleMassRightLeg = (toInt(data[11]) * 0x100 + toInt(data[12])) / 10.0f;
            muscleMassLeftLeg = (toInt(data[13]) * 0x100 + toInt(data[14])) / 10.0f;
            mCheckGroup3 = true;

        }

        //接收第四行數據
        if (data[0] == (byte) 0xAA && data[1] == (byte) 0x0E && data[2] == (byte) 0x46) {
            Log.e("user" , "mCheckGroup4");
            bmr = (toInt(data[3]) * 0x100 + toInt(data[4]));
            boneMass = (toInt(data[5]) * 0x100 + toInt(data[6])) / 10.0f;
            smr = (toInt(data[7]) * 0x100 + toInt(data[8])) / 10.0f;
            mCheckGroup4 = true;

        }

        //接收第五行數據
        if (data[0] == (byte) 0xAA && data[1] == (byte) 0x0E && data[2] == (byte) 0x47) {
            Log.e("user" , "mCheckGroup5");
            vatLevel = toInt(data[3]);
            bodyAge = toInt(data[4]);
            bodyWater = (toInt(data[5]) * 0x100 + toInt(data[6]));
            mCheckGroup5 = true;
        }

        if (mCheckGroup1 && mCheckGroup2 && mCheckGroup3 && mCheckGroup4 && mCheckGroup5){
            mCheckGroup1 = false;
            mCheckGroup2 = false;
            mCheckGroup3 = false;
            mCheckGroup4 = false;
            mCheckGroup5 = false;
            return  AnalysisErrorCode.AnalysisOK;
        }else if (mCheckGroup2 || mCheckGroup3 || mCheckGroup4 || mCheckGroup5){
            return  AnalysisErrorCode.AnalysisDataGroupOK;
        }else if (mCheckGroup1){
            return  AnalysisErrorCode.AnalysisWeightOK;
        }
        return AnalysisErrorCode.getAnalysisError_02();
    }

    //提供使用者設置基本訊息
    public static byte[] setUserProfile() {

        if (height > 200){
            height = 200;
        }else if (height < 100){
            height = 100;
        }

        if (age > 82){
            age = 82;
        }else if (age < 7){
            age = 7;
        }
        Log.e("user" , "身高" + height);
        Log.e("user" , "年齡" + age);
        Log.e("user" , "性別" +gender);
//        Log.e("user" ,  "身高：" + height + "年齡：" + age + "性別：" + gender);
        int _hright = (int) (height * 10);
        int mHright = _hright / 256 ;
        int lHright = _hright % 256 ;
        byte[] value = {(byte) 0xAA, (byte) 0x07, (byte) 0x33, (byte) mHright , (byte) lHright , (byte) age, (byte) gender, (byte) 0x00, (byte) 0xFF};
        value[8] = (byte) (value[2] + value[3] + value[4] + value[5] + value[6] + + value[7]);
        return value;
    }


    private int toInt(byte u8) {
        return (int) (u8 & 0xFF);
    }

}
