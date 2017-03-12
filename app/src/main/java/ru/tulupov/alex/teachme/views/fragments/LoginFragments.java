package ru.tulupov.alex.teachme.views.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import ru.tulupov.alex.teachme.Constants;
import ru.tulupov.alex.teachme.R;


public class LoginFragments extends Fragment implements View.OnClickListener {

    private static final int LAYOUT = R.layout.fragment_login;
    private LoginFragmentCallback callbackClick;

    private TextView edtLogin;
    private TextView edtPassword;
    private TextView btnFeedback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT, container, false);
        view.findViewById(R.id.btn_enter).setOnClickListener(this);
        view.findViewById(R.id.btn_register).setOnClickListener(this);
        view.findViewById(R.id.btn_forgot_password).setOnClickListener(this);
        view.findViewById(R.id.btn_feedback).setOnClickListener(this);

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
            case R.id.btn_feedback:
                sendEmailToDevelopers();
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

    private void sendEmailToDevelopers() {
        Intent sendEmail = new Intent(Intent.ACTION_SEND);

        sendEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.EMAIL});
        sendEmail.setType("message/rfc822");
        sendEmail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(sendEmail);
        }catch (android.content.ActivityNotFoundException e) {
            String noApp =  getResources().getString(R.string.noEmailApps);
            Toast.makeText(getContext(), noApp, Toast.LENGTH_SHORT).show();
        }

    }



    public interface LoginFragmentCallback {
        void loginClick(String login, String password);
        void registerClick();
        void forgotPasswordClick();
    }
}
