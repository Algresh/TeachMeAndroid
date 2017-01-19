package ru.tulupov.alex.teachme.views.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.TeacherRegistration;

public class RegTeacherAboutFragment extends Fragment implements  RegDataCorrect{


    private EditText editTextAbout;
    private String textAbout = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reg_teacher_about, container, false);
        editTextAbout = (EditText) view.findViewById(R.id.et_reg_teacher_about);
        editTextAbout.setText(textAbout);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        TeacherRegistration teacher = TeacherRegistration.getInstance();
        textAbout = editTextAbout.getText().toString();
        teacher.setDescription(textAbout);
    }

    @Override
    public boolean dataIsCorrect() {
        boolean isCorrect = true;

        String strAbout = editTextAbout.getText().toString().trim();
        if (strAbout.length() < 2) {
            warningColorEditText(editTextAbout);
            isCorrect = false;
        } else {
            correctColorEditText(editTextAbout);
        }

        if (!isCorrect) {
            String msg = getResources().getString(R.string.reg_about_warning_message_);
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT ).show();
        }

        return isCorrect;
    }

    protected void warningColorEditText(EditText editText) {
        int colorWarning = ContextCompat.getColor(getContext(), R.color.colorWarning);
        editText.setTextColor(colorWarning);
        editText.setHintTextColor(colorWarning);
    }

    protected void correctColorEditText(EditText editText) {
        int colorWarning = ContextCompat.getColor(getContext(), R.color.colorCorrect);
        editText.setTextColor(colorWarning);
        editText.setHintTextColor(colorWarning);
    }
}
