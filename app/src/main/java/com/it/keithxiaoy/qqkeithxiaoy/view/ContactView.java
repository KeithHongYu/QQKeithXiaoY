package com.it.keithxiaoy.qqkeithxiaoy.view;

import java.util.List;

/**
 * Created by xiaoY on 2017/3/4.
 */

public interface ContactView {
    void onInitContact(List<String> contactsList);

    void onUpdateContact(boolean isSuccess, String message);

    void adterDelete(boolean isSuccess, String username);
}
