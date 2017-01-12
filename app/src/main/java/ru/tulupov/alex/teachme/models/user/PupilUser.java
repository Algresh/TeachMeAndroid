package ru.tulupov.alex.teachme.models.user;

import android.content.Context;
import android.content.SharedPreferences;

import static ru.tulupov.alex.teachme.Constants.APP_PREFERENCES;


public class PupilUser extends User {

    public PupilUser() {

    }

    public PupilUser(Context context, String typeUser, int userId, String accessToken, int enable) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_USER_TYPE_USER, typeUser);
        editor.putInt(PREF_USER_USER_ID, userId);
        editor.putString(PREF_USER_ACCESS_TOKEN, accessToken);
        editor.putInt(PREF_USER_ENABLE, enable);
        editor.apply();

        this.typeUser = typeUser;
        this.accessToken = accessToken;
        this.userId = userId;
        this.enable = enable;
    }

    @Override
    public String getAccessToken(Context context) {
        if (accessToken != null) {
            return accessToken;
        }
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getString(PREF_USER_ACCESS_TOKEN, "");
    }

    @Override
    public int getUserId(Context context) {
        if (userId != 0) {
            return userId;
        }
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getInt(PREF_USER_USER_ID, 0);
    }

    @Override
    public void setAccessToken(Context context, String accessToken) {
        setStringToPref(context, PREF_USER_ACCESS_TOKEN, accessToken);
        this.accessToken = accessToken;
    }

    @Override
    public void setUserId(Context context, int userId) {
        setIntToPref(context, PREF_USER_USER_ID, userId);
        this.userId = userId;
    }

    @Override
    public int getEnable(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getInt(PREF_USER_ENABLE, 0);
    }

    @Override
    public void setEnable(Context context, int enable) {
        setIntToPref(context, PREF_USER_ENABLE, enable);
        this.enable = enable;
    }
}
