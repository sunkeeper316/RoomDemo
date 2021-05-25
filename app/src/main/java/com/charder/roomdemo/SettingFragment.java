package com.charder.roomdemo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.charder.roomdemo.common.Common;
import com.charder.roomdemo.room.entity.Account;
import com.charder.roomdemo.room.entity.MeasuredPerson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class SettingFragment extends Fragment {

    RecyclerView rv_account;
    AccountAdapter accountAdapter;
    Activity activity;

    ArrayList<Account> accounts = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        rv_account = view.findViewById(R.id.rv_account);
        rv_account.setLayoutManager(new LinearLayoutManager(activity));
        accounts.add(Common.currentAccount);
        accountAdapter = new AccountAdapter(activity , accounts);
        rv_account.setAdapter(accountAdapter);
        if (Common.currentAccount.getPermission() == 1){
            getUserAccount();
        }else if (Common.currentAccount.getPermission() == 0){
            getAllAccount();
        }
    }
    void getUserAccount() {
        try {
            AsyncTask.execute( () -> {
                List<Account> accountList = Common.db.accountDao().getUserAccount();
                accounts.addAll(accountList);
                activity.runOnUiThread(() -> {
                    accountAdapter.notifyDataSetChanged();
                });
            });
        }catch (Exception e){
            Log.e("Exception" , e.getLocalizedMessage());
        }
    }
    void getAllAccount() {
        try {
            AsyncTask.execute( () -> {
                List<Account> accountList = Common.db.accountDao().getAll();
                accounts.addAll(accountList);
                activity.runOnUiThread(() -> {
                    accountAdapter.notifyDataSetChanged();
                });
            });
        }catch (Exception e){
            Log.e("Exception" , e.getLocalizedMessage());
        }
    }
    class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewholder>{
        Activity activity;
        ArrayList<Account> accounts;
        AccountAdapter(Activity activity , ArrayList<Account> accounts){
            this.activity = activity;
            this.accounts = accounts;
        }
        @Override
        public AccountViewholder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int viewType) {
            return new AccountViewholder(LayoutInflater.from(activity).inflate(R.layout.item_account ,viewGroup , false ));
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull SettingFragment.AccountAdapter.AccountViewholder holder, int position) {
            Account account = accounts.get(position);
            holder.tv_Account.setText(account.getAccount());
            holder.tv_createTime.setText(Common.dateFormatTime(account.getCreateTime()));
            int permission = account.getPermission();
            if (permission == 1){
                holder.tv_Permission.setText("管理者");
            }else if (permission == 2){
                holder.tv_Permission.setText("使用者");
            }
        }

        @Override
        public int getItemCount() {
            return accounts.size();
        }

        class AccountViewholder extends RecyclerView.ViewHolder {

            TextView tv_Account , tv_Permission , tv_createTime;
            public AccountViewholder( @NotNull View itemView) {
                super(itemView);
                tv_Account = itemView.findViewById(R.id.tv_Account);
                tv_Permission = itemView.findViewById(R.id.tv_Permission);
                tv_createTime = itemView.findViewById(R.id.tv_createTime);
            }
        }
    }
}