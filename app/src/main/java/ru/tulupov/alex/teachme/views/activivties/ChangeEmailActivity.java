package ru.tulupov.alex.teachme.views.activivties;

import android.os.Bundle;
import android.widget.Toast;

import ru.tulupov.alex.teachme.MyApplications;
import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.presenters.ChangeProfilePresenter;
import ru.tulupov.alex.teachme.views.fragments.ChangeEmailFragment;
import ru.tulupov.alex.teachme.views.fragments.RegConfirmationFragment;

public class ChangeEmailActivity extends BaseActivity implements ChangeProfileView,
        ChangeEmailFragment.ChangeEmailListener, RegConfirmationFragment.CodeConfirmation {

    protected ChangeProfilePresenter presenter;
    protected String newEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        presenter = new ChangeProfilePresenter();
        presenter.onCreate(this);


        ChangeEmailFragment fragment = new ChangeEmailFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_change_email, fragment, "changeEmail")
                .commit();

        initToolbar(getString(R.string.changeEmailTitle), R.id.toolbarChangeEmail);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void changedPasswordSuccess() {

    }

    @Override
    public void changedPasswordWrongOld() {

    }

    @Override
    public void changedPasswordWrongOther() {

    }

    @Override
    public void changedEmailSuccess() {
        RegConfirmationFragment fragment = new RegConfirmationFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_change_email, fragment, "confirmation")
                .commit();
    }

    @Override
    public void changedEmailWrongOther() {
        Toast.makeText(this, R.string.wrongOtherPassword, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void changedEmailConfirmSuccess() {
        Toast.makeText(this, R.string.emailModified, Toast.LENGTH_SHORT).show();
        MyApplications.getUser().setEmail(this, newEmail);
        finish();
    }

    @Override
    public void changedEmailConfirmWrongOther() {
        Toast.makeText(this, R.string.emailCodeWrong, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void changeEmail(String email, String accessToken) {
        if (checkConnection()) {
            newEmail = email;
            presenter.changeTeacherEmail(email, accessToken);
        } else {
            Toast.makeText(this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void codeConfirm(String code) {
        if (checkConnection()) {
            String accessToken = MyApplications.getUser().getAccessToken(this);
            presenter.changeTeacherEmailConfirmation(code, accessToken);
        } else {
            Toast.makeText(this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
        }
    }
}
