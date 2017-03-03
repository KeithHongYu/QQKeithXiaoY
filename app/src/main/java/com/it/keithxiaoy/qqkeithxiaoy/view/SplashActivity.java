package com.it.keithxiaoy.qqkeithxiaoy.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.widget.ImageView;

import com.it.keithxiaoy.qqkeithxiaoy.BaseActivity;
import com.it.keithxiaoy.qqkeithxiaoy.MainActivity;
import com.it.keithxiaoy.qqkeithxiaoy.R;
import com.it.keithxiaoy.qqkeithxiaoy.adapter.AnimatorAdapter;
import com.it.keithxiaoy.qqkeithxiaoy.presenter.SplashPresenter;
import com.it.keithxiaoy.qqkeithxiaoy.presenter.SplashPresenterImpl;

public class SplashActivity extends BaseActivity implements SplashView{

    public static final int DURATION = 2000;
    private ImageView mIvSplash;
    private SplashPresenter mSplashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mIvSplash = (ImageView) findViewById(R.id.iv_splash);
        mSplashPresenter = new SplashPresenterImpl(this);
        /**
         * 1.判断是否登录  (P层)
         * 2.以前登录过，直接进入主界面    （View层）
         * 3.第一次登录，闪屏2秒，然后跳转登录界面  （View层）
         *
         */
        mSplashPresenter.checkLogin();
    }

    @Override
    public void onCheckLogin(Boolean isLogined) {
        if (isLogined){
            //进入主界面
            startActivity(MainActivity.class,true);
        }else {
            //进入登录界面
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mIvSplash, "alpha", 0, 1);
            objectAnimator.setDuration(DURATION);
            objectAnimator.start();
            objectAnimator.addListener(new AnimatorAdapter(){
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    startActivity(LoginActivity.class,true);
                }
            });
        }
    }
}
