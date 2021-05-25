package com.charder.roomdemo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.charder.roomdemo.common.Common;
import com.charder.roomdemo.preferences.Preferences;
import com.charder.roomdemo.room.AppDataBase;
import com.charder.roomdemo.room.entity.Account;

import java.util.Date;
import java.util.List;

public class MainFragment extends Fragment {

    EditText et_account , et_password ;
    Button bt_register , bt_signIn;

    Activity activity;

    List<Account> accountList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        if (activity != null){
            Common.db = Room.databaseBuilder(activity,AppDataBase.class,Common.BodyCompositionDb).build();
        }
        setAdmin();
        et_account = view.findViewById(R.id.et_account);
        et_password = view.findViewById(R.id.et_password);
        bt_register = view.findViewById(R.id.bt_register);

        et_account.setText(Preferences.loadAccount(activity));
        et_password.setText(Preferences.loadPassword(activity));

        bt_signIn = view.findViewById(R.id.bt_signIn);
        bt_register.setOnClickListener( v -> {
            Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_registerFragment);
        });
        bt_signIn.setOnClickListener( v -> {
            String account = et_account.getText().toString().trim();
            String password = et_password.getText().toString().trim();
            AsyncTask.execute(() -> {
                boolean checkLogin = checkAccount(account , password);
                activity.runOnUiThread( () -> {
                    if (checkLogin){
                        Preferences.saveAccount(activity , account);
                        Preferences.savePassword(activity , password);
                        Toast.makeText(activity , "登入成功" , Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_measuredPersonFragment);
                    }else{
                        Toast.makeText(activity , "帳號密碼錯誤" , Toast.LENGTH_SHORT).show();
                    }
                });
            });

        });
    }

    void setAdmin(){
        try {
            AsyncTask.execute( () -> {
                Account checkAccount = Common.db.accountDao().findByAccount("Admin");
                if (checkAccount == null){
                    Account admin = new Account();
                    admin.setCreateTime(new Date());
                    admin.setPermission(0);
                    admin.setAccount("Admin");
                    admin.setPassword("123456");
                    Common.db.accountDao().insert(admin);
                }
            });
        }catch (Exception e){
            Log.e("Exception" , e.getLocalizedMessage());
        }
    }

    void getAllAccount() {
        try {
            AsyncTask.execute( () -> {
                accountList = Common.db.accountDao().getAll();
                for(Account a : accountList){
                    Log.e("Account" , a.getAccount() + " : " + a.getPassword());
                }
            });
        }catch (Exception e){
            Log.e("Exception" , e.getLocalizedMessage());
        }
    }

    Boolean checkAccount(String account , String password){
        try {
            Common.currentAccount = Common.db.accountDao().loginCheck(account , password);
            if (Common.currentAccount != null){
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