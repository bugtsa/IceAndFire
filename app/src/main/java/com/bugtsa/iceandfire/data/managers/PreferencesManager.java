package com.bugtsa.iceandfire.data.managers;

import android.content.SharedPreferences;

import com.bugtsa.iceandfire.IceAndFireApplication;

import static com.bugtsa.iceandfire.utils.ConstantManager.FIRST_LAUNCH_KEY;


public class PreferencesManager {

    private SharedPreferences mSharedPreferences;

    /**
     * Присваивает SharedPreferences в переменную класса
     */
    public PreferencesManager() {
        this.mSharedPreferences = IceAndFireApplication.getsSharedPreferences();
    }

    /**
     * Сохраняет пользовательскую информацию в SharedPreferences
     *
     * @param isFirst List строк полей пользовательской информации
     */
    public void saveFirstLaunch(boolean isFirst) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(FIRST_LAUNCH_KEY, isFirst);
        editor.apply();
    }

    public boolean isFirstLaunch() {
        return mSharedPreferences.getBoolean(FIRST_LAUNCH_KEY, false);
    }
}
