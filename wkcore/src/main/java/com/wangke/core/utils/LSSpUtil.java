package com.wangke.core.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * SharedPreferences 操作类
 * Created by Bining on 15/2/3.
 */
public class LSSpUtil {

    private static Context context;
    /**
     * 保存在手机里面的文件名
     */
    public static String FILE_NAME ;
    public static String DEF_FILE_NAME;
    public static String FILE_NAME_SD = "share_data_sd";
    public static String SD_PATH = "";

    public static boolean hasSD_Path ;

    /**
     * 普通构造方法，只允许将数据放到 内部空间
     * @param context
     */
    public LSSpUtil(Context context) {
        this.context = context;
        FILE_NAME = "share_data";
        DEF_FILE_NAME = "share_data";
    }

    /**
     * 普通构造方法，只允许将数据放到 内部空间, 可自定义 默认 SP文件 名称
     * @param context
     * @param defFileName
     */
    public LSSpUtil(Context context, String defFileName) {
        this.context = context;
        DEF_FILE_NAME = defFileName;
        FILE_NAME = defFileName;
    }

    public static void init(Context context) {
        LSSpUtil.context = context;
        FILE_NAME = "share_data";
        DEF_FILE_NAME = "share_data";
    }


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     */
    public static void put(String key, Object object) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else if (object instanceof String[]) {
            StringBuilder datas = new StringBuilder();
            String[] data = (String[]) object;
            for (int i = 0; i < data.length; i++) {
                if (i != 0) {
                    datas.append(":");
                }
                datas.append(data[i]);
            }
            editor.putString(key, datas.toString());
        } else {
//            editor.putString(key, object.toString());
        }
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * \
     * 指定保存的文件名的方法
     *
     * @param key
     * @param object
     * @param fileName
     */
    public static void put(String key, Object object, String fileName) {
        FILE_NAME = fileName;
        put( key, object);
        FILE_NAME = DEF_FILE_NAME;       //改完之后，把  FILE_NAME  改回默认，防止其他地方调用出错
    }

    public static void putToSD(String key, Object object) {
        try {
            setToSd(context);
            put( key, object, FILE_NAME_SD);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void putToSD(String key, Object object, String fileName) {
        try {
            setToSd(context);
            put( key, object, fileName);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */
    public static Object get(String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        } else if (defaultObject instanceof String[]) {
            return sp.getString(key, "").split(":");
        }
        return null;
    }

    /**
     * \
     * 指定文件名获取保存的数据的方法
     *
     * @param key
     * @param object
     * @param fileName
     */
    public static Object get(String key, Object object, String fileName) {
        if (!TextUtils.isEmpty(fileName)) {
            FILE_NAME = fileName;
            Object o = get(key, object);
            FILE_NAME = DEF_FILE_NAME;       //改完之后，把  FILE_NAME  改回默认，防止其他地方调用出错
            return o;
        } else {
            return object;
        }

    }

    public static Object getFromSD(String key, Object object) {
        try {
            setToSd(context);
            return get( key, object, FILE_NAME_SD);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static Object getFromSD(String key, Object defObject, String fileName) {
        try {
            setToSd(context);
            if (!TextUtils.isEmpty(fileName)) {

                return get( key, defObject, fileName);
            } else {
                return defObject;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return defObject;
    }


    /**
     * 移除某个key值已经对应的值
     */
    public static void remove(String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 移除某个key值已经对应的值
     */
    public static void remove(String key, String fileName) {
        if (!TextUtils.isEmpty(fileName)) {
            FILE_NAME = fileName;
            remove( key);
            FILE_NAME = DEF_FILE_NAME;       //改完之后，把  FILE_NAME  改回默认，防止其他地方调用出错
        }
    }

    public static void removeFromSD(String key) {
        try {
            setToSd(context);
            remove( key, FILE_NAME_SD);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void removeFromSD(String key, String fileName) {
        try {
            setToSd(context);
            if (!TextUtils.isEmpty(fileName)) {
                remove( key, fileName);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除所有数据
     */
    public static void clearAll() {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除指定文件名所有数据
     */
    public static void clearAll(String fileName) {

        if (!TextUtils.isEmpty(fileName)) {
            FILE_NAME = fileName;
            clearAll();
            FILE_NAME = DEF_FILE_NAME;       //改完之后，把  FILE_NAME  改回默认，防止其他地方调用出错
        }
    }


    public static void clearAllFromSD() {
        try {
            setToSd(context);
            clearAll( FILE_NAME_SD);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void clearAllFromSD(String fileName) {
        try {
            setToSd(context);
            if (!TextUtils.isEmpty(fileName)) {
                clearAll( fileName);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * 查询某个key是否已经存在
     */
    public static boolean contains(Context context, String key) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getAll();
    }


    /**
     * 反射机制改变SP可存到SD卡上
     */
    private static void setToSd(Context context) throws NoSuchFieldException, IllegalAccessException {
        Field field;
        // 获取ContextWrapper对象中的mBase变量。该变量保存了ContextImpl对象
        field = ContextWrapper.class.getDeclaredField("mBase");
        field.setAccessible(true);
        // 获取mBase变量
        Object obj = field.get(context);
        // 获取ContextImpl.mPreferencesDir变量，该变量保存了数据文件的保存路径
        field = obj.getClass().getDeclaredField("mPreferencesDir");
        field.setAccessible(true);
        // 创建自定义路径
        File file = new File(SD_PATH);
        // 修改mPreferencesDir变量的值
        field.set(obj, file);
    }



    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 保存在手机里面的文件名
         */
        public static final String FILE_NAME = DEF_FILE_NAME;

        /**
         * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
         */
        public static void put(Context context, String key, Object object) {
            SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            if (object instanceof String) {
                editor.putString(key, (String) object);
            } else if (object instanceof Integer) {
                editor.putInt(key, (Integer) object);
            } else if (object instanceof Boolean) {
                editor.putBoolean(key, (Boolean) object);
            } else if (object instanceof Float) {
                editor.putFloat(key, (Float) object);
            } else if (object instanceof Long) {
                editor.putLong(key, (Long) object);
            } else if (object instanceof String[]) {
                StringBuilder datas = new StringBuilder();
                String[] data = (String[]) object;
                for (int i = 0; i < data.length; i++) {
                    if (i != 0) {
                        datas.append(":");
                    }
                    datas.append(data[i]);
                }
                editor.putString(key, datas.toString());
            } else {
                editor.putString(key, object.toString());
            }
            SharedPreferencesCompat.apply(editor);
        }

        /**
         * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
         */
        public static Object get(Context context, String key, Object defaultObject) {
            SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            if (defaultObject instanceof String) {
                return sp.getString(key, (String) defaultObject);
            } else if (defaultObject instanceof Integer) {
                return sp.getInt(key, (Integer) defaultObject);
            } else if (defaultObject instanceof Boolean) {
                return sp.getBoolean(key, (Boolean) defaultObject);
            } else if (defaultObject instanceof Float) {
                return sp.getFloat(key, (Float) defaultObject);
            } else if (defaultObject instanceof Long) {
                return sp.getLong(key, (Long) defaultObject);
            } else if (defaultObject instanceof String[]) {
                return sp.getString(key, "").split(":");
            }
            return null;
        }

        /**
         * 移除某个key值已经对应的值
         */
        public static void remove(Context context, String key) {
            SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.remove(key);
            SharedPreferencesCompat.apply(editor);
        }

        /**
         * 清除所有数据
         */
        public static void clear(Context context) {
            SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            SharedPreferencesCompat.apply(editor);
        }

        /**
         * 查询某个key是否已经存在
         */
        public static boolean contains(Context context, String key) {
            SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            return sp.contains(key);
        }

        /**
         * 返回所有的键值对
         */
        public static Map<String, ?> getAll(Context context) {
            SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            return sp.getAll();
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            editor.commit();
        }
    }
}
