package com.hamster5295.qq_harker_bomber;

import android.content.SharedPreferences;

public class SettingUtil {
    public static SharedPreferences setting;
    public static boolean getSettingBoolean(String name) {
        if (setting == null)
            return false;

        return setting.getBoolean(name, false);
    }
}
