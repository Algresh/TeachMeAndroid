package ru.tulupov.alex.teachme;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import ru.tulupov.alex.teachme.models.user.PupilUser;
import ru.tulupov.alex.teachme.models.user.TeacherUser;
import ru.tulupov.alex.teachme.models.user.User;

import static ru.tulupov.alex.teachme.Constants.APP_PREFERENCES;
import static ru.tulupov.alex.teachme.Constants.MY_TAG;

public class MyApplications extends Application {

    private static User user;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(MY_TAG, "Application");
        initUser();
    }

    public static User getUser() {
        if (user == null) {

        }
        return user;
    }

    public static void setUser(User user) {
        MyApplications.user = user;
    }

    public User initUser() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        String type = preferences.getString(User.PREF_USER_TYPE_USER, "");
        User user = null;
        if (type.equals(User.TYPE_USER_TEACHER)) {
            user = new TeacherUser();
        } else if (type.equals(User.TYPE_USER_PUPIL)) {
            user = new PupilUser();
        }
        MyApplications.user = user;

        return user;
    }






}
