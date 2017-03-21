package com.it.keithxiaoy.qqkeithxiaoy.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.it.keithxiaoy.qqkeithxiaoy.R;
import com.it.keithxiaoy.qqkeithxiaoy.adapter.ContactAdapter;
import com.it.keithxiaoy.qqkeithxiaoy.event.ContactUpdateEvent;
import com.it.keithxiaoy.qqkeithxiaoy.presenter.ContactPresenter;
import com.it.keithxiaoy.qqkeithxiaoy.presenter.ContactPresenterImpl;
import com.it.keithxiaoy.qqkeithxiaoy.util.ToastUtils;
import com.it.keithxiaoy.qqkeithxiaoy.view.ContactView;
import com.it.keithxiaoy.qqkeithxiaoy.widget.ContactLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment implements ContactView, SwipeRefreshLayout.OnRefreshListener, ContactAdapter.OnContactItemClickListener {

    private static final String TAG = "ContactFragment";
    private ContactLayout mContactLayout;
    private ContactPresenter mContactPresenter;
    private ContactAdapter mContactAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContactLayout = (ContactLayout) view;

        mContactPresenter = new ContactPresenterImpl(this);
        /**
         * 获取联系人，然后展示到RecyclerView中
         */
        mContactPresenter.initContact();
        mContactLayout.setOnRefreshListener(this);
        //将当前对象作为EventBus的订阅者，这样当前对象就可以接收发布者发送的消息了
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ContactUpdateEvent contactUpdateEvent) {
        Log.d(TAG, "onEvent: " + contactUpdateEvent);
        Toast.makeText(getActivity(), "收到通讯录变化了：" + contactUpdateEvent, Toast.LENGTH_SHORT).show();
        //更新通讯录
        mContactPresenter.updateFromServer();

    }

    @Override
    public void onInitContact(List<String> contactsList) {
        mContactAdapter = new ContactAdapter(contactsList);
        mContactAdapter.setonContactItemClickListener(this);
        mContactLayout.setAdapter(mContactAdapter);
    }

    @Override
    public void onUpdateContact(boolean isSuccess, String message) {
        mContactAdapter.notifyDataSetChanged();
        /**
         *  把下拉刷新的进度条给隐藏掉
         */
        mContactLayout.setRefreshing(false);
    }


    @Override
    public void onRefresh() {
        //刷新是业务层的，所以需要放到Presenter层里面去处理
        mContactPresenter.updateFromServer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //当当前View销毁的时候，取消订阅
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(String username) {
        //跳转到聊天界面
        ToastUtils.showToast(getActivity(), "跟:"+username+"好友聊天愉快哦~~");
    }

    @Override
    public void onLongClick(final String username) {
        Snackbar.make(mContactLayout, "确定和" + username + "解除好友关系吗？？", Snackbar.LENGTH_LONG).setAction("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除好友，属于业务层面,所以需要放到Presenter层里面去处理
                mContactPresenter.deteleContact(username);
            }
        }).show();
    }

    @Override
    public void adterDelete(boolean isSuccess, String username) {
        if (isSuccess) {
            ToastUtils.showToast(getContext(), "删除成功" + username);
        } else {
            ToastUtils.showToast(getContext(), "删除失败" + username);
        }
    }
}
