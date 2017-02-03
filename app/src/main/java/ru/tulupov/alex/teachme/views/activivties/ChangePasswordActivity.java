package ru.tulupov.alex.teachme.views.activivties;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

import ru.tulupov.alex.teachme.MyApplications;
import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.user.User;
import ru.tulupov.alex.teachme.presenters.ChangeProfilePresenter;

public class ChangePasswordActivity extends AppCompatActivity implements ChangeProfileView {

    protected EditText edtOldPass;
    protected EditText edtNewPass;
    protected EditText edtNewPassConfirm;
    protected Button btnChange;

    ChangeProfilePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

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
                    String accessToken = MyApplications.getUser().getAccessToken(ChangePasswordActivity.this);
                    String email = MyApplications.getUser().getEmail(ChangePasswordActivity.this);
                    String userType = MyApplications.getUser().getTypeUser(ChangePasswordActivity.this);
                    Map<String, String> map = new HashMap<>();
                    map.put("email", email);
                    map.put("typeUser", userType);
                    map.put("oldPassword", oldPass);
                    map.put("newPassword", newPass);

                    presenter.changePassword(map, accessToken);
                }
            }
        });
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

    }

    @Override
    public void changedEmaildWrongOther() {

    }
}
