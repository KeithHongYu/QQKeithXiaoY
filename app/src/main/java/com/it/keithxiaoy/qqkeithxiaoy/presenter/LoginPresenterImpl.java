package com.it.keithxiaoy.qqkeithxiaoy.presenter;

import com.hyphenate.chat.EMClient;
import com.it.keithxiaoy.qqkeithxiaoy.adapter.CallbackAdapter;
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
        EMClient.getInstance().login(userName, pwd, new CallbackAdapter() {
            @Override
            public void onMainSuccess() {
                mLoginView.afterLogin(true,null,userName,pwd);
            }

            @Override
            public void onMainError(int i, String s) {
                mLoginView.afterLogin(false,s,userName,pwd);
            }
        });
    }
}
