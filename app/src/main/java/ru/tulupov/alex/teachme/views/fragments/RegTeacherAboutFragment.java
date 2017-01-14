package ru.tulupov.alex.teachme.views.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ru.tulupov.alex.teachme.R;

public class RegTeacherAboutFragment extends Fragment {


    private EditText editTextAbout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reg_teacher_about, container, false);
        editTextAbout = (EditText) view.findViewById(R.id.et_reg_teacher_about);

        return view;
    }

}
