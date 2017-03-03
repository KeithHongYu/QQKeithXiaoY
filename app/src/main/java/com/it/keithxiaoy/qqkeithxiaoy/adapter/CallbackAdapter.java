package com.it.keithxiaoy.qqkeithxiaoy.adapter;

import com.hyphenate.EMCallBack;
import com.it.keithxiaoy.qqkeithxiaoy.util.ThreadUtils;

/**
 * Created by xiaoY on 2017/3/3.
 */

public abstract class CallbackAdapter implements EMCallBack {
    public abstract void onMainSuccess();
    public abstract void onMainError(int i, String s);

    @Override
    public void onSuccess() {
        ThreadUtils.runMainThread(new Runnable() {
            @Override
            public void run() {
                onMainSuccess();
            }
        });
    }

    @Override
    public void onError(final int i, final String s) {
        ThreadUtils.runMainThread(new Runnable() {
            @Override
            public void run() {
                onMainError(i,s);
            }
        });
    }

    @Override
    public void onProgress(int i, String s) {

    }
}
