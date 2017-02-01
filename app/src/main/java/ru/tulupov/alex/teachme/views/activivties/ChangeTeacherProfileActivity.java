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

import java.util.Map;

import ru.tulupov.alex.teachme.Constants;
import ru.tulupov.alex.teachme.MyApplications;
import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.user.User;
import ru.tulupov.alex.teachme.presenters.ChangeTeacherPresenter;
import ru.tulupov.alex.teachme.presenters.LoginPresenter;
import ru.tulupov.alex.teachme.views.fragments.CheckLoginEmailExisted;
import ru.tulupov.alex.teachme.views.fragments.RegDataCorrect;
import ru.tulupov.alex.teachme.views.fragments.RegTeacherAboutFragment;
import ru.tulupov.alex.teachme.views.fragments.RegTeacherAgreementFragment;
import ru.tulupov.alex.teachme.views.fragments.RegTeacherContactsFragment;
import ru.tulupov.alex.teachme.views.fragments.RegTeacherFullNameFragment;
import ru.tulupov.alex.teachme.views.fragments.RegTeacherSubjectsFragment;

public class ChangeTeacherProfileActivity extends AppCompatActivity implements ChangeTeacherView {

    private ChangeTeacherPresenter presenter;

    protected ImageButton nextFragment;
    protected ImageButton previousFragment;
    protected int registerStep = 0;
    protected LinearLayout nextPrevPanel;
    private String[] regFragmentTagsTeacher;
    private RegDataCorrect currRegDataCorrect;

    private boolean checkingLoginEmail = false;

    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_teacher_profile);

        presenter = new ChangeTeacherPresenter();
        presenter.onCreate(this);

        nextFragment = (ImageButton) findViewById(R.id.btn_register_teacher_ok);
        nextFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toNextFragmentTeacher();
            }
        });
        previousFragment = (ImageButton) findViewById(R.id.btn_register_teacher_prev);
        previousFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toPreviousFragmentTeacher();
            }
        });
        nextPrevPanel = (LinearLayout) findViewById(R.id.register_next_prev_panel);

        initFragment();
        initArrayTags();
    }

    private void initFragment() {

        User user = MyApplications.getUser();
        presenter.showTeacherFullName(user.getAccessToken(this));
    }

    private void initArrayTags() {
        regFragmentTagsTeacher = getResources().getStringArray(R.array.regFragmentTeacherTags);
    }

    @Override
    public void showDataFullName(Map map) {
        FragmentManager manager = getSupportFragmentManager();
        RegTeacherFullNameFragment fragment = new RegTeacherFullNameFragment();
        fragment.setMap(map);

        manager.beginTransaction()
                .add(R.id.reg_fragment_container, fragment)
                .addToBackStack("fullName")
                .commit();
    }

    @Override
    public void errorDataFullName() {
        Log.d(Constants.MY_TAG, "error errorDataFullName");
    }

    //    @Override
//    public void registerTeacherSuccess() {
//        pDialog.dismiss();
//        Log.d(Constants.MY_TAG, "registerTeacherSuccess");
//    }
//
//    @Override
//    public void registerTeacherError() {
//        pDialog.dismiss();
//    }

    protected void toNextFragmentTeacher() {

        if (registerStep == 5) {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("");
            pDialog.show();
//            presenter.registrationTeacher();
        }

        if (currRegDataCorrect.dataIsCorrect()) {
            if (registerStep == 1) {

            }

            if (registerStep == 2) {

            }

            if (registerStep == 3) {

            }

            if (registerStep == 4) {
                if (checkingLoginEmail) {
                    return;
                }
                checkingLoginEmail = true;
                RegTeacherContactsFragment fragment = (RegTeacherContactsFragment) currRegDataCorrect;
                String login = fragment.getLogin();
                String phone = fragment.getPhone();
                presenter.checkEmailAndLogin(login, "", phone);
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
        }

        return null;
    }

    @Override
    public void emailAndLoginIsChecked(int err) {
        checkingLoginEmail = false;
        CheckLoginEmailExisted fragment = (CheckLoginEmailExisted) currRegDataCorrect;
        fragment.showLoginEmailNotExisted();
        if (err == 0) {
            setUpNextFragmentTeacher();
        }

        if (err % 10 == 1) {
            fragment.showLoginExisted();
        }
        if (err / 10 == 1 || err / 10 == 11) {
            fragment.showEmailExisted();
        }
        if (err / 100 == 1) {
            fragment.showPhoneExisted();
        }
    }

    @Override
    public void emailAndLoginIsCheckedError() {
        checkingLoginEmail = false;
    }
}
