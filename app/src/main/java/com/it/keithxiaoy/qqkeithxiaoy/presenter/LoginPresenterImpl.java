package com.it.keithxiaoy.qqkeithxiaoy.presenter;

import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.it.keithxiaoy.qqkeithxiaoy.util.ThreadUtils;
import com.it.keithxiaoy.qqkeithxiaoy.view.LoginView;

/**
 * Created by xiaoY on 2017/3/2.
 */

public class LoginPresenterImpl implements LoginPresenter {
    private LoginView mLoginView;

    public LoginPresenterImpl(LoginView loginView) {
        mLoginView = loginView;
    }

    @Override
    public void login(final String userName, final String pwd) {
        EMClient.getInstance().login(userName,pwd,new EMCallBack() {//回调
            @Override
            public void onSuccess() {
//                EMClient.getInstance().groupManager().loadAllGroups();
//                EMClient.getInstance().chatManager().loadAllConversations();
                ThreadUtils.runMainThread(new Runnable() {
                    @Override
                    public void run() {
                        mLoginView.afterLogin(true,"登录成功",userName,pwd);
                    }
                });

                Log.d("main", "登录聊天服务器成功！");
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "登录聊天服务器失败！");
            }
        });
    }
}
