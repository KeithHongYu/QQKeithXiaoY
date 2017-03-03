package com.it.keithxiaoy.qqkeithxiaoy.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by xiaoY on 2017/3/2.
 */

public class ThreadUtils {
    private static Executor sExecutor = Executors.newSingleThreadExecutor();
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public static void runSubThread(Runnable runnable){
        sExecutor.execute(runnable);
    }

    /**
     * 不管该方法是在哪个线程中调用的，一定能保证runnable在主线程中被调用
     * @param runnable
     */
    public static void runMainThread(Runnable runnable){
        sHandler.post(runnable);
    }

}
