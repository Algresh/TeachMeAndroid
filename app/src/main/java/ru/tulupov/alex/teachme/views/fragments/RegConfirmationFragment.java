package ru.tulupov.alex.teachme.views.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ru.tulupov.alex.teachme.R;


public class RegConfirmationFragment extends Fragment {

    EditText tvCode;
    Button btnCode;
    CodeConfirmation listener;

    public interface CodeConfirmation {
        void codeConfirm(String code);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fradment_code_confirmation, container, false);
        tvCode = (EditText) view.findViewById(R.id.edt_confirm_code);
        btnCode = (Button) view.findViewById(R.id.btn_confirm);
        btnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = tvCode.getText().toString();
                listener.codeConfirm(code);

            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        listener = (CodeConfirmation) activity;
        super.onAttach(activity);
    }
}
