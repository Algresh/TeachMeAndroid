package ru.tulupov.alex.teachme.views.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.tulupov.alex.teachme.R;


public class RegSelectTypeProfile extends Fragment {

    public interface SelectTypeProfile {
        void selectTeacher();
        void selectPupil();
    }

    SelectTypeProfile listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_select_type_profile, container, false);

        view.findViewById(R.id.btn_iam_teacher).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.selectTeacher();
            }
        });
        view.findViewById(R.id.btn_iam_not_teacher).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.selectPupil();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        listener = (SelectTypeProfile) activity;
        super.onAttach(activity);
    }
}
