package com.baichang.android.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by iscod on 2016/5/21.
 */
public class BCContactsUtil {
    public static List<String> getContacts(Context context) throws Exception {
        List<String> mContacts = new ArrayList<>();
        Uri uri = Uri.parse("content://com.android.contacts/contacts");
        //获得一个ContentResolver数据共享的对象
        ContentResolver reslover = context.getContentResolver();
        //取得联系人中开始的游标，通过content://com.android.contacts/contacts这个路径获得
        Cursor cursor = reslover.query(uri, null, null, null, null);

        //上边的所有代码可以由这句话代替：Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        //Uri.parse("content://com.android.contacts/contacts") == ContactsContract.Contacts.CONTENT_URI
        while (cursor.moveToNext()) {
            //获得联系人ID
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            //获得联系人姓名
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            //获得联系人手机号码
            Cursor phone = reslover.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null);
            while (phone.moveToNext()) { //取得电话号码(可能存在多个号码)
                int phoneFieldColumnIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String strPhone = phone.getString(phoneFieldColumnIndex);
                String finPhone = strPhone.replace(" ", "").trim();
                mContacts.add(finPhone);
            }
        }
        return mContacts;
    }
}
