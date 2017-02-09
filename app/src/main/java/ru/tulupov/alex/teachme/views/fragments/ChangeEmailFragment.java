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
import android.widget.Toast;

import ru.tulupov.alex.teachme.MyApplications;
import ru.tulupov.alex.teachme.R;

public class ChangeEmailFragment extends Fragment {

    protected EditText edtNewEmailConfirm;
    protected Button btnChange;
    protected ChangeEmailListener listener;

    public interface ChangeEmailListener {
        void changeEmail(String email, String accessToken);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_email, container, false);

        edtNewEmailConfirm = (EditText) view.findViewById(R.id.edt_change_email);
        btnChange = (Button) view.findViewById(R.id.btn_change_email_confirm);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickChangeEmail();
            }
        });

        return view;
    }

    protected void clickChangeEmail() {
        String newEmail = edtNewEmailConfirm.getText().toString();
        if (newEmail.length() >= 4 && newEmail.contains("@")) {
            String accessToken= MyApplications.getUser().getAccessToken(getContext());
            listener.changeEmail(newEmail, accessToken);
        } else {
            Toast.makeText(getContext(), R.string.reg_warning_message, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onAttach(Activity activity) {
        listener = (ChangeEmailListener) activity;
        super.onAttach(activity);
    }
}
