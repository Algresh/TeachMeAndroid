package ru.tulupov.alex.teachme.views.activivties;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import ru.tulupov.alex.teachme.Constants;
import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.presenters.LoginPresenter;
import ru.tulupov.alex.teachme.views.fragments.LoginFragments;
import ru.tulupov.alex.teachme.views.fragments.RegTeacherAboutFragment;
import ru.tulupov.alex.teachme.views.fragments.RegTeacherFullNameFragment;

public class LoginActivity extends AppCompatActivity implements LoginView, LoginFragments.LoginFragmentCallback {

    private LoginPresenter presenter;

    protected ImageButton nextFragment;
    protected ImageButton previousFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        presenter = new LoginPresenter();
        presenter.onCreate(this);

        nextFragment = (ImageButton) findViewById(R.id.btn_register_teacher_ok);
        nextFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toNextFragment();
            }
        });
        previousFragment = (ImageButton) findViewById(R.id.btn_register_teacher_prev);
        previousFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toPreviousFragment();
            }
        });

        initFragment();
    }

    private void initFragment() {
        FragmentManager manager = getSupportFragmentManager();
        RegTeacherFullNameFragment loginFragments = new RegTeacherFullNameFragment();
//        LoginFragments loginFragments = new LoginFragments();
        manager.beginTransaction()
                .add(R.id.reg_fragment_container, loginFragments)
                .addToBackStack("fullName")
                .commit();

    }

    @Override
    public void logInSuccess() {
        Log.d(Constants.MY_TAG, "URA");
    }

    @Override
    public void logInFail() {

    }

    @Override
    public void loginClick(String login, String password) {
        presenter.logIn(login, password, this);
    }

    protected void toNextFragment() {
        RegTeacherAboutFragment aboutFragment = new RegTeacherAboutFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.reg_fragment_container, aboutFragment)
                .addToBackStack("about")
                .commit();

    }

    protected void toPreviousFragment() {
        onBackPressed();
    }
}
