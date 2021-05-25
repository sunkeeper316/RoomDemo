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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class MeasuredPersonFragment extends Fragment {

    TextView tv_idName , tv_lastDate;
    Button bt_addMP , bt_setting;
    RecyclerView rv_MeasuredPerson;
    MeasuredPersonAdapter measuredPersonAdapter;
    ArrayList<MeasuredPerson> measuredPersonArrayList = new ArrayList<>();

    Activity activity;

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
//        getAllMeasureMP();
        getMP();
        bt_addMP = view.findViewById(R.id.bt_addMP);
        bt_addMP.setOnClickListener( v -> {
            Navigation.findNavController(v).navigate(R.id.action_measuredPersonFragment_to_addMeasuredPersonFragment);
        });
        bt_setting = view.findViewById(R.id.bt_setting);
        bt_setting.setOnClickListener( v -> {
            Navigation.findNavController(v).navigate(R.id.action_measuredPersonFragment_to_settingFragment);
        });
    }
    void getAllMeasureMP() {
        try {
            AsyncTask.execute( () -> {
                List<MeasuredPerson> measuredPeople = Common.db.measuredPersonDao().getAllMP(Common.currentAccount.getId());
                measuredPersonArrayList.clear();
                measuredPersonArrayList.addAll(measuredPeople);
                activity.runOnUiThread(() -> {
                    measuredPersonAdapter.notifyDataSetChanged();
                });
            });
        }catch (Exception e){
            Log.e("Exception" , e.getLocalizedMessage());
        }
    }
    void getMP() {
        try {
            AsyncTask.execute( () -> {
                List<MeasuredPerson> measuredPeople = Common.db.accountDao().getMeasuredPerson(Common.currentAccount.getId());
                measuredPersonArrayList.clear();
                measuredPersonArrayList.addAll(measuredPeople);
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
        }
        @Override
        public int getItemCount() {
            return measuredPersonArrayList.size();
        }
    }
}