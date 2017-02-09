package ru.tulupov.alex.teachme.views.activivties;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import ru.tulupov.alex.teachme.MyApplications;
import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.user.User;
import ru.tulupov.alex.teachme.presenters.ChangeProfilePresenter;

public class ChangePasswordActivity extends BaseActivity implements ChangeProfileView {

    protected EditText edtOldPass;
    protected EditText edtNewPass;
    protected EditText edtNewPassConfirm;
    protected Button btnChange;

    protected ChangeProfilePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        presenter = new ChangeProfilePresenter();
        presenter.onCreate(this);

        edtNewPass = (EditText) findViewById(R.id.edt_change_password_new);
        edtNewPassConfirm = (EditText) findViewById(R.id.edt_change_password_new_confirm);
        edtOldPass = (EditText) findViewById(R.id.edt_change_password_old);
        btnChange = (Button) findViewById(R.id.btn_change_pas_confirm);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPass = edtNewPass.getText().toString();
                String newPassConfirm = edtNewPassConfirm.getText().toString();
                String oldPass = edtOldPass.getText().toString();

                if (newPass.equals(newPassConfirm)) {
                    if (newPass.length() < 6) {
                        Toast.makeText(ChangePasswordActivity.this, R.string.pass_too_short, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (checkConnection()) {
                        String accessToken = MyApplications.getUser().getAccessToken(ChangePasswordActivity.this);
                        String email = MyApplications.getUser().getEmail(ChangePasswordActivity.this);
                        String userType = MyApplications.getUser().getTypeUser(ChangePasswordActivity.this);
                        Map<String, String> map = new HashMap<>();
                        map.put("email", email);
                        map.put("typeUser", userType);
                        map.put("oldPassword", oldPass);
                        map.put("newPassword", newPass);

                        presenter.changePassword(map, accessToken);
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChangePasswordActivity.this, R.string.text_is_differ, Toast.LENGTH_SHORT).show();
                }
            }
        });

        initToolbar(R.string.changePasswordTitle, R.id.toolbarChangePassword);
    }

    @Override
    public void changedPasswordSuccess() {
        Toast.makeText(this, R.string.passwordModified, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void changedPasswordWrongOld() {
        Toast.makeText(this, R.string.wrongOldPassword, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void changedPasswordWrongOther() {
        Toast.makeText(this, R.string.wrongOtherPassword, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void changedEmailConfirmSuccess() {

    }

    @Override
    public void changedEmailConfirmWrongOther() {

    }

    @Override
    public void changedEmailSuccess() {

    }

    @Override
    public void changedEmailWrongOther() {

    }
}
