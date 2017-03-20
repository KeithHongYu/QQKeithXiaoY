package com.it.keithxiaoy.qqkeithxiaoy;

import android.app.ActivityManager;
import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;

import com.avos.avoscloud.AVOSCloud;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;
import com.it.keithxiaoy.qqkeithxiaoy.db.DBUtils;
import com.it.keithxiaoy.qqkeithxiaoy.event.ContactUpdateEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by xiaoY on 2017/3/1.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initHuanXin();
        initAVOS();
        initDB();
    }

    private void initDB() {
        DBUtils.initDB(this);
    }

    private void initAVOS() {
        AVOSCloud.initialize(this,"WSN79vW3vDjFAYLwf6pj19uf-gzGzoHsz","ze2kTHqwqBsHzjFWSTBiQbGn");
    }

    private void initHuanXin() {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null ||!processAppName.equalsIgnoreCase(getPackageName())) {

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        //初始化
        EMClient.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
        initHuanxinListener();
    }

    private void initHuanxinListener() {
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {

            @Override
            public void onContactInvited(String username, String reason) {
                //收到好友邀请

                try {
                    Log.d(TAG, "onContactInvited: "+username+"/"+reason+"/自动接收了邀请！");
                    EMClient.getInstance().contactManager().acceptInvitation(username);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFriendRequestAccepted(String s) {
                //好友请求被同意
            }

            @Override
            public void onFriendRequestDeclined(String s) {
                //好友请求被拒绝
            }

            @Override
            public void onContactDeleted(String username) {
                //被删除时回调此方法
                Log.d(TAG, "onContactDeleted: "+username);
                EventBus.getDefault().post(new ContactUpdateEvent(false,username));
            }


            @Override
            public void onContactAdded(String username) {
                //增加了联系人时回调此方法
                Log.d(TAG, "onContactAdded: "+username);
                EventBus.getDefault().post(new ContactUpdateEvent(true,username));
            }
        });
    }


    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
}
