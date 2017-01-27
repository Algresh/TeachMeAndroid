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
        Intent intent;

        if (user == null) {
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        String typeUser = user.getTypeUser(this);

        if (typeUser.equals(User.TYPE_USER_NONE)) {
            intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        } else {
            intent = new Intent(this, SelectSearchActivity.class);
        }
        startActivity(intent);
        finish();

    }
}
