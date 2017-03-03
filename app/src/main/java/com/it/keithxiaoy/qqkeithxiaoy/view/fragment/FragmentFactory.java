package com.it.keithxiaoy.qqkeithxiaoy.view.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by xiaoY on 2017/3/3.
 */

public class FragmentFactory {
    private static ConversationFragment sConversationFragment ;
    private static ContactFragment sContactFragment ;
    private static PluginFragment sPluginFragment ;

    public static Fragment getFragment(int position){
        if (position == 0) {
            if (sConversationFragment == null){
                sConversationFragment = new ConversationFragment();
            }
            return sConversationFragment;
        }else if (position == 1){
            if (sContactFragment == null){
                sContactFragment = new ContactFragment();
            }
            return sContactFragment;
        }else if (position == 2){
            if (sPluginFragment == null){
                sPluginFragment = new PluginFragment();
            }
            return sPluginFragment;
        }
        return null;
    }
}
