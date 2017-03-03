package com.it.keithxiaoy.qqkeithxiaoy.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by xiaoY on 2017/3/2.
 */

public class ToastUtils {
    private static Toast sToast;

    public static void showToast(Context context, String msg) {
        if (sToast == null) {
            sToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        }
        sToast.setText(msg);
        sToast.show();
    }
}
