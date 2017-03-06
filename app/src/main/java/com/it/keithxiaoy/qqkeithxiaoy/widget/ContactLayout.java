package com.it.keithxiaoy.qqkeithxiaoy.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.it.keithxiaoy.qqkeithxiaoy.R;

/**
 * Created by xiaoY on 2017/3/3.
 */

public class ContactLayout extends RelativeLayout {

    private RecyclerView mRecyclerView;
    private TextView mTvFloat;
    private SlideBar mSlideBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public ContactLayout(Context context) {
        this(context,null);
    }

    public ContactLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    public ContactLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.contact_layout,this,true);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mTvFloat = (TextView) findViewById(R.id.tv_float);
        mSlideBar = (SlideBar) findViewById(R.id.slideBar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent),getResources().getColor(R.color.btnNormal));


    }

    public void setAdapter(RecyclerView.Adapter adapter){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener refreshListener){
        mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
    }

    public void setRefreshing(boolean isRefreshing){
        mSwipeRefreshLayout.setRefreshing(isRefreshing);
    }

}
