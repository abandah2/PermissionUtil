package com.spartacus.PermissionUtil;

import android.content.Context;

/**
 * Created by Abandah on 1/7/2018.
 */

class SharedPreferences {
    private static final String PREFERENCE = "PermissionUtil";

    //shared prefrnce control
    public static boolean isFirstTimeAskingPermission(Context context, String prefKey, boolean defaultValue) {
        return context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).getBoolean(prefKey, defaultValue);
    }

    public static boolean firstTimeAskingPermission(Context context, String prefKey, boolean Value) {
        return context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).edit().putBoolean(prefKey, Value).commit();
    }

}
