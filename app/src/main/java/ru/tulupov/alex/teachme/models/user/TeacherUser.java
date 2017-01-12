package ru.tulupov.alex.teachme.models.user;

import android.content.Context;
import android.content.SharedPreferences;

import static ru.tulupov.alex.teachme.Constants.APP_PREFERENCES;


public class TeacherUser extends User {

    public static final String PREF_USER_FIRST_NAME= "pref_first_name";
    public static final String PREF_USER_LAST_NAME = "pref_last_name";
    public static final String PREF_USER_FATHER_NAME = "pref_father_name";
    public static final String PREF_USER_LOGIN = "pref_login";

    private String firstName;
    private String lastName;
    private String fatherName;
    private String login;

    public TeacherUser(){

    }

    public TeacherUser(Context context, String typeUser, int userId, String accessToken, int enable, String firstName, String lastName, String fatherName, String login) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_USER_TYPE_USER, typeUser);
        editor.putInt(PREF_USER_USER_ID, userId);
        editor.putString(PREF_USER_ACCESS_TOKEN, accessToken);
        editor.putString(PREF_USER_FIRST_NAME, firstName);
        editor.putString(PREF_USER_LAST_NAME, lastName);
        editor.putString(PREF_USER_FATHER_NAME, fatherName);
        editor.putString(PREF_USER_LOGIN, login);
        editor.putInt(PREF_USER_ENABLE, enable);
        editor.apply();

        this.firstName = firstName;
        this.lastName = lastName;
        this.fatherName = fatherName;
        this.login = login;
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


    public String getFirstName(Context context) {
        if (firstName != null) {
            return firstName;
        }
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getString(PREF_USER_FIRST_NAME, "");
    }

    public void setFirstName(Context context, String firstName) {
        setStringToPref(context, PREF_USER_FIRST_NAME, firstName);
        this.firstName = firstName;
    }

    public String getLastName(Context context) {
        if (lastName != null) {
            return lastName;
        }
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getString(PREF_USER_LAST_NAME, "");
    }

    public void setLastName(Context context, String lastName) {
        setStringToPref(context, PREF_USER_LAST_NAME, lastName);
        this.lastName= lastName;
    }

    public String getFatherName(Context context) {
        if (fatherName != null) {
            return fatherName;
        }
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getString(PREF_USER_FATHER_NAME, "");
    }

    public void setFatherName(Context context, String fatherName) {
        setStringToPref(context, PREF_USER_FATHER_NAME, fatherName);
        this.fatherName = fatherName;
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
