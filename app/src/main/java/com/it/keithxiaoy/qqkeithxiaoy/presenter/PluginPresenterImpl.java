package com.it.keithxiaoy.qqkeithxiaoy.presenter;

import com.hyphenate.chat.EMClient;
import com.it.keithxiaoy.qqkeithxiaoy.adapter.CallbackAdapter;
import com.it.keithxiaoy.qqkeithxiaoy.view.PluginView;

/**
 * Created by xiaoY on 2017/3/3.
 */

public class PluginPresenterImpl implements PluginPresenter {
    private PluginView mPluginView ;

    public PluginPresenterImpl(PluginView pluginView) {
        mPluginView = pluginView;
    }

    @Override
    public void loginOut() {
        /**
         * 参数1：true代表解绑设备，不再接收任何推送消息
         */
        EMClient.getInstance().logout(true, new CallbackAdapter() {
            @Override
            public void onMainSuccess() {
                //退出成功
                mPluginView.afterLogout(true,null);
            }

            @Override
            public void onMainError(int i, String s) {
                //退出失败
                mPluginView.afterLogout(false,s);
            }
        });
    }
}
