package com.it.keithxiaoy.qqkeithxiaoy.presenter;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.it.keithxiaoy.qqkeithxiaoy.db.DBUtils;
import com.it.keithxiaoy.qqkeithxiaoy.util.ThreadUtils;
import com.it.keithxiaoy.qqkeithxiaoy.view.ContactView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by xiaoY on 2017/3/4.
 */

public class ContactPresenterImpl implements ContactPresenter {
    private ContactView mContactView;
    private List<String> contactsList = new ArrayList<>();

    public ContactPresenterImpl(ContactView contactView) {
        mContactView = contactView;
    }

    @Override
    public void initContact() {
        /**
         * 1. 去访问本地数据库的联系人缓存
         * 2. 拿到缓存后返回给View
         * 3. 去访问网络，获取网络上最新的联系人
         * 4. 当网络数据过来的时候
         * 5. 更新View
         * 6. 缓存到本地数据库
         */
        final String currentUser = EMClient.getInstance().getCurrentUser();
        List<String> contacts = DBUtils.getContactList(currentUser);
        contactsList.clear();
        contactsList.addAll(contacts);
        mContactView.onInitContact(contactsList);
        updateFromServer();
    }

    @Override
    public void updateFromServer() {
        final String currentUser = EMClient.getInstance().getCurrentUser();
        ThreadUtils.runSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> contactsFromServer = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    //请求服务器成功
                    contactsList.clear();
                    contactsList.addAll(contactsFromServer);
                    //服务器的请求过来的列表是无序的，我们需要将其排序
                    Collections.sort(contactsList, new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            return o1.compareToIgnoreCase(o2);
                        }
                    });
                    ThreadUtils.runMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.onUpdateContact(true, null);
                        }
                    });
                    // 缓存到本地数据库
                    DBUtils.updateContacts(currentUser, contactsList);

                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    //请求服务器失败
                    ThreadUtils.runMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.onUpdateContact(false, e.getMessage());
                        }
                    });

                }
            }
        });
    }

    @Override
    public void deteleContact(final String username) {
        ThreadUtils.runSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(username);
                    //删除成功
                    adterDelete(true, username);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    //删除失败
                    adterDelete(false, username);
                }
            }
        });

    }

    private void adterDelete(final boolean b, final String username) {
        ThreadUtils.runMainThread(new Runnable() {
            @Override
            public void run() {
                mContactView.adterDelete(b, username);
            }
        });
    }
}
