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

import com.charder.roomdemo.common.Common;
import com.charder.roomdemo.room.entity.MeasuredPerson;
import com.charder.roomdemo.room.entity.MeasurementData;
import com.charder.roomdemo.room.entity.MeasurementFunc;

import org.jetbrains.annotations.NotNull;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MeasuredPersonFragment extends Fragment {

    TextView tv_idName , tv_lastDate;
    Button bt_addMP , bt_setting;
    RecyclerView rv_MeasuredPerson;
    MeasuredPersonAdapter measuredPersonAdapter;
    ArrayList<MeasuredPerson> measuredPersonArrayList = new ArrayList<>();

    Activity activity;

    Collator collator = Collator.getInstance();

    boolean isASC = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_measured_person, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        rv_MeasuredPerson = view.findViewById(R.id.rv_MeasuredPerson);
        rv_MeasuredPerson.setLayoutManager(new LinearLayoutManager(activity));
        measuredPersonAdapter = new MeasuredPersonAdapter(activity,measuredPersonArrayList);
        rv_MeasuredPerson.setAdapter(measuredPersonAdapter);
        getMP();
        bt_addMP = view.findViewById(R.id.bt_addMP);
        bt_addMP.setOnClickListener( v -> {
            Navigation.findNavController(v).navigate(R.id.action_measuredPersonFragment_to_addMeasuredPersonFragment);
        });
        bt_setting = view.findViewById(R.id.bt_setting);
        bt_setting.setOnClickListener( v -> {
            Navigation.findNavController(v).navigate(R.id.action_measuredPersonFragment_to_settingFragment);
        });
        tv_idName = view.findViewById(R.id.tv_idName);
        tv_idName.setOnClickListener( v -> {
            Collections.sort(measuredPersonArrayList, (o1, o2) -> collator.compare(o1.getName() , o2.getName()));
            measuredPersonAdapter.notifyDataSetChanged();
        });
        tv_lastDate = view.findViewById(R.id.tv_lastDate);
        tv_lastDate.setOnClickListener( v -> {
            if (isASC){
                isASC = false;
                Collections.sort(measuredPersonArrayList , (o1, o2) ->{
                    Date dateO1 = o1.getLastDate();
                    Date dateO2 = o2.getLastDate();
                    if (o1.getLastDate() == null){
                        dateO1 = new Date(0);
                    }
                    if (o2.getLastDate() == null){
                        dateO2 = new Date(0);
                    }
                    return dateO2.compareTo(dateO1);
                });
            }else {
                isASC = true;
                Collections.sort(measuredPersonArrayList , (o1, o2) ->{
                    Date dateO1 = o1.getLastDate();
                    Date dateO2 = o2.getLastDate();
                    if (o1.getLastDate() == null){
                        dateO1 = new Date();
                    }
                    if (o2.getLastDate() == null){
                        dateO2 = new Date();
                    }
                    return dateO1.compareTo(dateO2);
                });
            }

            measuredPersonAdapter.notifyDataSetChanged();
        });
    }

    void getMP() {
        try {
            AsyncTask.execute( () -> {
                List<MeasuredPerson> measuredPeople;
                int permission = Common.currentAccount.getPermission();
                if (permission == 0){ // Admin 可以看到所有受測者資料
                    measuredPeople = Common.db.measuredPersonDao().getAllMP();
                }else if (permission == 1){ // 管理者 可以看到自己及所有使用者受測者資料
                    measuredPeople = Common.db.measuredPersonDao().getPermission1MP(Common.currentAccount.getId());
                }else { // 使用者 可以看到自己受測者資料
                    measuredPeople = Common.db.measuredPersonDao().getPermission2MP(Common.currentAccount.getId());
                }
                measuredPersonArrayList.clear();
                for (MeasuredPerson m : measuredPeople){
                    MeasurementData data = Common.db.measurementDataDao().getLastData(m.getId());
                    if (data != null){
                        m.setLastDate(data.getDate());
                        MeasurementFunc posture = Common.db.measurementFuncDao().getPostureData(data.getId());
                        if (posture != null){
                            m.setLastX10(posture.getNumberX10());
                        }
                    }
                }
                measuredPersonArrayList.addAll(measuredPeople);
                Collections.sort(measuredPersonArrayList, (o1, o2) -> collator.compare(o1.getName() , o2.getName()));

                activity.runOnUiThread(() -> {
                    measuredPersonAdapter.notifyDataSetChanged();
                });
            });
        }catch (Exception e){
            Log.e("Exception" , e.getLocalizedMessage());
        }
    }
    class MeasuredPersonAdapter extends RecyclerView.Adapter<MeasuredPersonAdapter.MeasuredPersonViewHolder>{
        Activity activity;
        ArrayList<MeasuredPerson> measuredPersonArrayList;
        MeasuredPersonAdapter(Activity activity , ArrayList<MeasuredPerson> measuredPersonArrayList){
            this.activity = activity;
            this.measuredPersonArrayList = measuredPersonArrayList;
        }
        class MeasuredPersonViewHolder extends RecyclerView.ViewHolder{
            TextView tv_id , tv_name ,tv_lastDate , tv_posture;
            public MeasuredPersonViewHolder( @NotNull View itemView) {
                super(itemView);
                tv_id = itemView.findViewById(R.id.tv_id);
                tv_name = itemView.findViewById(R.id.tv_name);
                tv_lastDate = itemView.findViewById(R.id.tv_lastDate);
                tv_posture = itemView.findViewById(R.id.tv_posture);
            }
        }
        @Override
        public MeasuredPersonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            return new MeasuredPersonViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_mp, viewGroup, false));
        }
        @Override
        public void onBindViewHolder( MeasuredPersonAdapter.MeasuredPersonViewHolder holder, int position) {
            MeasuredPerson measuredPerson = measuredPersonArrayList.get(position);
            holder.tv_id.setText(measuredPerson.getIdCode());
            holder.tv_name.setText(measuredPerson.getName());
            Date last = measuredPerson.getLastDate();
            if (last != null){
                holder.tv_lastDate.setText(Common.dateFormatFull(last));
            }else{
                holder.tv_lastDate.setText("");
            }
            holder.itemView.setOnClickListener( v -> {
                Bundle bundle = new Bundle();
                bundle.putInt("MeasuredPersonId" ,  measuredPerson.getId());

                Navigation.findNavController(v).navigate(R.id.action_measuredPersonFragment_to_dataFragment , bundle);
            });
        }
        @Override
        public int getItemCount() {
            return measuredPersonArrayList.size();
        }
    }
}

//    void getAllMeasureMP() {
//        try {
//            AsyncTask.execute( () -> {
//                List<MeasuredPerson> measuredPeople = Common.db.measuredPersonDao().getAllMP();
//                measuredPersonArrayList.clear();
//                measuredPersonArrayList.addAll(measuredPeople);
//                activity.runOnUiThread(() -> {
//                    measuredPersonAdapter.notifyDataSetChanged();
//                });
//            });
//        }catch (Exception e){
//            Log.e("Exception" , e.getLocalizedMessage());
//        }
//    }