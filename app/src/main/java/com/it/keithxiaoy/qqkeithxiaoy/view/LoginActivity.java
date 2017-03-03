package com.it.keithxiaoy.qqkeithxiaoy.view;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.it.keithxiaoy.qqkeithxiaoy.BaseActivity;
import com.it.keithxiaoy.qqkeithxiaoy.MainActivity;
import com.it.keithxiaoy.qqkeithxiaoy.R;
import com.it.keithxiaoy.qqkeithxiaoy.presenter.LoginPresenter;
import com.it.keithxiaoy.qqkeithxiaoy.presenter.LoginPresenterImpl;
import com.it.keithxiaoy.qqkeithxiaoy.util.StringUtils;
import com.it.keithxiaoy.qqkeithxiaoy.view.activity.RegistActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginView, TextView.OnEditorActionListener {

    private static final int REQUEST_SDCARD = 1;
    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.til_username)
    TextInputLayout mTilUsername;
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.til_pwd)
    TextInputLayout mTilPwd;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.tv_newUser)
    TextView mTvNewUser;

    private LoginPresenter mLoginPresenter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        /**
         * 数据回显
         */
        String username = getUsername();
        String userPassword = getUserPassword();
        mEtUsername.setText(username);
        mEtPwd.setText(userPassword);


        mEtPwd.setOnEditorActionListener(this);
        mLoginPresenter = new LoginPresenterImpl(this);



    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        /**
         * 数据回显
         */
        String username = getUsername();
        String userPassword = getUserPassword();
        mEtUsername.setText(username);
        mEtPwd.setText(userPassword);
    }

    @OnClick({R.id.btn_login, R.id.tv_newUser})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_newUser:
                startActivity(RegistActivity.class,false);
                break;
        }
    }

    private void login() {
        /**
         * 1. 先获取用户名和密码
         * 2. 对数据合法性校验
         * 3. 如果通过了则提交给服务器 (P 层)
         */
        String username = mEtUsername.getText().toString().trim();
        String pwd = mEtPwd.getText().toString().trim();
        if (!StringUtils.checkUsername(username)) {
            mEtUsername.requestFocus(View.FOCUS_RIGHT);
            mTilUsername.setErrorEnabled(true);
            mTilUsername.setError("用户名不合法");
            showToast("用户名不合法");
            return;
        } else {
            mTilUsername.setErrorEnabled(false);
        }
        if (!StringUtils.checkPwd(pwd)) {
            mEtPwd.requestFocus(View.FOCUS_RIGHT);
            mTilPwd.setErrorEnabled(true);
            mTilPwd.setError("密码不合法");
            showToast("密码不合法");
            return;
        } else {
            mTilPwd.setErrorEnabled(false);
        }

        //申请权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PermissionChecker.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_SDCARD);

            return;
        }
        
        //显示进度条
        showDialog("正在登录中...");
        //提交服务器
        mLoginPresenter.login(username, pwd);

    }

    @Override
    public void afterLogin(Boolean isSuccess, String msg, String username, String pwd) {
        hideDialog();
        /**
         * 登录成功，保存用户名和密码到sp
         * 跳转到MainActivity，并finish掉当前Activity
         */
        if (isSuccess){
            saveUser(username,pwd);
            startActivity(MainActivity.class,true);

        }else {
        //失败，弹出吐司
            showToast("登录失败："+ msg);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == R.id.et_pwd) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login();
                return true;
            }
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==REQUEST_SDCARD){
            if (grantResults[0]==PermissionChecker.PERMISSION_GRANTED){
                showToast("权限被允许了");
                login();
            }else{
                showToast("没有SDCard权限，可能导致部分功能无法使用！");
                /**
                 * 显示进度条对话框
                 */
                showDialog("正在登录中...");
                //3到20位字符（a-zA-Z0-9_）必须是字母开头
                String username = mEtUsername.getText().toString().trim();
                //3到20位的数字
                String pwd = mEtPwd.getText().toString().trim();
                mLoginPresenter.login(username, pwd);
            }
        }


    }
}
