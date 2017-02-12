package ru.tulupov.alex.teachme.views.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import ru.tulupov.alex.teachme.R;


public class RegTeacherAgreementFragment extends Fragment implements RegDataCorrect{

    private SwitchCompat swAgreement;
    private TextView tvTextAgreement;

    private boolean isAgree = false;

    public RegTeacherAgreementFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reg_teacher_agreement, container, false);
        swAgreement = (SwitchCompat) view.findViewById(R.id.reg_teacher_agreement);
        tvTextAgreement = (TextView) view.findViewById(R.id.text_agreement);
        Bundle args = getArguments();

        if (args != null) {
            String type = args.getString("type");
            if (type != null && type.equals("pupil")) {
                tvTextAgreement.setText(R.string.textAgreementPupil);
            }

            if (type != null && type.equals("teacher")) {
                tvTextAgreement.setText(R.string.textAgreementTeacher);
            }
        }

        swAgreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAgree = isChecked;
            }
        });
        return view;
    }

    @Override
    public boolean dataIsCorrect() {
        if (!isAgree) {
            String str = getContext().getResources().getString(R.string.error_dont_agreement);
            Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
        }

        return isAgree;
    }

}
