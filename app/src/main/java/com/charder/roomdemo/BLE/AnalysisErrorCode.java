package com.charder.roomdemo.BLE;

public class AnalysisErrorCode{
    public static final int AnalysisError_01 = 1; //資料title不符合，或checkSum不符合
    public static final int AnalysisError_02 = 2; //資料有符合title，但長度太短
    public static final int AnalysisError_03 = 3; //資料有符合，長度太長，確定有符合規則資料
    public static final int AnalysisError_04 = 4; //長度夠，但資料不完整，有解析出來的可能性
    public static final int AnalysisOK = 5;       //資料完全符合
    public static final int AnalysisDataGroupOK = 6; // 每次完成一個接收資料
    public static final int AnalysisWeightOK = 7; // Weight接收OK

    public static int getAnalysisError_01() {
        return AnalysisError_01;
    }

    public static int getAnalysisError_02() {
        return AnalysisError_02;
    }

    public static int getAnalysisError_03() {
        return AnalysisError_03;
    }

    public static int getAnalysisError_04() {
        return AnalysisError_04;
    }

    public static int getAnalysisOK() {
        return AnalysisOK;
    }

    public static int getAnalysisDataGroupOK() {
        return AnalysisDataGroupOK;
    }

    public static int getAnalysisWeightOK() {
        return AnalysisWeightOK;
    }
}





