package ru.tulupov.alex.teachme.models.user;

import android.content.Context;
import android.content.SharedPreferences;

import static ru.tulupov.alex.teachme.Constants.APP_PREFERENCES;

public abstract class User {

    public static final String TYPE_USER_PUPIL = "pupil";
    public static final String TYPE_USER_TEACHER = "teacher";
    public static final String TYPE_USER_NONE = "none";

    protected static final String PREF_USER_ACCESS_TOKEN = "pref_access_token";
    protected static final String PREF_USER_USER_ID = "pref_user_id";
    protected static final String PREF_USER_ENABLE = "pref_enable";
    public static final String PREF_USER_TYPE_USER = "pref_type";
    public static final String PREF_USER_CITY_TITLE = "pref_city_title";
    public static final String PREF_USER_CITY_ID = "pref_city_id";
    public static final String PREF_USER_EMAIL = "pref_email";
    public static final String PREF_USER_LOGIN = "pref_login";

    public static final int TYPE_ENABLE_ENABLE = 1;
    public static final int TYPE_ENABLE_DISABLE = 0;

    protected String typeUser;
    protected String accessToken;
    protected String cityTitle;
    protected int cityId;
    protected String email;
    protected int userId = 0;
    protected int enable;
    protected String login;

    abstract String getAccessToken(Context context);
    abstract int getUserId(Context context);
    abstract int getEnable(Context context);
    abstract String getCityTitle(Context context);
//    abstract int getCityId(Context context);
    abstract String getEmail(Context context);

    abstract void setAccessToken(Context context, String accessToken);
    abstract void setUserId(Context context, int userId);
    abstract void setEnable(Context context, int enable);
    abstract void setCityTitle(Context context, String cityTitle);
//    abstract void setCityId(Context context, int cityId);
    abstract void setEmail(Context context, String email);

    public String getTypeUser(Context context) {
        if (typeUser != null) {
            return typeUser;
        }
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getString(PREF_USER_TYPE_USER, TYPE_USER_NONE);
    }

    public void setTypeUser(Context context, String typeUser) {
        setStringToPref(context, PREF_USER_TYPE_USER, typeUser);
        this.typeUser = typeUser;
    }

    protected void setStringToPref(Context context, String param, String str) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(param, str);
        editor.apply();
    }

    protected void setIntToPref(Context context, String param, int digit) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(param, digit);
        editor.apply();
    }

    public void setCityId(Context context, int cityId) {
        setIntToPref(context, PREF_USER_USER_ID, cityId);
        this.cityId = cityId;
    }

    public int getCityId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getInt(PREF_USER_CITY_ID, 0);
    }


}
