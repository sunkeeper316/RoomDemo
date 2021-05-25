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
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.charder.roomdemo.common.Common;
import com.charder.roomdemo.room.entity.Account;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Date;


public class RegisterFragment extends Fragment {

    EditText et_account , et_password , et_permission;
    Button bt_register ;

    Activity activity;

    Account newAccount = new Account();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        et_account = view.findViewById(R.id.et_account);
        et_password = view.findViewById(R.id.et_password);
        et_permission = view.findViewById(R.id.et_permission);
        et_permission.setFocusable(false);
        et_permission.setOnClickListener( v -> {

            OptionsPickerView<Object> picker = new OptionsPickerBuilder(activity, (options1, options2, options3, v1) -> {
                et_permission.setText(Common.permissions[options1]);
                newAccount.setPermission(options1 + 1);
            })
                    .isDialog(true)
                    .isCenterLabel(true)
                    .setTitleSize(24)
                    .setCancelText("取消")
                    .setSubmitText("確定")
                    .setContentTextSize(24)
                    .build();
            picker.setPicker(Arrays.asList(Common.permissions));

            picker.show();
        });
        bt_register = view.findViewById(R.id.bt_register);
        bt_register.setOnClickListener( v -> {
            if (et_permission.getText().toString().trim().equals("")){
                Toast.makeText(activity , "請選擇權限" , Toast.LENGTH_SHORT).show();
                return;
            }
            String account = et_account.getText().toString().trim();
            String password = et_password.getText().toString().trim();
            AsyncTask.execute( () -> {
                if (checkAccount(account)){
                    activity.runOnUiThread(() -> {
                        Toast.makeText(activity,"帳號已存在",Toast.LENGTH_SHORT).show();
                    });
                    return;
                }
                newAccount.setAccount(account);
                newAccount.setPassword(password);
                newAccount.setCreateTime(new Date());
                if (Common.db != null){
                    try {
                        Common.db.accountDao().insert(newAccount);
                        activity.runOnUiThread( () -> {
                            Navigation.findNavController(v).popBackStack();
                        });
                    }catch (Exception e){
                        Log.e("Exception",e.getLocalizedMessage());
                    }
                }
            });
        });
    }

    Boolean checkAccount(String account){
        try {
            Account checkAccount = Common.db.accountDao().findByAccount(account);
            if (checkAccount != null){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            Log.e("Exception",e.getLocalizedMessage());
            return false;
        }
    }
}