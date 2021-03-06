package ru.tulupov.alex.teachme.views.activivties;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.FileOutputStream;

import ru.tulupov.alex.teachme.Constants;
import ru.tulupov.alex.teachme.MyApplications;
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

import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.TYPE_SEARCH;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.TYPE_SEARCH_ALL;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            int color = ContextCompat.getColor(this, R.color.colorPrimary);
            window.setStatusBarColor(color);
        }

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

        registerStep++;
        Log.d(Constants.MY_TAG, "initFragment regStep: " + registerStep);
    }

    private void initArrayTags() {
        regFragmentTagsTeacher = getResources().getStringArray(R.array.regFragmentTeacherTags);
        regFragmentTagsPupil = getResources().getStringArray(R.array.regFragmentPupilTags);
    }

    @Override
    public void logInSuccess() {
        Intent intent = new Intent(this, ListTeachersActivity.class);
        intent.putExtra(TYPE_SEARCH, TYPE_SEARCH_ALL);
        startActivity(intent);
        finish();
    }

    @Override
    public void logInSuccessNotConfirm(String typeUser) {
        registerUserType = typeUser;
        FragmentManager manager = getSupportFragmentManager();
        RegConfirmationFragment confirmFragment = new RegConfirmationFragment();
        manager.beginTransaction()
                .replace(R.id.reg_fragment_container, confirmFragment)
                .addToBackStack("confirmation")
                .commit();
        registerStep++;
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
                .replace(R.id.reg_fragment_container, fragment, "confirmation")
                .addToBackStack("confirmation")
                .commit();
        registerStep++;
        Log.d(Constants.MY_TAG, "registerTeacherSuccess regStep: " + registerStep);
    }

    @Override
    public void registerTeacherError() {
        pDialog.dismiss();
    }

    @Override
    public void registerClick() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = getFragmentTeacherStep(registerStep);
        if (fragment != null) {
            currRegDataCorrect = (RegDataCorrect) fragment;
            manager.beginTransaction()
                    .replace(R.id.reg_fragment_container, fragment, regFragmentTagsTeacher[registerStep])
                    .addToBackStack(regFragmentTagsTeacher[registerStep - 1])
                    .commit();
            registerStep++;
            Log.d(Constants.MY_TAG, regFragmentTagsTeacher[registerStep- 1] + " |" + "| setUpNextFragmentTeacher regStep: " + registerStep);
        }
        nextPrevPanel.setVisibility(View.VISIBLE);
        registerUserType = User.TYPE_USER_TEACHER;

//        if (checkConnection()) {
//            FragmentManager manager = getSupportFragmentManager();
//            RegSelectTypeProfile fragment = new RegSelectTypeProfile();
//            manager.beginTransaction()
//                    .replace(R.id.reg_fragment_container, fragment, "selectProfile")
//                    .addToBackStack("selectProfile")
//                    .commit();
//            registerStep++;
//            Log.d(Constants.MY_TAG, "registerClick regStep: " + registerStep);
//        } else {
//            Toast.makeText(this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void forgotPasswordClick() {
        FragmentManager manager = getSupportFragmentManager();
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        manager.beginTransaction()
                .replace(R.id.reg_fragment_container, fragment, "forgotPassword")
                .addToBackStack("forgotPassword")
                .commit();
        registerStep++;
        Log.d(Constants.MY_TAG, "forgotPasswordClick regStep: " + registerStep);
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
        if (registerStep == 3 && currRegDataCorrect.dataIsCorrect()) {
            if (checkingLoginEmail) return;

            checkingLoginEmail = true;
            RegPupilContacts fragment = (RegPupilContacts) currRegDataCorrect;
            String email = fragment.getEmail();
            if (checkConnection()) {
                presenter.checkEmailAndLogin(email, "");
            } else {
                Toast.makeText(this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
            }
        }


        if (registerStep == 4) {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage(getString(R.string.pleaseWait));
            pDialog.show();
            if (checkConnection()) {
                presenter.registrationPupil();
            } else {
                Toast.makeText(this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
            }
        }

    }

    protected void toNextFragmentTeacher() {

        if (registerStep == 6) {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage(getString(R.string.pleaseWait));
            pDialog.show();

            if (checkConnection()) {
                presenter.registrationTeacher();
            } else {
                Toast.makeText(this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
            }
        }

        if (currRegDataCorrect.dataIsCorrect()) {
            if (registerStep == 5) {
                if (checkingLoginEmail) {
                    return;
                }
                checkingLoginEmail = true;
                RegTeacherContactsFragment fragment = (RegTeacherContactsFragment) currRegDataCorrect;
//                String login = fragment.getLogin();
                String email = fragment.getEmail();
                String phone = fragment.getPhone();
                if (checkConnection()) {
                    presenter.checkEmailAndLogin(email, phone);
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
                    .addToBackStack(regFragmentTagsTeacher[registerStep - 1])
                    .commit();
            registerStep++;
            Log.d(Constants.MY_TAG, regFragmentTagsTeacher[registerStep- 1] + " |" + "| setUpNextFragmentTeacher regStep: " + registerStep);
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
            Log.d(Constants.MY_TAG, "regStep: " + registerStep);
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
        Fragment confirmFragment = getSupportFragmentManager().findFragmentByTag("confirmation");
        if (confirmFragment != null && confirmFragment.isVisible()) return;
        super.onBackPressed();

        registerStep--;
        try {
            Log.d(Constants.MY_TAG, regFragmentTagsTeacher[registerStep - 1] + " back regStep: " + registerStep);
        } catch (ArrayIndexOutOfBoundsException ignore) {}

        if (registerStep == 0) {
            finish();
        }

        if (registerStep <= 1) {
            nextPrevPanel.setVisibility(View.GONE);
        } else {

            if (registerUserType != null && registerUserType.equals(User.TYPE_USER_TEACHER)) {
                nextPrevPanel.setVisibility(View.VISIBLE);
                currRegDataCorrect = (RegDataCorrect) getSupportFragmentManager()
                        .findFragmentByTag(regFragmentTagsTeacher[registerStep - 1]);
            } else if(registerUserType != null && registerUserType.equals(User.TYPE_USER_PUPIL)) {
                nextPrevPanel.setVisibility(View.VISIBLE);
                currRegDataCorrect = (RegDataCorrect) getSupportFragmentManager()
                        .findFragmentByTag(regFragmentTagsPupil[registerStep - 1]);
            }
        }

    }

    protected Fragment getFragmentTeacherStep (int step) {

        switch (step) {
            case 1:
                return new RegTeacherFullNameFragment();
            case 2:
                return new RegTeacherAboutFragment();
            case 3:
                return new RegTeacherSubjectsFragment();
            case 4:
                return new RegTeacherContactsFragment();
            case 5:
                RegTeacherAgreementFragment fragment = new RegTeacherAgreementFragment();
                Bundle args = new Bundle();
                args.putString("type", "teacher");
                fragment.setArguments(args);
                return fragment;
        }

        return null;
    }

    protected Fragment getFragmentPupilStep (int step) {

        switch (step) {
            case 2:
                return new RegPupilContacts();
            case 3:
                RegTeacherAgreementFragment fragment = new RegTeacherAgreementFragment();
                Bundle args = new Bundle();
                args.putString("type", "pupil");
                fragment.setArguments(args);
                return fragment;
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
        Intent intent = new Intent(this, ListTeachersActivity.class);
        intent.putExtra(TYPE_SEARCH, TYPE_SEARCH_ALL);
        startActivity(intent);
        finish();
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
                .addToBackStack("confirmation")
                .commit();
        registerStep++;
        Log.d(Constants.MY_TAG, "registerPupilSuccess regStep: " + registerStep);
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
        finish();
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
                .addToBackStack("confirmation")
                .commit();
        registerStep++;
    }

    @Override
    public void forgotPassEmailError() {
        Toast.makeText(this, R.string.somethingBroken, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void forgotPassConfirmSuccess() {
        Log.d(Constants.MY_TAG, "pass change " + registerStep);
        registerStep = registerStep - 3;
        initFragment();
        Toast.makeText(this, R.string.passwordChanges, Toast.LENGTH_SHORT).show();
        Log.d(Constants.MY_TAG, "pass change " + registerStep);

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
            Log.d(Constants.MY_TAG, "selectTeacher regStep: " + registerStep);
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
            Log.d(Constants.MY_TAG, "selectPupil regStep: " + registerStep);
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
