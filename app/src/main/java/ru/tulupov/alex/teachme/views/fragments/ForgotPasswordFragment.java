package ru.tulupov.alex.teachme.views.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ru.tulupov.alex.teachme.R;

public class ForgotPasswordFragment extends Fragment{

    private EditText edtEmail;
    private EditText edtPass;
    private EditText edtPassConfirm;
    private Button btn;

    private ForgotPass listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        edtEmail = (EditText) view.findViewById(R.id.edt_forgot_pass_email);
        edtPass = (EditText) view.findViewById(R.id.edt_forgot_pass_newPass);
        edtPassConfirm = (EditText) view.findViewById(R.id.edt_forgot_pass_newPass_confirm);
        btn = (Button) view.findViewById(R.id.btn_forgot_pas_confirm);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = edtPass.getText().toString();
                String passConfirm = edtPassConfirm.getText().toString();
                String email = edtEmail.getText().toString();

                if(pass.equals(passConfirm) && pass.length() > 6 &&
                        email.length() > 5  && email.contains("@") ) {
                    listener.checkEmail(email, pass);
                } else {
                    String msg = getResources().getString(R.string.reg_warning_message);
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT ).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        listener = (ForgotPass) activity;
        super.onAttach(activity);
    }

    public interface ForgotPass {
        void checkEmail(String email, String pass);
    }
}
