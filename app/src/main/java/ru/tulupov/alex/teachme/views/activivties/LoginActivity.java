package ru.tulupov.alex.teachme.views.activivties;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import ru.tulupov.alex.teachme.Constants;
import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.presenters.LoginPresenter;
import ru.tulupov.alex.teachme.views.fragments.LoginFragments;

public class LoginActivity extends AppCompatActivity implements LoginView, LoginFragments.LoginFragmentCallback {

    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        presenter = new LoginPresenter();
        presenter.onCreate(this);

        initFragment();
    }

    private void initFragment() {
        FragmentManager manager = getSupportFragmentManager();
        LoginFragments loginFragments = new LoginFragments();
        manager.beginTransaction()
                .add(R.id.activity_login, loginFragments)
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
}
