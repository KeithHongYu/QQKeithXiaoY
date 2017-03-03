package com.it.keithxiaoy.qqkeithxiaoy.util;

import android.text.TextUtils;

/**
 * Created by xiaoY on 2017/3/2.
 */

public class StringUtils {
    public static boolean checkUsername(String username){
        if (TextUtils.isEmpty(username)){
            return false;
        }
        return username.matches("^[a-zA-Z]\\w{2,19}$");
    }
    public static boolean checkPwd(String pwd){
        if (TextUtils.isEmpty(pwd)){
            return false;
        }
        return pwd.matches("^\\d{3,20}$");
    }
}
