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

import java.util.List;
import java.util.Map;

import ru.tulupov.alex.teachme.Constants;
import ru.tulupov.alex.teachme.MyApplications;
import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.ContactsBlock;
import ru.tulupov.alex.teachme.models.FullNameBlock;
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

public class ChangeTeacherProfileActivity extends BaseActivity implements ChangeTeacherView {

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
        initToolbar(getString(R.string.changeProfileTitle), R.id.toolbarChangeTeacherProfile);
    }

    private void initFragment() {
        if (checkConnection()) {
            User user = MyApplications.getUser();
            presenter.showTeacherFullName(user.getAccessToken(this));
        } else {
            Toast.makeText(this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
        }
    }

    private void initArrayTags() {
        regFragmentTagsTeacher = getResources().getStringArray(R.array.changeFragmentTeacherTags);
    }

    @Override
    public void showDataFullName(FullNameBlock block) {
        FragmentManager manager = getSupportFragmentManager();
        RegTeacherFullNameFragment fragment = new RegTeacherFullNameFragment();
        fragment.setFullNameBlock(block);
        currRegDataCorrect = fragment;
        manager.beginTransaction()
                .add(R.id.reg_fragment_container, fragment, "fullName")
                .addToBackStack("fullName")
                .commit();

        registerStep++;
    }

    @Override
    public void errorDataFullName() {
        Log.d(Constants.MY_TAG, "error errorDataFullName");
    }

    protected void toNextFragmentTeacher() {

        if (checkConnection()) {
            if (currRegDataCorrect.dataIsCorrect()) {
                pDialog = new ProgressDialog(this);
                pDialog.setMessage(getString(R.string.pleaseWait));
                pDialog.show();

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
                        pDialog.dismiss();
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
        } else {
            Toast.makeText(this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public void changeFullNameSuccess(FullNameBlock block) {
        Log.d(Constants.MY_TAG, block.toString());
        String firstName = block.getFirstName();
        String lastName = block.getLastName();
        String fatherName = block.getFatherName();
        int cityId = block.getCity().getId();
        String cityTitle =block.getCity().getTitle();
        boolean cityHasSub = block.getCity().isHasSubway();
        String photo = block.getPhoto();

        TeacherUser teacherUser = (TeacherUser) MyApplications.getUser();
        teacherUser.setCityId(this, cityId);
        teacherUser.setCityTitle(this, cityTitle);
        teacherUser.setFirstName(this, firstName);
        teacherUser.setLastName(this, lastName);
        teacherUser.setFatherName(this, fatherName);
        teacherUser.setPhotoSrc(this, photo);
        teacherUser.setCityHasSub(this, cityHasSub);

        Toast.makeText(this, getString(R.string.allChangesSave), Toast.LENGTH_SHORT).show();
        presenter.showTeacherDescription(teacherUser.getAccessToken(this));
    }

    @Override
    public void changeFullNameError() {
        if (pDialog != null) pDialog.dismiss();
    }

    @Override
    public void showDataContacts(ContactsBlock block) {
        FragmentManager manager = getSupportFragmentManager();
        ChangeTeacherContactsFragment fragment = new ChangeTeacherContactsFragment();
        fragment.setContactsBlock(block);

        currRegDataCorrect = fragment;
        manager.beginTransaction()
                .replace(R.id.reg_fragment_container, fragment, regFragmentTagsTeacher[registerStep])
                .addToBackStack("contacts")
                .commit();
        if (pDialog != null) pDialog.dismiss();
        registerStep++;
    }

    @Override
    public void errorDataContacts() {
        if (pDialog != null) pDialog.dismiss();
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
        if (pDialog != null) pDialog.dismiss();
        registerStep++;
    }

    @Override
    public void errorDataDescription() {
        if (pDialog != null) pDialog.dismiss();
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
        if (pDialog != null) pDialog.dismiss();
        registerStep++;
    }

    @Override
    public void errorDataSubjects() {
        if (pDialog != null) pDialog.dismiss();
    }

    @Override
    public void changeContactsSuccess(ContactsBlock block) {
        String login = block.getLogin();

        TeacherUser teacherUser = (TeacherUser) MyApplications.getUser();
        teacherUser.setLogin(this, login);
        Toast.makeText(this, getString(R.string.allChangesSave), Toast.LENGTH_SHORT).show();
        if (pDialog != null) pDialog.dismiss();
        finish();
    }

    @Override
    public void changeContactsError() {
        if (pDialog != null) pDialog.dismiss();
    }

    @Override
    public void changeDescriptionSuccess() {
        if (checkConnection()) {
            String accessToken = MyApplications.getUser().getAccessToken(this);
            presenter.showTeacherSubjects(accessToken);
            Toast.makeText(this, getString(R.string.allChangesSave), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void changeDescriptionError() {
        if (pDialog != null) pDialog.dismiss();
    }

    @Override
    public void changeSubjectsSuccess() {
        if (checkConnection()) {
            String accessToken = MyApplications.getUser().getAccessToken(this);
            presenter.showTeacherContacts(accessToken);
            Toast.makeText(this, getString(R.string.allChangesSave), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void changeSubjectsError() {

    }

    protected void toPreviousFragmentTeacher() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        registerStep--;

        if (registerStep == 0) {
            finish();
        } else {
            currRegDataCorrect = (RegDataCorrect) getSupportFragmentManager()
                    .findFragmentByTag(regFragmentTagsTeacher[registerStep - 1]);
        }
    }

    @Override
    public void emailAndLoginIsChecked(int err) {
        checkingLoginEmail = false;
        ChangeTeacherContactsFragment fragment = (ChangeTeacherContactsFragment) currRegDataCorrect;
        fragment.showLoginEmailNotExisted();
        if (err == 0) {
            if (checkConnection()) {
                Map<String, String> map = fragment.getDataMap();
                String accessToken = MyApplications.getUser().getAccessToken(this);
                presenter.changeTeacherContacts(accessToken, map);
            } else {
                Toast.makeText(this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
            }
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
