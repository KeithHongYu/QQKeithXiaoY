package com.it.keithxiaoy.qqkeithxiaoy.presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SignUpCallback;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.it.keithxiaoy.qqkeithxiaoy.model.User;
import com.it.keithxiaoy.qqkeithxiaoy.util.ThreadUtils;
import com.it.keithxiaoy.qqkeithxiaoy.view.RegistView;

/**
 * Created by xiaoY on 2017/3/2.
 */

public class RegistPresenterImpl implements RegistPresenter {

    private RegistView mRegistView;

    public RegistPresenterImpl(RegistView registView) {
        mRegistView = registView;
    }

    /**
     * 1.先注册AVOS
     * 2.再注册环信
     * 3.两者都成功才算成功
     *
     * @param username
     * @param pwd
     */
    @Override
    public void regist(final String username, final String pwd) {
        final User user = new User();
        user.setUsername(username);// 设置用户名
        user.setPassword(pwd);// 设置密码
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    // 注册成功，把用户对象赋值给当前用户 AVUser.getCurrentUser()
                    /**
                     *   判断环信是否注册成功
                     *   如果环信注册成功则成功
                     *   如果环信注册失败则需要删除AVOS的数据
                     */
                    ThreadUtils.runSubThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().createAccount(username, pwd);//同步方法
                                //注册成功
                                afterRegist(true, null, username, pwd);


                            } catch (final HyphenateException e1) {
                                e1.printStackTrace();
                                //环信注册失败，还需要删除AVOS的数据
                                try {
                                    user.delete();
                                    afterRegist(false, e1.getMessage(), username, pwd);
                                } catch (AVException e2) {
                                    e2.printStackTrace();
                                }

                            }
                        }
                    });
                    //注册失败会抛出HyphenateException


                } else {
                    // 失败的原因可能有多种，常见的是用户名已经存在。
                    mRegistView.onRegistResult(false, e.getMessage(), username, pwd);
                }
            }
        });
    }

    private void afterRegist(final boolean isSuccess, final String msg, final String username, final String pwd) {
        ThreadUtils.runMainThread(new Runnable() {
            @Override
            public void run() {
                mRegistView.onRegistResult(isSuccess, msg, username, pwd);
            }
        });
    }
}
