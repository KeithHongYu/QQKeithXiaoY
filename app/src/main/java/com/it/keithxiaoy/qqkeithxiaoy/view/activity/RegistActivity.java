package com.it.keithxiaoy.qqkeithxiaoy.view.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.it.keithxiaoy.qqkeithxiaoy.BaseActivity;
import com.it.keithxiaoy.qqkeithxiaoy.R;
import com.it.keithxiaoy.qqkeithxiaoy.presenter.RegistPresenter;
import com.it.keithxiaoy.qqkeithxiaoy.presenter.RegistPresenterImpl;
import com.it.keithxiaoy.qqkeithxiaoy.util.StringUtils;
import com.it.keithxiaoy.qqkeithxiaoy.view.LoginActivity;
import com.it.keithxiaoy.qqkeithxiaoy.view.RegistView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistActivity extends BaseActivity implements RegistView, TextView.OnEditorActionListener {

    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.til_username)
    TextInputLayout mTilUsername;
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.til_pwd)
    TextInputLayout mTilPwd;
    @BindView(R.id.btn_regist)
    Button mBtnRegist;

    private RegistPresenter mRegisetPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式状态栏
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

        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);
        mEtPwd.setOnEditorActionListener(this);
        mRegisetPresenter = new RegistPresenterImpl(this);
    }

    @OnClick(R.id.btn_regist)
    public void onClick() {
        regist();

    }

    private void regist() {
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
        //显示进度条
        showDialog("正在注册中...");
        //提交服务器
        mRegisetPresenter.regist(username, pwd);


    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == R.id.et_pwd) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                regist();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRegistResult(boolean isSuccess, String msg, String username, String pwd) {
        hideDialog();

        /**
         * 1.注册成功，保存用户名和密码到sp,然后跳转到LoginActivity
         * 2.注册失败，弹出Toast提醒
         */

        if (isSuccess) {
            saveUser(username,pwd);
            startActivity(LoginActivity.class,true);

        } else {
            showToast("注册失败：" + msg);
        }


    }
}
