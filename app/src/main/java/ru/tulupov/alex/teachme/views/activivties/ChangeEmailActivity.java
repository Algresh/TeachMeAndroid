package ru.tulupov.alex.teachme.views.activivties;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import ru.tulupov.alex.teachme.MyApplications;
import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.presenters.ChangeProfilePresenter;
import ru.tulupov.alex.teachme.views.fragments.ChangeEmailFragment;
import ru.tulupov.alex.teachme.views.fragments.RegConfirmationFragment;

public class ChangeEmailActivity extends AppCompatActivity implements ChangeProfileView,
        ChangeEmailFragment.ChangeEmailListener, RegConfirmationFragment.CodeConfirmation {

    ChangeProfilePresenter presenter;

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
        finish();
    }

    @Override
    public void changedEmailConfirmWrongOther() {
        Toast.makeText(this, R.string.emailCodeWrong, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void changeEmail(String email, String accessToken) {
        presenter.changeTeacherEmail(email, accessToken);
    }

    @Override
    public void codeConfirm(String code) {
        String accessToken = MyApplications.getUser().getAccessToken(this);
        presenter.changeTeacherEmailConfirmation(code, accessToken);
    }
}
