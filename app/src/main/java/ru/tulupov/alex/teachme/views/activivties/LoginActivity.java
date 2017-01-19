package ru.tulupov.alex.teachme.views.activivties;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.FileOutputStream;

import ru.tulupov.alex.teachme.Constants;
import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.ModelUserInfo;
import ru.tulupov.alex.teachme.models.TeacherRegistration;
import ru.tulupov.alex.teachme.presenters.LoginPresenter;
import ru.tulupov.alex.teachme.views.fragments.LoginFragments;
import ru.tulupov.alex.teachme.views.fragments.RegConfirmationFragment;
import ru.tulupov.alex.teachme.views.fragments.RegDataCorrect;
import ru.tulupov.alex.teachme.views.fragments.RegTeacherAboutFragment;
import ru.tulupov.alex.teachme.views.fragments.RegTeacherAgreementFragment;
import ru.tulupov.alex.teachme.views.fragments.RegTeacherContactsFragment;
import ru.tulupov.alex.teachme.views.fragments.RegTeacherFullNameFragment;
import ru.tulupov.alex.teachme.views.fragments.RegTeacherSubjectsFragment;

public class LoginActivity extends AppCompatActivity
        implements LoginView, LoginFragments.LoginFragmentCallback, RegConfirmationFragment.CodeConfirmation {

    private LoginPresenter presenter;

    protected ImageButton nextFragment;
    protected ImageButton previousFragment;
    protected String registerUserType;
    protected int registerStep = 0;
    protected LinearLayout nextPrevPanel;
    private String[] regFragmentTags;
    private  RegDataCorrect currRegDataCorrect;

    private boolean checkingLoginEmail = false;

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
    public void registerTeacherSuccess() {
        FragmentManager manager = getSupportFragmentManager();
        nextPrevPanel.setVisibility(View.GONE);
        Fragment fragment = new RegConfirmationFragment();
        manager.beginTransaction()
                .replace(R.id.reg_fragment_container, fragment)
                .commit();
    }

    @Override
    public void registerTeacherError() {

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

        if (registerStep == 5) {
            presenter.registrationTeacher();
        }

        if (currRegDataCorrect.dataIsCorrect()) {
            if (registerStep == 4) {
                if (checkingLoginEmail) {
                    return;
                }
                checkingLoginEmail = true;
                RegTeacherContactsFragment fragment = (RegTeacherContactsFragment) currRegDataCorrect;
                String login = fragment.getLogin();
                String email = fragment.getEmail();
                presenter.checkEmailAndLogin(login, email);
            } else {
                setUpNextFragment();
            }
        }
    }

    protected void setUpNextFragment() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = getFragmentTeacherStep(registerStep);
        if (fragment != null) {
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
            case 4:
                return new RegTeacherAgreementFragment();
        }

        return null;
    }

    @Override
    public void codeConfirm(String code) {
        presenter.registerConfirmationTeacher(code, this);
    }

    @Override
    public void registerConfirmTeacherSuccess() {
        Log.d(Constants.MY_TAG, "full registration success");
    }

    @Override
    public void registerConfirmTeacherError() {

    }

    @Override
    public void emailAndLoginIsChecked(String type) {
        checkingLoginEmail = false;
        RegTeacherContactsFragment fragment = (RegTeacherContactsFragment) currRegDataCorrect;
        fragment.showLoginEmailNotExisted();
        if (type.equals(ModelUserInfo.TYPE_CORRECT_BOTH)) {
            setUpNextFragment();
        }
        if (type.equals(ModelUserInfo.TYPE_ERROR_EMAIL)) {
            fragment.showEmailExisted();
        }
        if (type.equals(ModelUserInfo.TYPE_ERROR_LOGIN)) {
            fragment.showLoginExisted();
        }
        if (type.equals(ModelUserInfo.TYPE_ERROR_BOTH)) {
            fragment.showLoginExisted();
            fragment.showEmailExisted();

        }
    }

    @Override
    public void emailAndLoginIsCheckedError() {
        checkingLoginEmail = false;
    }
}
