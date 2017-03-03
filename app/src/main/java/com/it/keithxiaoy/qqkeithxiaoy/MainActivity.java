package com.it.keithxiaoy.qqkeithxiaoy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.it.keithxiaoy.qqkeithxiaoy.view.fragment.FragmentFactory;

public class MainActivity extends BaseActivity {
    private Toolbar mToolbar;
    private BottomNavigationBar mBottomNavigationBar;
    private TextView mTvTitle;
    private static final String[] TITLES = {"消息", "联系人", "动态"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolBar);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        initBottomNavigationBar();
        initFragment();
    }

    private void initFragment() {
        /**
         * 解决fragment重影的bug
         */
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();

        for (int i = 0; i < TITLES.length; i++) {
            Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(i + "");
            if (fragmentByTag != null){
                beginTransaction.remove(fragmentByTag);
            }
        }
            beginTransaction.commit();

        //先把第一个Fragment给添加进来
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, FragmentFactory.getFragment(0),""+0).commit();
    }

    private void initBottomNavigationBar() {
        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.conversation_selected_2, TITLES[0]))
                .addItem(new BottomNavigationItem(R.mipmap.contact_selected_2, TITLES[1]))
                .addItem(new BottomNavigationItem(R.mipmap.plugin_selected_2, TITLES[2]))
                .setActiveColor(R.color.colorPrimary)
                .setInActiveColor(R.color.tabInActive)
                .setFirstSelectedPosition(0)
                .initialise();
        mTvTitle.setText(TITLES[0]);

        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                /**
                 * 1. 修改ToolBar上的标题
                 * 2. 切换Fragment
                 *  （1）首先通过FragmentFactory获取到对应的Fragment
                 *  （2）判断该Fragment有没有被添加到Activity
                 *   (3) 如果没有添加则添加，然后显示
                 *   （4）直接显示
                 */
                mTvTitle.setText(TITLES[position]);
                Fragment fragment = FragmentFactory.getFragment(position);
                FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
                if (!fragment.isAdded()){
                    beginTransaction.add(R.id.fl_content,fragment,position+"");
                }
                beginTransaction.show(fragment).commit();
            }

            @Override
            public void onTabUnselected(int position) {
                //隐藏
                getSupportFragmentManager().beginTransaction().hide(FragmentFactory.getFragment(position)).commit();

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }
}
