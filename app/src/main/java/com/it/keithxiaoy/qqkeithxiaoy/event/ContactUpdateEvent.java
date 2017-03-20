package com.it.keithxiaoy.qqkeithxiaoy.event;

/**
 * Created by xiaoY on 2017/3/20.
 */

public class ContactUpdateEvent {
    public String username;
    public boolean isAdded;//是否被添加呢

    public ContactUpdateEvent( boolean isAdded,String username) {
        this.isAdded = isAdded;
        this.username = username;
    }

    @Override
    public String toString() {
        return "ContactUpdateEvent{" +
                "username='" + username + '\'' +
                ", isAdded=" + isAdded +
                '}';
    }
}
