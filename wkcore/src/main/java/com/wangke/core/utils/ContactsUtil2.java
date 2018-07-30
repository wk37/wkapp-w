package com.wangke.core.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ContactsUtil2 {


    public static void test(Context context) {
        Map<String, String> map = findAllContactID(context);
        if (map != null) {
            Log.e("ContactsUtil2", "map " + map.toString());

            Set<Map.Entry<String, String>> set = map.entrySet();
            // 遍历键值对对象的集合，得到每一个键值对对象
            for (Map.Entry<String, String> me : set) {
                // 根据键值对对象获取键和值
                String key = me.getKey();
                String value = me.getValue();
                if (!TextUtils.isEmpty(value)) {
                    value = value.replace("-", "");
                    value = value.replace("+86", "");
                    updateContact(context, key, value);
                } else {
                    Log.e("ContactsUtil2", "name: " + key + "   phone: " + value);
//                    deleteContact(context, key);
                }
            }
        }
    }


    private static void deleteContact(Context context, String contactId) {
        ContentResolver cr = context.getContentResolver();
        //第一步先删除Contacts表中的数据
        cr.delete(ContactsContract.Contacts.CONTENT_URI, ContactsContract.Contacts._ID + " =?", new String[]{contactId + ""});
        //第二步再删除RawContacts表的数据
        cr.delete(ContactsContract.RawContacts.CONTENT_URI, ContactsContract.RawContacts.CONTACT_ID + " =?", new String[]{contactId + ""});
    }

    private static void updateContact(Context context, String id, String phone) {

        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        resolver.update(ContactsContract.Data.CONTENT_URI, values,
                ContactsContract.Data.MIMETYPE + "=? and " + ContactsContract.Data.RAW_CONTACT_ID + "=?",
                new String[]{ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, id + ""});
    }


    private static Map<String, String> findAllContactID(Context context) {
        Map<String, String> map = new HashMap<>();
        Cursor cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.Data.RAW_CONTACT_ID,

                        ContactsContract.CommonDataKinds.Phone.NUMBER},
                null, null, null);
        try {
            if (cursor != null && cursor.getCount() > 0) {
                Log.e("ContactsUtil2", "cursor.getCount() " + cursor.getCount());
//
                while (cursor.moveToNext()) {
                    map.put(cursor.getString(cursor.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID)),
                            cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                }
                return map;
            }
        } catch (Exception e) {

        }finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return map;
    }

}
