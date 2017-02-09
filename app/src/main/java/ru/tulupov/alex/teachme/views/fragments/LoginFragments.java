package ru.tulupov.alex.teachme.views.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.tulupov.alex.teachme.R;


public class LoginFragments extends Fragment implements View.OnClickListener {

    private static final int LAYOUT = R.layout.fragment_login;
    private LoginFragmentCallback callbackClick;

    private TextView edtLogin;
    private TextView edtPassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT, container, false);
        view.findViewById(R.id.btn_enter).setOnClickListener(this);
        view.findViewById(R.id.btn_register).setOnClickListener(this);
        view.findViewById(R.id.btn_forgot_password).setOnClickListener(this);

        edtLogin = (TextView) view.findViewById(R.id.et_login);
        edtPassword = (TextView) view.findViewById(R.id.et_password);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_enter:
                loginClick();
                break;
            case R.id.btn_register:
                callbackClick.registerClick();
                break;
            case R.id.btn_forgot_password:
                callbackClick.forgotPasswordClick();
                break;
        }
    }

    protected void loginClick() {
        String login = edtLogin.getText().toString();
        String password = edtPassword.getText().toString();
        if (login.length() >= 2 && password.length() >= 6) {
            callbackClick.loginClick(login, password);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbackClick = (LoginFragmentCallback) activity;

    }

    public interface LoginFragmentCallback {
        void loginClick(String login, String password);
        void registerClick();
        void forgotPasswordClick();
    }
}
