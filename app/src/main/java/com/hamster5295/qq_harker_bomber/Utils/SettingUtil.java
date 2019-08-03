package com.hamster5295.qq_harker_bomber.Utils;

import android.content.SharedPreferences;

public class SettingUtil {
    public static SharedPreferences setting;

    public static boolean getSettingBoolean(String name) {
        if (setting == null)
            return false;

        return setting.getBoolean(name, false);
    }

    public static int getSettingInt(String name) {
        if (setting == null)
            return 0;

        return Integer.parseInt(setting.getString(name, "200"));
    }
}
