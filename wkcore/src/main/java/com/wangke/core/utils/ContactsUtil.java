package com.wangke.core.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

public class ContactsUtil {

    /**
     * 插入或更新联系人    保证只有一个号码
     *
     * @param context
     * @param name
     * @param phoneNumbe
     */
    public static void insertOrUpdateContact(Context context, String name, String phoneNumbe) {
        String id = findContactIDbyName(context, name);
        if (TextUtils.isEmpty(id)) {
            addContact(context, name, phoneNumbe);
        } else {
            updateContact(context, id, phoneNumbe);
        }
    }

    /**
     * 插入或添加联系人   号码累加
     *
     * @param context
     * @param name
     * @param phoneNumbe
     */
    public static void insertOrAddContact(Context context, String name, String phoneNumbe) {
        try {
            String id = findContactIDbyName(context, name);
            if (TextUtils.isEmpty(id)) {
                addContact(context, name, phoneNumbe);
            } else {
                addContactById(context, id, phoneNumbe);
            }
        } catch (Exception e) {

        }
    }

    public static boolean hasThisContact(Context context, String name) {
        return !TextUtils.isEmpty(findContactIDbyName(context, name));
    }


    private static void addContact(Context context, String name, String phoneNumber) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phoneNumber)) {
            return;
        }
        // 创建一个空的ContentValues
        ContentValues values = new ContentValues();
        // 向RawContacts.CONTENT_URI空值插入，
        // 先获取Android系统返回的rawContactId
        // 后面要基于此id插入值
        Uri rawContactUri = context.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        // 内容类型
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        // 联系人名字
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
        // 向联系人URI添加联系人名字
        context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        values.clear();

        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        // 联系人的电话号码
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);
        // 电话类型
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        // 向联系人电话号码URI添加电话号码
        context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

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

    private static void addContactById(Context context, String id, String phone) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, id);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        resolver.insert(ContactsContract.Data.CONTENT_URI, values);
    }


    private static String findContactIDbyName(Context context, String name) {
        //根据姓名求id
        Cursor cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.Data.RAW_CONTACT_ID},
                ContactsContract.Contacts.DISPLAY_NAME + "=?", new String[]{name}, null);
        try {
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    String contacetId = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID));
                    return contacetId;
                }
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return "";
    }

}
