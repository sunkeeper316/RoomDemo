package com.charder.roomdemo.common;

public class Function {
    final static public Function func1 = new Function( 0 , "weight" , "kg");
    final static public Function func2 = new Function( 1 , "BMI" , "");
    final static public Function func3 = new Function( 2 , "bodyFate" , "%");

    public Function(int funcId, String funcName, String unit) {
        this.funcId = funcId;
        this.funcName = funcName;
        this.unit = unit;
    }

    private int funcId;
    private String funcName;
    private int number;
    String unit;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

}
