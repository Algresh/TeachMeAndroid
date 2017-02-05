package ru.tulupov.alex.teachme.views.activivties;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.FileOutputStream;

import ru.tulupov.alex.teachme.Constants;
import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.ModelUserInfo;
import ru.tulupov.alex.teachme.models.PupilRegistration;
import ru.tulupov.alex.teachme.models.TeacherRegistration;
import ru.tulupov.alex.teachme.models.user.User;
import ru.tulupov.alex.teachme.presenters.LoginPresenter;
import ru.tulupov.alex.teachme.views.fragments.CheckLoginEmailExisted;
import ru.tulupov.alex.teachme.views.fragments.ForgotPasswordFragment;
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
        RegConfirmationFragment.CodeConfirmation, RegSelectTypeProfile.SelectTypeProfile,
        ForgotPasswordFragment.ForgotPass {

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

    private String forgotPasswordNewPass;
    private String forgotPasswordType;
    private String forgotPasswordEmail;

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
                    toPreviousPupilTeacher();
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
        Intent intent = new Intent(this, SelectSearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void logInFail() {
        Toast.makeText(this, R.string.loginOrPasswordError, Toast.LENGTH_SHORT).show();
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
        if (checkConnection()) {
            FragmentManager manager = getSupportFragmentManager();
            RegSelectTypeProfile fragment = new RegSelectTypeProfile();
            manager.beginTransaction()
                    .replace(R.id.reg_fragment_container, fragment, "selectProfile")
                    .addToBackStack("selectProfile")
                    .commit();
        } else {
            Toast.makeText(this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void forgotPasswordClick() {
        FragmentManager manager = getSupportFragmentManager();
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        manager.beginTransaction()
                .replace(R.id.reg_fragment_container, fragment, "forgotPassword")
                .addToBackStack("forgotPassword")
                .commit();
    }

    @Override
    public void loginClick(String login, String password) {
        if (checkConnection()) {
            presenter.logIn(login, password, this);
        } else {
            Toast.makeText(this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
        }

    }

    protected void toNextFragmentPupil () {
        if (registerStep == 1 && currRegDataCorrect.dataIsCorrect()) {
            if (checkingLoginEmail) return;

            checkingLoginEmail = true;
            RegPupilContacts fragment = (RegPupilContacts) currRegDataCorrect;
            String login = fragment.getLogin();
            String email = fragment.getEmail();
            if (checkConnection()) {
                presenter.checkEmailAndLogin(login, email, "");
            } else {
                Toast.makeText(this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
            }
        }


        if (registerStep == 2) {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("");
            pDialog.show();
            if (checkConnection()) {
                presenter.registrationPupil();
            } else {
                Toast.makeText(this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
            }
        }

    }

    protected void toNextFragmentTeacher() {

        if (registerStep == 5) {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("");
            pDialog.show();

            if (checkConnection()) {
                presenter.registrationTeacher();
            } else {
                Toast.makeText(this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
            }
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
                String phone = fragment.getPhone();
                if (checkConnection()) {
                    presenter.checkEmailAndLogin(login, email, phone);
                } else {
                    Toast.makeText(this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
                }

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
        onBackPressed();
    }

    protected void toPreviousPupilTeacher() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        registerStep--;

        if (registerStep == 0) {
            nextPrevPanel.setVisibility(View.GONE);
        } else {
            if (registerUserType != null && registerUserType.equals(User.TYPE_USER_TEACHER)) {
                currRegDataCorrect = (RegDataCorrect) getSupportFragmentManager()
                        .findFragmentByTag(regFragmentTagsTeacher[registerStep - 1]);
            } else if(registerUserType != null && registerUserType.equals(User.TYPE_USER_PUPIL)) {
                currRegDataCorrect = (RegDataCorrect) getSupportFragmentManager()
                        .findFragmentByTag(regFragmentTagsPupil[registerStep - 1]);
            }
        }

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
        if (checkConnection()) {
            if (registerUserType != null && registerUserType.equals( User.TYPE_USER_TEACHER)) {
                presenter.registerConfirmationTeacher(code, this);
            } else if (registerUserType != null && registerUserType.equals( User.TYPE_USER_PUPIL)) {
                presenter.registerConfirmationPupil(code, this);
            } else {
                presenter.forgotPasswordConfirm(forgotPasswordEmail, forgotPasswordType,
                        forgotPasswordNewPass, code);
            }
        } else {
            Toast.makeText(this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void registerConfirmTeacherSuccess() {
        Log.d(Constants.MY_TAG, "full registration success");
        Intent intent = new Intent(this, SelectSearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void registerConfirmTeacherError() {
        Toast.makeText(this, R.string.somethingBroken, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void emailAndLoginIsChecked(int err) {
        checkingLoginEmail = false;
        CheckLoginEmailExisted fragment = (CheckLoginEmailExisted) currRegDataCorrect;
        fragment.showLoginEmailNotExisted();
        if (err == 0) {
            if (registerUserType.equals(User.TYPE_USER_TEACHER)) {
                setUpNextFragmentTeacher();
            } else if (registerUserType.equals(User.TYPE_USER_PUPIL)) {
                setUpNextFragmentPupil();
            }
        }

        if (err % 10 == 1) {
            fragment.showLoginExisted();
        }
        if (err / 10 == 1 || err / 10 == 11) {
            fragment.showEmailExisted();
        }
        if (err / 100 == 1) {
            if (registerUserType.equals(User.TYPE_USER_TEACHER)) {
                fragment.showPhoneExisted();
            } else if (registerUserType.equals(User.TYPE_USER_PUPIL)) {
                setUpNextFragmentPupil();
            }
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
        Intent intent = new Intent(this, SelectSearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void registerConfirmPupilError() {
        Toast.makeText(this, R.string.somethingBroken, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void forgotPassEmailSuccess(String typeUser) {
        forgotPasswordType = typeUser;

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = new RegConfirmationFragment();
        manager.beginTransaction()
                .replace(R.id.reg_fragment_container, fragment)
                .commit();
    }

    @Override
    public void forgotPassEmailError() {
        Toast.makeText(this, R.string.somethingBroken, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void forgotPassConfirmSuccess() {
        Log.d(Constants.MY_TAG, "pass change");
    }

    @Override
    public void forgotPassConfirmError() {
        Toast.makeText(this, R.string.somethingBroken, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void checkEmail(String email, String pass) {
        forgotPasswordEmail = email;
        forgotPasswordNewPass = pass;
        if (checkConnection()) {
            presenter.forgotPassword(email);
        } else {
            Toast.makeText(this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void selectTeacher() {
        if (checkConnection()) {
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
        } else {
            Toast.makeText(this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void selectPupil() {
        if (checkConnection()) {
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
        } else {
            Toast.makeText(this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
        }
    }

    protected boolean checkConnection() {
        ConnectivityManager connectChecker = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectChecker.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = connectChecker.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        return false;
    }


}
