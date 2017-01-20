package ru.tulupov.alex.teachme.views.activivties;

import android.app.ProgressDialog;
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
import ru.tulupov.alex.teachme.models.PupilRegistration;
import ru.tulupov.alex.teachme.models.TeacherRegistration;
import ru.tulupov.alex.teachme.models.user.User;
import ru.tulupov.alex.teachme.presenters.LoginPresenter;
import ru.tulupov.alex.teachme.views.fragments.CheckLoginEmailExisted;
import ru.tulupov.alex.teachme.views.fragments.LoginFragments;
import ru.tulupov.alex.teachme.views.fragments.RegConfirmationFragment;
import ru.tulupov.alex.teachme.views.fragments.RegDataCorrect;
import ru.tulupov.alex.teachme.views.fragments.RegPupilContacts;
import ru.tulupov.alex.teachme.views.fragments.RegSelectTypeProfile;
import ru.tulupov.alex.teachme.views.fragments.RegTeacherAboutFragment;
import ru.tulupov.alex.teachme.views.fragments.RegTeacherAgreementFragment;
import ru.tulupov.alex.teachme.views.fragments.RegTeacherContactsFragment;
import ru.tulupov.alex.teachme.views.fragments.RegTeacherFullNameFragment;
import ru.tulupov.alex.teachme.views.fragments.RegTeacherSubjectsFragment;

public class LoginActivity extends AppCompatActivity
        implements LoginView, LoginFragments.LoginFragmentCallback,
        RegConfirmationFragment.CodeConfirmation, RegSelectTypeProfile.SelectTypeProfile {

    private LoginPresenter presenter;

    protected ImageButton nextFragment;
    protected ImageButton previousFragment;
    protected String registerUserType;
    protected int registerStep = 0;
    protected LinearLayout nextPrevPanel;
    private String[] regFragmentTagsTeacher;
    private String[] regFragmentTagsPupil;
    private  RegDataCorrect currRegDataCorrect;

    private boolean checkingLoginEmail = false;

    private ProgressDialog pDialog;

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
                if (registerUserType.equals(User.TYPE_USER_TEACHER)) {
                    toNextFragmentTeacher();
                } else {
                    toNextFragmentPupil();
                }

            }
        });
        previousFragment = (ImageButton) findViewById(R.id.btn_register_teacher_prev);
        previousFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (registerUserType.equals(User.TYPE_USER_TEACHER)) {
                    toPreviousFragmentTeacher();
                } else {

                }
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
        regFragmentTagsTeacher = getResources().getStringArray(R.array.regFragmentTeacherTags);
        regFragmentTagsPupil = getResources().getStringArray(R.array.regFragmentPupilTags);
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
        pDialog.dismiss();
        FragmentManager manager = getSupportFragmentManager();
        nextPrevPanel.setVisibility(View.GONE);
        Fragment fragment = new RegConfirmationFragment();
        manager.beginTransaction()
                .replace(R.id.reg_fragment_container, fragment)
                .commit();
    }

    @Override
    public void registerTeacherError() {
        pDialog.dismiss();
    }

    @Override
    public void registerClick() {
        FragmentManager manager = getSupportFragmentManager();
        RegSelectTypeProfile fragment = new RegSelectTypeProfile();
        manager.beginTransaction()
                .replace(R.id.reg_fragment_container, fragment, "selectProfile")
                .addToBackStack("selectProfile")
                .commit();
    }

    @Override
    public void forgotPasswordClick() {

    }

    @Override
    public void loginClick(String login, String password) {
        presenter.logIn(login, password, this);
    }

    protected void toNextFragmentPupil () {
        if (registerStep == 1 && currRegDataCorrect.dataIsCorrect()) {
            if (checkingLoginEmail) return;

            checkingLoginEmail = true;
            RegPupilContacts fragment = (RegPupilContacts) currRegDataCorrect;
            String login = fragment.getLogin();
            String email = fragment.getEmail();
            presenter.checkEmailAndLogin(login, email);
        }


        if (registerStep == 2) {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("");
            pDialog.show();
            presenter.registrationPupil();
        }

    }

    protected void toNextFragmentTeacher() {

        if (registerStep == 5) {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("");
            pDialog.show();
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
                setUpNextFragmentTeacher();
            }
        }
    }

    protected void setUpNextFragmentTeacher() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = getFragmentTeacherStep(registerStep);
        if (fragment != null) {
            currRegDataCorrect = (RegDataCorrect) fragment;
            manager.beginTransaction()
                    .replace(R.id.reg_fragment_container, fragment, regFragmentTagsTeacher[registerStep])
                    .addToBackStack(regFragmentTagsTeacher[registerStep])
                    .commit();
            registerStep++;
        }
    }

    protected void setUpNextFragmentPupil() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = getFragmentPupilStep(registerStep);
        if (fragment != null) {
            currRegDataCorrect = (RegDataCorrect) fragment;
            manager.beginTransaction()
                    .replace(R.id.reg_fragment_container, fragment, regFragmentTagsPupil[registerStep])
                    .addToBackStack(regFragmentTagsPupil[registerStep])
                    .commit();
            registerStep++;
        }
    }

    protected void toPreviousFragmentTeacher() {
        registerStep--;
        if (registerStep == 0) {
            nextPrevPanel.setVisibility(View.GONE);
        } else {
            currRegDataCorrect = (RegDataCorrect) getSupportFragmentManager()
                    .findFragmentByTag(regFragmentTagsTeacher[registerStep - 1]);
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

    protected Fragment getFragmentPupilStep (int step) {

        switch (step) {
            case 0:
                return new RegPupilContacts();
            case 1:
                return new RegTeacherAgreementFragment();
        }

        return null;
    }

    @Override
    public void codeConfirm(String code) {
        if (registerUserType.equals( User.TYPE_USER_TEACHER)) {
            presenter.registerConfirmationTeacher(code, this);
        }else if (registerUserType.equals( User.TYPE_USER_PUPIL)) {
            presenter.registerConfirmationPupil(code, this);
        }
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
        CheckLoginEmailExisted fragment = (CheckLoginEmailExisted) currRegDataCorrect;
        fragment.showLoginEmailNotExisted();
        if (type.equals(ModelUserInfo.TYPE_CORRECT_BOTH)) {
            if (registerUserType.equals( User.TYPE_USER_TEACHER)) {
                setUpNextFragmentTeacher();
            } else if (registerUserType.equals( User.TYPE_USER_PUPIL)) {
                setUpNextFragmentPupil();
            }
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

    @Override
    public void registerPupilSuccess() {
        pDialog.dismiss();
        FragmentManager manager = getSupportFragmentManager();
        nextPrevPanel.setVisibility(View.GONE);
        Fragment fragment = new RegConfirmationFragment();
        manager.beginTransaction()
                .replace(R.id.reg_fragment_container, fragment)
                .commit();
    }

    @Override
    public void registerPupilError() {
        pDialog.dismiss();
    }

    @Override
    public void registerConfirmPupilSuccess() {
        Log.d(Constants.MY_TAG, "full registration success pupil");
    }

    @Override
    public void registerConfirmPupilError() {

    }

    @Override
    public void selectTeacher() {
        registerUserType = User.TYPE_USER_TEACHER;

        FragmentManager manager = getSupportFragmentManager();
        RegTeacherFullNameFragment fragment = new RegTeacherFullNameFragment();
        TeacherRegistration.clearInstance();
        PupilRegistration.clearInstance();

        currRegDataCorrect = fragment;
        manager.beginTransaction()
                .replace(R.id.reg_fragment_container, fragment, "fullName")
                .addToBackStack("fullName")
                .commit();
        nextPrevPanel.setVisibility(View.VISIBLE);

        registerStep++;
    }

    @Override
    public void selectPupil() {
        registerUserType = User.TYPE_USER_PUPIL;

        FragmentManager manager = getSupportFragmentManager();
        RegPupilContacts fragment = new RegPupilContacts();
        TeacherRegistration.clearInstance();
        PupilRegistration.clearInstance();

        currRegDataCorrect = fragment;
        manager.beginTransaction()
                .replace(R.id.reg_fragment_container, fragment, "contactsPupil")
                .addToBackStack("contactsPupil")
                .commit();
        nextPrevPanel.setVisibility(View.VISIBLE);

        registerStep++;
    }
}
