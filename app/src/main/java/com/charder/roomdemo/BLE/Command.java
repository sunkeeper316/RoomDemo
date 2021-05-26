package com.charder.roomdemo.BLE;

public class Command{
    public static byte[] DataReceivedOK = {(byte) 0xAA , (byte) 0x02 , (byte) 0x0C, (byte) 0x0C };

    public static byte[] getDataReceivedOK() {
        return DataReceivedOK;
    }
}
