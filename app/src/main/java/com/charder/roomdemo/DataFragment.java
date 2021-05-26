package com.charder.roomdemo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.charder.roomdemo.BLE.AnalysisObject;
import com.charder.roomdemo.common.Common;
import com.charder.roomdemo.common.Device;
import com.charder.roomdemo.common.Function;
import com.charder.roomdemo.room.entity.MeasuredPerson;
import com.charder.roomdemo.room.entity.MeasurementData;
import com.charder.roomdemo.room.entity.MeasurementFunc;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DataFragment extends Fragment {

    int measuredPersonId;

    Activity activity;

    TextView tv_last,tv_next,tv_date;
    RecyclerView rv_data;
    FunctionAdapter functionAdapter;
    Button bt_random;

    MeasurementData currentData ;

    List<MeasurementData> measurementDataList;
    ArrayList<MeasurementFunc> showFuncs = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        Bundle bundle = getArguments();
        if (bundle != null){
            measuredPersonId = bundle.getInt("MeasuredPersonId" , 0);
        }
        tv_last = view.findViewById(R.id.tv_last);
        tv_last.setOnClickListener( v -> {
            int index = measurementDataList.indexOf(currentData);
            if (index - 1 > -1){
                currentData = measurementDataList.get(index - 1);
                getFunc();
            }else{
                Toast.makeText(activity,"沒有資料了！",Toast.LENGTH_SHORT).show();
            }

        });
        tv_next = view.findViewById(R.id.tv_next);
        tv_next.setOnClickListener( v -> {
            int index = measurementDataList.indexOf(currentData);
            if (index + 1 < measurementDataList.size()){
                currentData = measurementDataList.get(index + 1);
                getFunc();
            }else{
                Toast.makeText(activity,"沒有資料了！",Toast.LENGTH_SHORT).show();
            }

        });
        tv_date = view.findViewById(R.id.tv_date);
        rv_data = view.findViewById(R.id.rv_data);
        rv_data.setLayoutManager( new LinearLayoutManager(activity));
        functionAdapter = new FunctionAdapter(activity,showFuncs);
        rv_data.setAdapter(functionAdapter);
        bt_random = view.findViewById(R.id.bt_random);
        bt_random.setOnClickListener( v -> {
            randomData();
        });
        getData();
    }
    private void getData(){
        AsyncTask.execute(() -> {
            try{
                measurementDataList = Common.db.measurementDataDao().getAllData(measuredPersonId);
                currentData = measurementDataList.get(measurementDataList.size() - 1);

                getFunc();
            }catch (Exception e){
                Log.e("Exception",e.getLocalizedMessage());
            }
        });
    }
    private void getFunc(){
        AsyncTask.execute(() -> {
            try{
                int dataId = currentData.getId();
                List<MeasurementFunc> measurementFuncList = Common.db.measurementFuncDao().getAllData(dataId);
                showFuncs.clear();
                for (MeasurementFunc m : measurementFuncList){
                    if (m.getFunc_id() == 0 ||m.getFunc_id() == 1 || m.getFunc_id() == 2|| m.getFunc_id() == 3|| m.getFunc_id() == 8|| m.getFunc_id() == 14|| m.getFunc_id() == 15|| m.getFunc_id() == 16|| m.getFunc_id() == 17|| m.getFunc_id() == 18|| m.getFunc_id() == 19|| m.getFunc_id() == 20){
                        showFuncs.add(m);
                    }
                }
                activity.runOnUiThread(()->{
                    tv_date.setText(Common.dateFormatFull(currentData.getDate()));
                    functionAdapter.notifyDataSetChanged();
                });

            }catch (Exception e){
                Log.e("Exception",e.getLocalizedMessage());
            }
        });
    }

    private void randomData(){
        AsyncTask.execute(() -> {
            try {
                MeasurementData measurementData = new MeasurementData();
                measurementData.setDeviceId(0);//暫定0 因為只有U310
                measurementData.setDate(new Date());
                measurementData.setMP_id(measuredPersonId);
                List<Long> ids = Common.db.measurementDataDao().insert(measurementData);
                Log.e("measurementData","insert id "+ ids.get(0));
                Log.e("measurementData","insert id "+ ids.get(0).intValue());
                MeasurementFunc[] measurementFuncs = Device.U310Func( ids.get(0).intValue());
                //(int)(Math.random() * (Y-X+1)) + X;
                measurementFuncs[0].setNumberX10(800 + (int)(Math.random() * 10));
                measurementFuncs[1].setNumberX10(200 + (int)(Math.random() * 10));
                measurementFuncs[2].setNumberX10(200 + (int)(Math.random() * 10));
                measurementFuncs[3].setNumberX10(0);
                measurementFuncs[4].setNumberX10(0);
                measurementFuncs[5].setNumberX10(0);
                measurementFuncs[6].setNumberX10(0);
                measurementFuncs[7].setNumberX10(0);
                measurementFuncs[8].setNumberX10(500 + (int)(Math.random() * 10));
                measurementFuncs[9].setNumberX10(0);
                measurementFuncs[10].setNumberX10(0);
                measurementFuncs[11].setNumberX10(0);
                measurementFuncs[12].setNumberX10(0);
                measurementFuncs[13].setNumberX10(0);
                measurementFuncs[14].setNumberX10(15000);
                measurementFuncs[15].setNumberX10(5);
                measurementFuncs[16].setNumberX10(30);
                measurementFuncs[17].setNumberX10(90);
                measurementFuncs[18].setNumberX10(300);
                measurementFuncs[19].setNumberX10(500+ (int)(Math.random() * 10));
                measurementFuncs[20].setNumberX10(10);
                Common.db.measurementFuncDao().insert(measurementFuncs);
                getData();

            }catch (Exception e){
                Log.e("Exception",e.getLocalizedMessage());
            }
        });
    }


    class FunctionAdapter extends RecyclerView.Adapter<FunctionAdapter.FunctionViewHolder>{
        Activity activity;
        ArrayList<MeasurementFunc> measurementFuncs;
        FunctionAdapter(Activity activity , ArrayList<MeasurementFunc> measurementFuncs){
            this.activity = activity;
            this.measurementFuncs = measurementFuncs;
        }
        class FunctionViewHolder extends RecyclerView.ViewHolder{
            TextView tv_funcName , tv_Number ;
            public FunctionViewHolder( @NotNull View itemView) {
                super(itemView);
                tv_funcName = itemView.findViewById(R.id.tv_funcName);
                tv_Number = itemView.findViewById(R.id.tv_Number);
            }
        }
        @Override
        public FunctionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            return new FunctionViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_func, viewGroup, false));
        }
        @Override
        public void onBindViewHolder(FunctionViewHolder holder, int position) {
            MeasurementFunc measurementFunc = measurementFuncs.get(position);
            String name = Function.getFunc(measurementFunc.getFunc_id()).getFuncName();
            String unit = Function.getFunc(measurementFunc.getFunc_id()).getUnit();
            double number = (double) measurementFunc.getNumberX10();
            holder.tv_funcName.setText(name);
            holder.tv_Number.setText((number / 10) + " " + unit);
        }
        @Override
        public int getItemCount() {
            return measurementFuncs.size();
        }
    }

}