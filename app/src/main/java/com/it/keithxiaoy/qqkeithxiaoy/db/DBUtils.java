package com.it.keithxiaoy.qqkeithxiaoy.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.it.keithxiaoy.qqkeithxiaoy.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoY on 2017/3/4.
 */

public class DBUtils {
    private static Context sContext ;



    public static void initDB(Context context){
        sContext = context.getApplicationContext();
    }

    public static List<String>  getContactList (String username){
        if (sContext == null) {
            throw new RuntimeException("使用DBUtils之前需要调用期initDB方法，对其初始化!");
        }
        ContactSQLiteOpenHelper openHelper = new ContactSQLiteOpenHelper(sContext);
        SQLiteDatabase database = openHelper.getReadableDatabase();
        Cursor cursor = database.query(Constant.TABLE_CONTACT, new String[]{"contact"}, "username=?", new String[]{username}, null, null, "contact");
        List<String> contactsList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String contact = cursor.getString(0);
            contactsList.add(contact);
        }
        cursor.close();
        database.close();
        return contactsList;
    }

    public static void updateContacts(String username,List<String> contactsList){
        //先把username的好友全部删除掉
        //然后把最新的全部插入进入
        ContactSQLiteOpenHelper openHelper = new ContactSQLiteOpenHelper(sContext);
        SQLiteDatabase database = openHelper.getReadableDatabase();
        database.beginTransaction();//开始事务
        database.delete(Constant.TABLE_CONTACT,"username=?",new String[]{username});
        ContentValues values = new ContentValues();
        values.put("username",username);
        for (String contact : contactsList) {
            //一个ContentValues代表数据库中的一行记录
            values.put("contact",contact);
            database.insert(Constant.TABLE_CONTACT,null,values);
        }
        database.setTransactionSuccessful();//提交事务
        database.endTransaction();//释放事务资源
        database.close();
    }


}
