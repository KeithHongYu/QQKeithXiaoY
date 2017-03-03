package com.it.keithxiaoy.qqkeithxiaoy.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hyphenate.chat.EMClient;
import com.it.keithxiaoy.qqkeithxiaoy.MainActivity;
import com.it.keithxiaoy.qqkeithxiaoy.R;
import com.it.keithxiaoy.qqkeithxiaoy.presenter.PluginPresenter;
import com.it.keithxiaoy.qqkeithxiaoy.presenter.PluginPresenterImpl;
import com.it.keithxiaoy.qqkeithxiaoy.view.LoginActivity;
import com.it.keithxiaoy.qqkeithxiaoy.view.PluginView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PluginFragment extends Fragment implements PluginView, View.OnClickListener {

    private PluginPresenter mPluginPresenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plugin, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button button = (Button) view.findViewById(R.id.btn_logout);
        //获取当前登录的用户名
        String currentUser = EMClient.getInstance().getCurrentUser();
        button.setText("退（" + currentUser + "）出");
        mPluginPresenter = new PluginPresenterImpl(this);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        //退出 跳转到登录界面
        MainActivity activity = (MainActivity) getActivity();
        activity.showDialog("正在退出...");
        //退出登录，并跳转到LoginActivity
        mPluginPresenter.loginOut();
    }

    @Override
    public void afterLogout(boolean isSuccess, String msg) {
        MainActivity activity = (MainActivity) getActivity();
        activity.hideDialog();
        if (!isSuccess) {
            activity.showDialog("退出失败：" + msg);
        }
        activity.startActivity(LoginActivity.class,true);
    }
}
