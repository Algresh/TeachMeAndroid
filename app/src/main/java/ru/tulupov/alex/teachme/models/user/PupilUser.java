package ru.tulupov.alex.teachme.models.user;

import android.content.Context;
import android.content.SharedPreferences;

import static ru.tulupov.alex.teachme.Constants.APP_PREFERENCES;


public class PupilUser extends User {

    public PupilUser() {

    }

    public PupilUser(Context context, String typeUser, int userId, String accessToken, int enable,
                     String email, String cityTitle, int cityId, String login) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_USER_TYPE_USER, typeUser);
        editor.putInt(PREF_USER_USER_ID, userId);
        editor.putString(PREF_USER_ACCESS_TOKEN, accessToken);
        editor.putInt(PREF_USER_ENABLE, enable);
        editor.putString(PREF_USER_EMAIL, email);
        editor.putString(PREF_USER_CITY_TITLE, cityTitle);
        editor.putInt(PREF_USER_CITY_ID, cityId);
        editor.putString(PREF_USER_LOGIN, login);
        editor.apply();

        this.typeUser = typeUser;
        this.accessToken = accessToken;
        this.userId = userId;
        this.enable = enable;
        this.email = email;
        this.cityTitle = cityTitle;
        this.cityId = cityId;
        this.login = login;
    }


    @Override
    public void clearAllData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_USER_TYPE_USER, User.TYPE_USER_NONE);

        editor.remove(PREF_USER_USER_ID);
        editor.remove(PREF_USER_ACCESS_TOKEN);
        editor.remove(PREF_USER_LOGIN);
        editor.remove(PREF_USER_ENABLE);
        editor.remove(PREF_USER_EMAIL);
        editor.remove(PREF_USER_CITY_TITLE);
        editor.remove(PREF_USER_CITY_ID);

        editor.apply();
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

    @Override
    public String getEmail(Context context) {
        if (email != null) {
            return email;
        }
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getString(PREF_USER_EMAIL, "");
    }

    @Override
    public void setEmail(Context context, String email) {
        setStringToPref(context, PREF_USER_EMAIL, email);
        this.email = email;
    }

    @Override
    public String getCityTitle(Context context) {
        if (cityTitle != null) {
            return cityTitle;
        }
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getString(PREF_USER_CITY_TITLE, "");
    }

    @Override
    public int getCityId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getInt(PREF_USER_CITY_ID, 0);
    }

    @Override
    public void setCityTitle(Context context, String cityTitle) {
        setStringToPref(context, PREF_USER_CITY_TITLE, cityTitle);
        this.cityTitle = cityTitle;
    }

    @Override
    public void setCityId(Context context, int cityId) {
        setIntToPref(context, PREF_USER_USER_ID, cityId);
        this.cityId = cityId;
    }

    public String getLogin(Context context) {
        if (login != null) {
            return login;
        }
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getString(PREF_USER_LOGIN, "");
    }

    public void setLogin(Context context, String login) {
        setStringToPref(context, PREF_USER_LOGIN, login);
        this.login = login;
    }
}
