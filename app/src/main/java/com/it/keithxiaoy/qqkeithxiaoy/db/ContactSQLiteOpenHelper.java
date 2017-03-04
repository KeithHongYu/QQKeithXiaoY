package com.it.keithxiaoy.qqkeithxiaoy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.it.keithxiaoy.qqkeithxiaoy.util.Constant;

/**
 * Created by xiaoY on 2017/3/4.
 */

public class ContactSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "contact.db";
    private static final int DB_VERSION = 1;

    public ContactSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public ContactSQLiteOpenHelper(Context context){
        this(context,DB_NAME,null,DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ Constant.TABLE_CONTACT+"(_id integer primary key,username varchar(20),contact varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
