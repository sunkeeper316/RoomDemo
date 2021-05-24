package com.charder.roomdemo;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.charder.roomdemo.room.AppDataBase;
import com.charder.roomdemo.room.entity.MeasuredPerson;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class AddMeasuredPersonFragment extends Fragment {

    EditText et_idCode , et_name , et_birthday , et_height;
    Button bt_save ;

    RadioGroup radioGroup;
    
    Activity activity;

    AppDataBase db;

    MeasuredPerson measuredPerson = new MeasuredPerson();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_measured_person, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        et_idCode = view.findViewById(R.id.et_idCode);
        int randomId = (int) (Math.random() * 1000000);
        et_idCode.setText(String.valueOf(randomId));
        et_name = view.findViewById(R.id.et_name);
        et_birthday = view.findViewById(R.id.et_birthday);
        et_height = view.findViewById(R.id.et_height);

        bt_save = view.findViewById(R.id.bt_save);
        bt_save.setOnClickListener( v -> {
            String height = et_height.getText().toString().trim();
            double dHeight = Double.valueOf(height) * 10;
            measuredPerson.setCreateTime(new Date());
            measuredPerson.setAccount_id(Common.currentAccount.getId());
            measuredPerson.setGender(true);
            measuredPerson.setName(et_name.getText().toString().trim());
            measuredPerson.setHeightX10((int) dHeight);
            measuredPerson.setIdCode(et_idCode.getText().toString().trim());

        });
    }
}