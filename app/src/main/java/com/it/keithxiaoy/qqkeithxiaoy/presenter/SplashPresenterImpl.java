package com.it.keithxiaoy.qqkeithxiaoy.presenter;

import com.hyphenate.chat.EMClient;
import com.it.keithxiaoy.qqkeithxiaoy.view.SplashView;

/**
 * Created by xiaoY on 2017/3/1.
 */

public class SplashPresenterImpl implements SplashPresenter {
    private SplashView mSplashView;

    public SplashPresenterImpl(SplashView splashView) {
        mSplashView = splashView;
    }

    @Override
    public void checkLogin() {
        if (EMClient.getInstance().isConnected() && EMClient.getInstance().isLoggedInBefore()) {
            //已经自动登录了
            mSplashView.onCheckLogin(true);
        }else{
            //还未登录
            mSplashView.onCheckLogin(false);
        }
    }
}
