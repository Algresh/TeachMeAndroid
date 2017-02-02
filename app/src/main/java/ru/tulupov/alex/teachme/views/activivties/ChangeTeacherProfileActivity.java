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
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import ru.tulupov.alex.teachme.Constants;
import ru.tulupov.alex.teachme.MyApplications;
import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.PriceList;
import ru.tulupov.alex.teachme.models.Teacher;
import ru.tulupov.alex.teachme.models.user.TeacherUser;
import ru.tulupov.alex.teachme.models.user.User;
import ru.tulupov.alex.teachme.presenters.ChangeTeacherPresenter;
import ru.tulupov.alex.teachme.presenters.LoginPresenter;
import ru.tulupov.alex.teachme.views.fragments.ChangeTeacherContactsFragment;
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
        currRegDataCorrect = fragment;
        manager.beginTransaction()
                .add(R.id.reg_fragment_container, fragment)
                .addToBackStack("fullName")
                .commit();

        registerStep++;
    }

    @Override
    public void errorDataFullName() {
        Log.d(Constants.MY_TAG, "error errorDataFullName");
    }

    protected void toNextFragmentTeacher() {

        if (currRegDataCorrect.dataIsCorrect()) {
            if (registerStep == 1) {
                RegTeacherFullNameFragment fragment = (RegTeacherFullNameFragment) currRegDataCorrect;
                Map<String, String> map = fragment.getDataMap();
                String accessToken = MyApplications.getUser().getAccessToken(this);
                if (fragment.isPhotoChanged()) {
                    int userId = MyApplications.getUser().getUserId(this);
                    Log.d(Constants.MY_TAG, userId + "");
                    presenter.changeTeacherFullName(accessToken, fragment.getBitmapPhoto(), userId, map);
                } else {
                    presenter.changeTeacherFullName(accessToken, map);
                }
            }

            if (registerStep == 2) {
                RegTeacherAboutFragment fragment = (RegTeacherAboutFragment) currRegDataCorrect;
                String description = fragment.getTextAbout();
                String accessToken = MyApplications.getUser().getAccessToken(this);
                presenter.changeTeacherDescription(accessToken, description);
            }

            if (registerStep == 3) {
                RegTeacherSubjectsFragment fragment = (RegTeacherSubjectsFragment) currRegDataCorrect;
                List<PriceList> priceLists = fragment.getPriceLists();
                String accessToken = MyApplications.getUser().getAccessToken(this);
                presenter.changeTeacherSubjects(accessToken, priceLists);
            }

            if (registerStep == 4) {
                if (checkingLoginEmail) {
                    return;
                }
                checkingLoginEmail = true;
                ChangeTeacherContactsFragment fragment = (ChangeTeacherContactsFragment) currRegDataCorrect;
                String login = "";
                String phone = "";

                if (fragment.isChangedLogin()) {
                    login = fragment.getLogin();
                }

                if (fragment.isChangedPhone()) {
                    phone = fragment.getPhone();
                }

                presenter.checkEmailAndLogin(login, "", phone);
            }
        }
    }

    @Override
    public void changeFullNameSuccess(Map map) {
        String firstName = (String) map.get("firstName");
        String lastName = (String) map.get("lastName");
        String fatherName = (String) map.get("fatherName");
        Double cityId = (Double) map.get("cityId");
        String cityTitle = (String) map.get("cityTitle");
        Boolean cityHasSub = (Boolean) map.get("cityHasSub");
        String photo = (String) map.get("photo");

        TeacherUser teacherUser = (TeacherUser) MyApplications.getUser();
        teacherUser.setCityId(this, cityId.intValue());
        teacherUser.setCityTitle(this, cityTitle.replace("\0", " "));
        teacherUser.setFirstName(this, firstName.replace("\0", " "));
        teacherUser.setLastName(this, lastName.replace("\0", " "));
        teacherUser.setFatherName(this, fatherName.replace("\0", " "));
        teacherUser.setPhotoSrc(this, photo);
        teacherUser.setCityHasSub(this, cityHasSub);

        presenter.showTeacherDescription(teacherUser.getAccessToken(this));
    }

    @Override
    public void changeFullNameError() {

    }

    @Override
    public void showDataContacts(Map map) {
        FragmentManager manager = getSupportFragmentManager();
        ChangeTeacherContactsFragment fragment = new ChangeTeacherContactsFragment();
        fragment.setMap(map);

        currRegDataCorrect = fragment;
        manager.beginTransaction()
                .replace(R.id.reg_fragment_container, fragment, regFragmentTagsTeacher[registerStep])
                .addToBackStack("contacts")
                .commit();
        registerStep++;
    }

    @Override
    public void errorDataContacts() {

    }

    @Override
    public void showDataDescription(String description) {
        FragmentManager manager = getSupportFragmentManager();
        RegTeacherAboutFragment fragment = new RegTeacherAboutFragment();
        fragment.setTextAbout(description);

        currRegDataCorrect = fragment;
        manager.beginTransaction()
                .replace(R.id.reg_fragment_container, fragment, regFragmentTagsTeacher[registerStep])
                .addToBackStack("description")
                .commit();
        registerStep++;
    }

    @Override
    public void errorDataDescription() {

    }

    @Override
    public void showDataSubjects(List<PriceList> list) {
        FragmentManager manager = getSupportFragmentManager();
        RegTeacherSubjectsFragment fragment = new RegTeacherSubjectsFragment();
        fragment.setPriceLists(list);
        fragment.isUpdating();

        currRegDataCorrect = fragment;
        manager.beginTransaction()
                .replace(R.id.reg_fragment_container, fragment, regFragmentTagsTeacher[registerStep])
                .addToBackStack("subjects")
                .commit();
        registerStep++;
    }

    @Override
    public void errorDataSubjects() {

    }

    @Override
    public void changeContactsSuccess(Map map) {
        String login = (String) map.get("login");

        TeacherUser teacherUser = (TeacherUser) MyApplications.getUser();
        teacherUser.setLogin(this, login.replace("\0", " "));
        Toast.makeText(this, getString(R.string.allChangesSave), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void changeContactsError() {

    }

    @Override
    public void changeDescriptionSuccess() {
        String accessToken = MyApplications.getUser().getAccessToken(this);
        presenter.showTeacherSubjects(accessToken);
    }

    @Override
    public void changeDescriptionError() {

    }

    @Override
    public void changeSubjectsSuccess() {
        String accessToken = MyApplications.getUser().getAccessToken(this);
        presenter.showTeacherContacts(accessToken);
    }

    @Override
    public void changeSubjectsError() {

    }

//    protected void setUpNextFragmentTeacher() {
//        FragmentManager manager = getSupportFragmentManager();
//        Fragment fragment = getFragmentTeacherStep(registerStep);
//        if (fragment != null) {
//            currRegDataCorrect = (RegDataCorrect) fragment;
//            manager.beginTransaction()
//                    .replace(R.id.reg_fragment_container, fragment, regFragmentTagsTeacher[registerStep])
//                    .addToBackStack(regFragmentTagsTeacher[registerStep])
//                    .commit();
//            registerStep++;
//        }
//    }

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

//    protected Fragment getFragmentTeacherStep (int step) {
//
//        switch (step) {
//            case 0:
//                return new RegTeacherFullNameFragment();
//            case 1:
//                return new RegTeacherAboutFragment();
//            case 2:
//                return new RegTeacherSubjectsFragment();
//            case 3:
//                return new RegTeacherContactsFragment();
//        }
//
//        return null;
//    }

    @Override
    public void emailAndLoginIsChecked(int err) {
        checkingLoginEmail = false;
        ChangeTeacherContactsFragment fragment = (ChangeTeacherContactsFragment) currRegDataCorrect;
        fragment.showLoginEmailNotExisted();
        if (err == 0) {
            Map<String, String> map = fragment.getDataMap();
            String accessToken = MyApplications.getUser().getAccessToken(this);
            presenter.changeTeacherContacts(accessToken, map);

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
