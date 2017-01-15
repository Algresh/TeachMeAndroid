package ru.tulupov.alex.teachme.views.activivties;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import ru.tulupov.alex.teachme.Constants;
import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.TeacherRegistration;
import ru.tulupov.alex.teachme.presenters.LoginPresenter;
import ru.tulupov.alex.teachme.views.fragments.LoginFragments;
import ru.tulupov.alex.teachme.views.fragments.RegDataCorrect;
import ru.tulupov.alex.teachme.views.fragments.RegTeacherAboutFragment;
import ru.tulupov.alex.teachme.views.fragments.RegTeacherContactsFragment;
import ru.tulupov.alex.teachme.views.fragments.RegTeacherFullNameFragment;
import ru.tulupov.alex.teachme.views.fragments.RegTeacherSubjectsFragment;

public class LoginActivity extends AppCompatActivity implements LoginView, LoginFragments.LoginFragmentCallback {

    private LoginPresenter presenter;

    protected ImageButton nextFragment;
    protected ImageButton previousFragment;
    protected String registerUserType;
    protected int registerStep = 0;
    protected LinearLayout nextPrevPanel;
    private String[] regFragmentTags;
    private  RegDataCorrect currRegDataCorrect;

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
        nextPrevPanel = (LinearLayout) findViewById(R.id.register_next_prev_panel);

        initFragment();
        initArrayTags();
    }

    private void initFragment() {
        FragmentManager manager = getSupportFragmentManager();
        LoginFragments loginFragments = new LoginFragments();
//        LoginFragments loginFragments = new LoginFragments();
        manager.beginTransaction()
                .add(R.id.reg_fragment_container, loginFragments)
                .addToBackStack("login")
                .commit();

    }

    private void initArrayTags() {
        regFragmentTags = getResources().getStringArray(R.array.regFragmentTeacherTags);
    }

    @Override
    public void logInSuccess() {
        Log.d(Constants.MY_TAG, "URA");
    }

    @Override
    public void logInFail() {

    }

    @Override
    public void registerClick() {
        FragmentManager manager = getSupportFragmentManager();
        RegTeacherFullNameFragment loginFragments = new RegTeacherFullNameFragment();
//        LoginFragments loginFragments = new LoginFragments();
        currRegDataCorrect = loginFragments;
        manager.beginTransaction()
                .replace(R.id.reg_fragment_container, loginFragments, "fullName")
                .addToBackStack("fullName")
                .commit();
        nextPrevPanel.setVisibility(View.VISIBLE);

        registerStep++;
    }

    @Override
    public void forgotPasswordClick() {

    }

    @Override
    public void loginClick(String login, String password) {
        presenter.logIn(login, password, this);
    }

    protected void toNextFragment() {
        FragmentManager manager = getSupportFragmentManager();
        if (currRegDataCorrect.dataIsCorrect()) {
            Fragment fragment = getFragmentTeacherStep(registerStep);
            currRegDataCorrect = (RegDataCorrect) fragment;
            manager.beginTransaction()
                    .replace(R.id.reg_fragment_container, fragment, regFragmentTags[registerStep])
                    .addToBackStack(regFragmentTags[registerStep])
                    .commit();
            registerStep++;
        }
    }

    protected void toPreviousFragment() {
        registerStep--;
        if (registerStep == 0) {
            nextPrevPanel.setVisibility(View.GONE);
        } else {
            currRegDataCorrect = (RegDataCorrect) getSupportFragmentManager()
                    .findFragmentByTag(regFragmentTags[registerStep - 1]);
        }

        onBackPressed();
    }

    protected Fragment getFragmentTeacherStep (int step) {

        switch (step) {
            case 0:
                return new RegTeacherFullNameFragment();
            case 1:
                return new RegTeacherAboutFragment();
            case 2:
                return new RegTeacherSubjectsFragment();
            case 3:
                return new RegTeacherContactsFragment();
        }

        return null;
    }
}
