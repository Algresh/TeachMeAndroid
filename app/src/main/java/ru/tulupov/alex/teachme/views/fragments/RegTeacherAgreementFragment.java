package ru.tulupov.alex.teachme.views.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.TeacherRegistration;


public class RegTeacherAgreementFragment extends Fragment implements RegDataCorrect{

    SwitchCompat swAgreement;
    TeacherAgreement listener;

    public RegTeacherAgreementFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reg_teacher_agreement, container, false);
        swAgreement = (SwitchCompat) view.findViewById(R.id.reg_teacher_agreement);
        TextView tv = (TextView) view.findViewById(R.id.test_param);
        tv.setText(TeacherRegistration.getInstance().toString());

        swAgreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    listener.agree();
                }
                else {

                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        listener = (TeacherAgreement) activity;
        super.onAttach(activity);
    }

    @Override
    public boolean dataIsCorrect() {
        return false;
    }

    public interface TeacherAgreement {
        void agree();
    }

}
