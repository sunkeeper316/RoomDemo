package com.charder.roomdemo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.charder.roomdemo.common.Common;
import com.charder.roomdemo.room.entity.MeasuredPerson;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddMeasuredPersonFragment extends Fragment {

    EditText et_idCode , et_name , et_birthday , et_height;
    Button bt_save ;

    RadioGroup radioGroup;

    Activity activity;

    MeasuredPerson measuredPerson = new MeasuredPerson();

    final private String[] nameList = {"小明","小一","小七","小七當","小七七","阿美","阿明","阿明哥","girl","阿明","英雄","壞人","sun","boy"};
    // 0 - 13
    //(int)(Math.random() * (Y-X+1)) + X;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_measured_person, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        et_idCode = view.findViewById(R.id.et_idCode);
        int randomId = (int) (Math.random() * 1000000);
        et_idCode.setText(String.valueOf(randomId));
        et_name = view.findViewById(R.id.et_name);
        int r = (int)(Math.random() * 14);
        et_name.setText(nameList[r]);
        et_birthday = view.findViewById(R.id.et_birthday);
        et_height = view.findViewById(R.id.et_height);

        radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener( (group , checkedId) -> {
            Log.e("checkedId","checkedId : "+checkedId);
            if (checkedId == R.id.rb_male){
                measuredPerson.setGender(true);
            }else{
                measuredPerson.setGender(false);
            }
        });

        bt_save = view.findViewById(R.id.bt_save);
        bt_save.setOnClickListener( v -> {
            String height = et_height.getText().toString().trim();
            String bir = et_birthday.getText().toString().trim();
            double dHeight = Double.valueOf(height) * 10;
            measuredPerson.setCreateTime(new Date());
            measuredPerson.setAccount_id(Common.currentAccount.getId());
            measuredPerson.setName(et_name.getText().toString().trim());
            measuredPerson.setHeightX10((int) dHeight);
            measuredPerson.setIdCode(et_idCode.getText().toString().trim());
            measuredPerson.setBirthday(Common.dateFormatTime(bir));
            try {
                AsyncTask.execute(() -> {
                    Common.db.measuredPersonDao().insert(measuredPerson);
                    activity.runOnUiThread( () -> {
                        Navigation.findNavController(v).popBackStack();
                    });
                });
            }catch (Exception e){
                Log.e("Exception",e.getLocalizedMessage());
            }
        });
    }

}