package ru.tulupov.alex.teachme;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;

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
//        Log.d(MY_TAG, "Application");
        AnalyticsTrackers.initialize(this);
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
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


    public synchronized Tracker getGoogleAnalyticsTracker() {
        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
        return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
    }

    /***
     * Tracking screen view
     *
     * @param screenName screen name to be displayed on GA dashboard
     */
    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker();

        // Set screen name.
        t.setScreenName(screenName);

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }

    /***
     * Tracking exception
     *
     * @param e exception to be tracked
     */
    public void trackException(Exception e) {
        if (e != null) {
            Tracker t = getGoogleAnalyticsTracker();

            t.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(
                            new StandardExceptionParser(this, null)
                                    .getDescription(Thread.currentThread().getName(), e))
                    .setFatal(false)
                    .build()
            );
        }
    }

    /***
     * Tracking event
     *
     * @param category event category
     * @param action   action of the event
     * @param label    label
     */
    public void trackEvent(String category, String action, String label) {
        Tracker t = getGoogleAnalyticsTracker();

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
    }



}
