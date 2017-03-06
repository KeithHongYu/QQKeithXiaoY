package com.it.keithxiaoy.qqkeithxiaoy.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.it.keithxiaoy.qqkeithxiaoy.R;
import com.it.keithxiaoy.qqkeithxiaoy.adapter.ContactAdapter;
import com.it.keithxiaoy.qqkeithxiaoy.presenter.ContactPresenter;
import com.it.keithxiaoy.qqkeithxiaoy.presenter.ContactPresenterImpl;
import com.it.keithxiaoy.qqkeithxiaoy.view.ContactView;
import com.it.keithxiaoy.qqkeithxiaoy.widget.ContactLayout;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment implements ContactView, SwipeRefreshLayout.OnRefreshListener {


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

    }

    @Override
    public void onInitContact(List<String> contactsList) {
        mContactAdapter = new ContactAdapter(contactsList);

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
}
