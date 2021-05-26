package com.charder.roomdemo.BLE;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CommService extends Service {
    private final static String TAG = CommService.class.getSimpleName();

    private BluetoothGatt bluetoothGatt;

    private int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED = "ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "ACTION_DATA_AVAILABLE";

    public final static String EXTRA_DATA = "EXTRA_DATA";

    public BluetoothDevice bluetoothDevice;

    public BluetoothGattCharacteristic setIndicateCharacteristic;
    public BluetoothGattCharacteristic setWriteCharacteristic;

    public BluetoothGattService deviceInformationService;
//    public ArrayList<Blu>

    //==================================scaleComm=================================

    public static class ScaleComm extends Activity {

        public static final int BT_RequestCode = 1;
        public static final int Location_RequestCode = 101;
        private static final int PER_ACCESS_LOCATION = 201;

        private Activity activity;
        private BluetoothManager bluetoothManager;

        AnalysisObject analysisObject;

        LocationManager locationManager;

        public BluetoothAdapter bluetoothAdapter;
        private CommService commService;

        private ConnectHandler connectHandler;
        private DiscoverHandler discoverHandler;
        private ResultHandler resultHandler;
        private DataResultHandler dataResultHandler;
        private ResultWeightHandler resultWeightHandler;

        public ScaleComm() {

        }

        //==================================init=================================

        public ScaleComm(Activity activity) {
            this.activity = activity;
            connect();
        }

        public void connect() {

            bluetoothManager = (BluetoothManager) this.activity.getSystemService(Context.BLUETOOTH_SERVICE);
            if (bluetoothManager == null) {
                return;
            }
            bluetoothAdapter = bluetoothManager.getAdapter();
            if (bluetoothAdapter == null) {
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (this.activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    this.activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PER_ACCESS_LOCATION);
                    return;
                } else {
                    checkLocationSettings();
                }

            } else {
                bluetoothEnabled();
            }

        }

        public void bluetoothEnabled() {
            if (bluetoothAdapter.isEnabled()) {
                Log.i("DEBUG", "==== bluetoothAdapter isEnabled ==== " + bluetoothAdapter.isEnabled());
                serviceConnection();
            } else {
                Log.i("DEBUG", "==== bluetoothAdapter isEnabled ==== " + bluetoothAdapter.isEnabled());
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                this.activity.startActivityForResult(enableBtIntent, BT_RequestCode);
            }
        }

        // 檢查裝置是否開啟Location設定
        private void checkLocationSettings() {
            // 必須將LocationRequest設定加入檢查
            LocationSettingsRequest.Builder builder =
                    new LocationSettingsRequest.Builder().addLocationRequest(new LocationRequest());
            Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(activity).checkLocationSettings(builder.build());
            task.addOnSuccessListener(activity, locationSettingsResponse -> {
                if (ActivityCompat.checkSelfPermission(activity,
                        Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
                    // 取得並顯示最新位置

                }
                bluetoothEnabled();
            });
            task.addOnFailureListener(e -> {
                if (e instanceof ResolvableApiException) {
                    Log.e(TAG, e.getMessage());
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        // 跳出Location設定的對話視窗
                        resolvable.startResolutionForResult(activity, Location_RequestCode);
                    } catch (IntentSender.SendIntentException sendEx) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            });
        }

        public void serviceConnection() {
            Intent gattServiceIntent = new Intent(this.activity, CommService.class);
            this.activity.bindService(gattServiceIntent, serviceConnection, BIND_AUTO_CREATE);
            this.activity.registerReceiver(broadcastReceiver, makeGattUpdateIntentFilter());
        }

        public void connect(BluetoothDevice device) {
            if (commService != null) {
                commService.connect(device);
            }
        }

        public void disconnect() {
            if (commService != null) {
                commService.disconnect();
            }
        }

        //==================================init=================================

        public static int GetBT_RequestCode() {
            return BT_RequestCode;
        }

        public static int GetLocation_RequestCode() {
            return Location_RequestCode;
        }

        public static int GetPER_ACCESS_LOCATION() {
            return PER_ACCESS_LOCATION;
        }

        public void setConnectHandler(ConnectHandler connectHandler) {
            this.connectHandler = connectHandler;
        }

        public void setDiscoverHandler(DiscoverHandler discoverHandler) {
            this.discoverHandler = discoverHandler;
        }

        public void setResultHandler(ResultHandler resultHandler) {
            this.resultHandler = resultHandler;
        }

        public void setDataResultHandler(DataResultHandler dataResultHandler) {
            this.dataResultHandler = dataResultHandler;
        }

        public void setResultWeightHandler(ResultWeightHandler resultWeightHandler) {
            this.resultWeightHandler = resultWeightHandler;
        }

        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                if (commService != null) {
                    commService = null;
                }
                commService = ((LocalBinder) service).getService();
                if (bluetoothAdapter != null) {
                    bluetoothAdapter.startLeScan(leScanCallback);
                }
                Log.i("DEBUG", "==== bluetoothLeService open ====" + commService);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                if (commService != null) {
                    commService = null;
                }
            }
        };

        private BluetoothAdapter.LeScanCallback leScanCallback = (device, rssi, scanRecord) -> {
            if (device.getName() != null) {
                Log.i("DEBUG", "=device=" + device.getName());
                if (device.getName().contains( AnalysisObject.getDeviceName())) {
//                    bluetoothDevice=device;
//                    commService.connect (device);
                    if (discoverHandler != null) {
                        discoverHandler.handler(device);
                    }
                }
            }
        };

        //==================================broadcastReceiver=================================

        private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();

                if (CommService.ACTION_GATT_CONNECTED.equals(action)) {
                    Log.i("DEBUG", "====ACTION_GATT_CONNECTED====");
                    bluetoothAdapter.stopLeScan(leScanCallback);
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        if (connectHandler != null) {
                            connectHandler.handler(true);
                        }
                    }, 300);

                } else if (CommService.ACTION_GATT_DISCONNECTED.equals(action)) {
                    Log.i("DEBUG", "====ACTION_GATT_DISCONNECTED====");
                    // /*
                    // 怕秤子disconnect後還在廣播，delay 一些再 scan  NG!!!

                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        if (connectHandler != null) {
                            connectHandler.handler(false);
                        }
                        bluetoothAdapter.startLeScan(leScanCallback);
                        Toast.makeText(activity, "Scanning", Toast.LENGTH_SHORT).show();
                    }, 300);
                    //*/
                } else if (CommService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                    Log.i("DEBUG", "====ACTION_GATT_SERVICES_DISCOVERED====");
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        commService.setNotifitice();
                        Handler handlerWrite = new Handler();
                        handlerWrite.postDelayed(() -> {
                             AnalysisObject.setHeight(160);
                             AnalysisObject.setAge(25);
                             AnalysisObject.setGender(1);
                            if (commService.setWriteCharacteristic != null) {
                                Log.i("user", "setWriteCharacteristic");

                                byte[] command =  AnalysisObject.setUserProfile();
                                commService.setWriteBluetoothGatt(command);
                            }
                        }, 500);
                    }, 500);

                } else if (CommService.ACTION_DATA_AVAILABLE.equals(action)) {
                    Log.i("DEBUG", "====ACTION_DATA_AVAILABLE====");
                    byte[] data = intent.getByteArrayExtra(CommService.EXTRA_DATA);

                    if (Arrays.equals(data,  AnalysisObject.getResponseDataOK())) {
                        Toast.makeText(activity, " 基本資料 ACK", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (Arrays.equals(data,  AnalysisObject.getResponseDataError())) {
                        Toast.makeText(activity, " 基本資料 NACK", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (dataResultHandler != null) {
                        dataResultHandler.handler(dataToString(data));
                    }

                    if (analysisObject == null) {
                        analysisObject = new  AnalysisObject();
                    }

                    int analysisErrorCode = analysisObject.analysisResult(data);

                    if (analysisErrorCode ==  AnalysisErrorCode.AnalysisOK) {
                        Log.i("user", "analysisErrorCode" + analysisErrorCode);
                        if (resultHandler != null) {
                            resultHandler.handler(analysisObject);
                            commService.setWriteBluetoothGatt(Command.getDataReceivedOK());
                            analysisObject = null;
                            analysisObject = new  AnalysisObject();
                        }
                    } else if (analysisErrorCode ==  AnalysisErrorCode.getAnalysisDataGroupOK()) {
                        Log.i("user", "analysisErrorCode:" + "getDataReceivedOK" + analysisErrorCode);
                        commService.setWriteBluetoothGatt(Command.getDataReceivedOK());

                    } else if (analysisErrorCode ==  AnalysisErrorCode.getAnalysisWeightOK()) {
                        if (resultWeightHandler != null) {
                            resultWeightHandler.handler(analysisObject.getWeight());
                        }
                        commService.setWriteBluetoothGatt(Command.getDataReceivedOK());
                    }

                } else {
                    Log.i("DEBUG", "==== Other ACTION ====" + action);
                }
            }

        };

        String dataToString(byte[] data) {
            final StringBuilder stringBuilder = new StringBuilder(data.length);

            if (data != null && data.length > 0) {

                for (byte byteChar : data) {
                    stringBuilder.append(String.format("%02X ", byteChar));
                }

            }
            return stringBuilder.toString();
        }

        //==================================broadcastReceiver=================================


        private IntentFilter makeGattUpdateIntentFilter() {
            Log.i("DEBUG", "   == makeGattUpdateIntentFilter==");

            final IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(CommService.ACTION_GATT_CONNECTED);
            intentFilter.addAction(CommService.ACTION_GATT_DISCONNECTED);
            intentFilter.addAction(CommService.ACTION_GATT_SERVICES_DISCOVERED);
            intentFilter.addAction(CommService.ACTION_DATA_AVAILABLE);
            return intentFilter;
        }

        //==================================setCommand=================================

        public void sendDataReceivedOK() {
            if (commService.setWriteCharacteristic != null) {
                commService.setWriteBluetoothGatt(Command.getDataReceivedOK());
            }
        }


        public interface ConnectHandler {
            void handler(Boolean isConnect);
        }

        public interface DiscoverHandler {
            void handler(BluetoothDevice device);
        }

        public interface ResultHandler {
            void handler( AnalysisObject analysisObject);
        }

        public interface DataResultHandler {
            void handler(String data);
        }

        public interface ResultWeightHandler {
            void handler(float data);
        }
    }

    //==================================scaleComm=================================


    private BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                Log.i(TAG, " GATT server open");
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                bluetoothGatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");

                broadcastUpdate(intentAction);
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                didDiscoverCharacteristics(gatt.getServices());
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
//            Log.i ( "DEBUG" , "=讀取= " );
            String str_uuid = characteristic.getUuid().toString();
            str_uuid = str_uuid.toUpperCase();
//            Log.i ( "DEBUG" , "== onCharacteristicChanged: uuid= " + str_uuid );
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }

        //======================================onCharacteristicChanged=============================

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
//            Log.i ( "DEBUG" , "=收到廣播= " );
            String str_uuid = characteristic.getUuid().toString();
            str_uuid = str_uuid.toUpperCase();
//            Log.i ( "DEBUG" , "== onCharacteristicChanged: uuid= " + str_uuid );
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }

        //======================================onCharacteristicChanged=============================

    };

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void didDiscoverCharacteristics(List<BluetoothGattService> gattServices) {
        for (BluetoothGattService gattService : gattServices) {

            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();

            for (BluetoothGattCharacteristic bluetoothGattCharacteristic : gattCharacteristics) {
                UUID uuid = bluetoothGattCharacteristic.getUuid();
                Log.i("uuid", uuid.toString());
                //for
                if (bluetoothGattCharacteristic.getUuid().toString().equals( AnalysisObject.getSetIndicateUuid())) {
                    setIndicateCharacteristic = bluetoothGattCharacteristic;
                }
                if (bluetoothGattCharacteristic.getUuid().toString().equals( AnalysisObject.getSetWriteUuid())) {
                    setWriteCharacteristic = bluetoothGattCharacteristic;
                }

            }
            broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
        }
    }

    //==================================setNotifications===========================================================
    public void setNotifitice() {

        setNotifications(setIndicateCharacteristic);

    }

    public void setNotifications(BluetoothGattCharacteristic setNotificationsCharacteristic) {
        if (bluetoothGatt == null) {
            if (bluetoothDevice == null) {
                return;
            }
            connect(bluetoothDevice);
            return;
        }
        Log.i("ENABLEVALUE", "   == setbluetoothGattCharacteristic==" + setNotificationsCharacteristic.getUuid().toString());
        boolean isNotification, isCommandNotification;
        isNotification = bluetoothGatt.setCharacteristicNotification(setNotificationsCharacteristic, true);
//        Log.i ( "ENABLEVALUE", "=isNotification=" + isNotification );
        if (isNotification) {
            for (BluetoothGattDescriptor bd : setNotificationsCharacteristic.getDescriptors()) {
                if ((setNotificationsCharacteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_INDICATE) != 0) {
                    bd.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                } else if ((setNotificationsCharacteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0) {
                    bd.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//                    Log.i ( "ENABLEVALUE", "=ENABLE_NOTIFICATION_VALUE="  );
                }
                bluetoothGatt.writeDescriptor(bd);
            }
        }

    }

    public void readDeviceInformationService() {
        if (deviceInformationService != null) {
            List<BluetoothGattCharacteristic> gattCharacteristics = deviceInformationService.getCharacteristics();
            for (BluetoothGattCharacteristic bluetoothGattCharacteristic : gattCharacteristics) {
                bluetoothGatt.readCharacteristic(bluetoothGattCharacteristic);
            }
        }
//        BluetoothGattCharacteristic setWriteCharacteristic = new BluetoothGattCharacteristic()
//        bluetoothGatt.readCharacteristic (  )
    }

    public void setWriteBluetoothGatt(byte[] command) {
        if (setWriteCharacteristic == null) {
            return;
        }
        setWriteCharacteristic.setValue(command);
        bluetoothGatt.writeCharacteristic(setWriteCharacteristic);
    }


    private void broadcastUpdate(final String action) {
        Log.i("DEBUG", "==broadcastUpdate action=" + action);

        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {

        Log.i("DEBUG", "==broadcastUpdate action=" + action + " char = " + characteristic);

        final Intent intent = new Intent(action);

        final byte[] data = characteristic.getValue();
        if (data != null && data.length > 0) {
            final StringBuilder stringBuilder = new StringBuilder(data.length);

            for (byte byteChar : data) {
                stringBuilder.append(String.format("%02X ", byteChar));
            }

            Log.i("DEBUG", "==stringBuilder=" + action + " stringBuilder = " + stringBuilder.toString());
            intent.putExtra(EXTRA_DATA, data);
        }
        sendBroadcast(intent);
    }

    public class LocalBinder extends Binder {
        CommService getService() {
            Log.i("DEBUG", "==getService==");
            return CommService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("DEBUG", "==onBind==");
        return new LocalBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        close();
        return super.onUnbind(intent);
    }

    public void connect(final BluetoothDevice device) {
        Log.i("DEBUG", "==BluetoothLeSevices: connect == " + device);
        bluetoothDevice = device;
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return;
        }
        //
        if (bluetoothGatt != null) {
            Log.i("DEBUG", " ----- mBluetoothGatt != null. must close before connect");
            bluetoothGatt.close();  //
        }
        //
        Log.i("DEBUG", " ----- connectGatt");
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        bluetoothGatt = device.connectGatt(this, false, bluetoothGattCallback);

        Log.d(TAG, "Trying to create a new connection.");
        mConnectionState = STATE_CONNECTING;
    }

    public void disconnect() {
        Log.i("DEBUG", "== BluetoothLeService: disconnect==");
        if (bluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        bluetoothGatt.disconnect();
    }

    //-----------------------------------------------------------------
    public void close() {
        Log.i("DEBUG", "== BluetoothLeService: close==");

        if (bluetoothGatt == null) {
            return;
        }
        bluetoothGatt.close();
        bluetoothGatt = null;
    }

}
