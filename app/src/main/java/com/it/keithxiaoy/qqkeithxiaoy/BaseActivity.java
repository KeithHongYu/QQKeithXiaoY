package com.it.keithxiaoy.qqkeithxiaoy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.it.keithxiaoy.qqkeithxiaoy.util.Constant;
import com.it.keithxiaoy.qqkeithxiaoy.util.ToastUtils;

/**
 * Created by xiaoY on 2017/3/1.
 */

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mSharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
    }

    public void startActivity(Class clazz, Boolean isFinish){
        startActivity(new Intent(this,clazz));
        if (isFinish){
            finish();
        }
    }

    public void showToast(String msg){
        ToastUtils.showToast(this,msg);
    }

    public void showDialog(String msg){
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }
    public void hideDialog(){
        mProgressDialog.dismiss();
    }

    public void saveUser(String username,String password){
        mSharedPreferences.edit().putString(Constant.SP_KEY_USERNAME,username)
                .putString(Constant.SP_KEY_PASSWORD,password)
                .commit();

    }

    public String getUsername(){
        return mSharedPreferences.getString(Constant.SP_KEY_USERNAME,"");
    }
    public String getUserPassword(){
        return mSharedPreferences.getString(Constant.SP_KEY_PASSWORD,"");
    }

}
