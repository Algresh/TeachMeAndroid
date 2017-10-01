package ru.tulupov.alex.teachme.views.activivties;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import ru.tulupov.alex.teachme.MyApplications;
import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.user.User;

import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.TYPE_SEARCH;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.TYPE_SEARCH_ALL;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                User user = MyApplications.getUser();

                if (user == null) {
                    user = ((MyApplications) getApplication()).initUser();
                    if (user != null) {
                        MyApplications.setUser(user);
                    }
                }
                Intent intent;

                intent = new Intent(SplashActivity.this, ListTeachersActivity.class);
                intent.putExtra(TYPE_SEARCH, TYPE_SEARCH_ALL);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

//                if (user == null) {
//                    intent = new Intent(SplashActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                    return;
//                }
//
//                String typeUser = user.getTypeUser(SplashActivity.this);
//
//                if (typeUser.equals(User.TYPE_USER_NONE) || typeUser.equals("")) {
//                    intent = new Intent(SplashActivity.this, LoginActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                } else {
//                    intent = new Intent(SplashActivity.this, SelectSearchActivity.class);
//                }
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);


    }
}
