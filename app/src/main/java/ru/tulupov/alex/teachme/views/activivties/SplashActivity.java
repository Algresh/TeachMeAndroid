package ru.tulupov.alex.teachme.views.activivties;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import ru.tulupov.alex.teachme.MyApplications;
import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.user.User;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        User user = MyApplications.getUser();

        if (user == null) {
            user = ((MyApplications) getApplication()).initUser();
            MyApplications.setUser(user);
        }

        String typeUSer = user.getTypeUser(this);
        Intent intent;
        if (typeUSer.equals(User.TYPE_USER_NONE)) {
            intent = new Intent(this, LoginActivity.class);
        } else {
            intent = new Intent(this, SelectSearchActivity.class);
        }
        startActivity(intent);

    }
}
