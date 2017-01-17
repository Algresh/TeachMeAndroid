package ru.tulupov.alex.teachme.views.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import ru.tulupov.alex.teachme.Constants;
import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.City;
import ru.tulupov.alex.teachme.models.Subway;
import ru.tulupov.alex.teachme.models.TeacherRegistration;
import ru.tulupov.alex.teachme.presenters.CitySubjectPresenter;

public class RegTeacherContactsFragment extends Fragment implements ShowSubway, RegDataCorrect,
        FragmentSubwayDialog.SelectSubway, PromotionDialogFragment.SelectPromotion {

    private TextView tvSubway;
    private SwitchCompat scLeaveHouse;
    private EditText edtPhone;
    private EditText edtEmail;
    private EditText edtLogin;
    private EditText edtPassword;
    private EditText edtPasswordConfirm;
    private TextView tvAnketa;
    private TextView tvPassDiffer;

    private CitySubjectPresenter presenter;

    City selectedCity;

    private List<Integer> listSelected;
    private List<Subway> listSubways;
    private boolean subwayDialogIsDownloading;
    private int selectedPromotion = 0;

    boolean leaveHouse = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        selectedCity = TeacherRegistration.getInstance().getCity();
        View view = inflater.inflate(R.layout.fragment_reg_teacher_contacts, container, false);

        tvSubway = (TextView) view.findViewById(R.id.et_reg_teacher_subway);
        if (selectedCity.isHasSubway()) {

            tvSubway.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!subwayDialogIsDownloading) {
                        subwayDialogIsDownloading = true;
                        presenter.getListSubways(selectedCity.getId());
                    }

                }
            });
        } else  {
            tvSubway.setVisibility(View.GONE);
        }

        scLeaveHouse = (SwitchCompat) view.findViewById(R.id.sc_reg_teacher_leave);
        scLeaveHouse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                leaveHouse = b;
            }
        });
        edtPhone = (EditText) view.findViewById(R.id.edt_reg_teacher_phone);
        edtEmail = (EditText) view.findViewById(R.id.edt_reg_teacher_email);
        edtLogin = (EditText) view.findViewById(R.id.edt_reg_teacher_login);
        edtPassword = (EditText) view.findViewById(R.id.edt_reg_teacher_password);
        edtPasswordConfirm = (EditText) view.findViewById(R.id.edt_reg_teacher_passwordConfirm);
        tvPassDiffer = (TextView) view.findViewById(R.id.tv_pass_is_differ);
        tvAnketa = (TextView) view.findViewById(R.id.tv_reg_teacher_anketa);
        tvAnketa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PromotionDialogFragment dialog = new PromotionDialogFragment();
                dialog.setSelectedItem(selectedPromotion);
                dialog.show(getChildFragmentManager(), "promotion");
            }
        });

        presenter = new CitySubjectPresenter();
        presenter.onCreate(null, null, this);

        return view;
    }

    @Override
    public void showSubways(List<Subway> list) {
        listSubways = list;
        FragmentSubwayDialog dialog = new FragmentSubwayDialog();
        dialog.setListSubways(list);
        dialog.setArrSelected(getBooleanArr(list));
        dialog.setListSelected(listSelected);
        FragmentManager manager = getChildFragmentManager();
        dialog.show(manager, "subway");
        subwayDialogIsDownloading = false;
    }

    protected boolean[] getBooleanArr(List<Subway> listSubways) {
        boolean[] arr = new boolean[listSubways.size()];

        for (int i = 0; i < listSubways.size(); i++) {
            if (listSelected != null && listSelected.contains(i)) {
                arr[i] = true;
            } else {
                arr[i] = false;
            }
        }

        return arr;
    }

    public void saveData() {
        TeacherRegistration teacherRegistration = TeacherRegistration.getInstance();

        teacherRegistration.setLeaveHome(leaveHouse);
        teacherRegistration.setSubways(tvSubway.getText().toString());
        teacherRegistration.setPhoneNumber(edtPhone.getText().toString());
        teacherRegistration.setEmail(edtEmail.getText().toString());
        teacherRegistration.setLogin(edtLogin.getText().toString());
        teacherRegistration.setPassword(edtPassword.getText().toString());
        teacherRegistration.setAnketa(selectedPromotion);
    }

    @Override
    public boolean dataIsCorrect() {
        boolean isCorrect = true;

        String strSubway = tvSubway.getText().toString().trim();
        String textSubway = getResources().getString(R.string.hint_subject);
        if(strSubway.equals(textSubway)) {
            warningColorTextView(tvSubway);
        } else {
            correctColorTextView(tvSubway);
        }

        String strAnketa = tvAnketa.getText().toString().trim();
        String textAnketa = getResources().getString(R.string.btn_select_promotion);
        if(strAnketa.equals(textAnketa)) {
            warningColorTextView(tvAnketa);
        } else {
            correctColorTextView(tvAnketa);
        }

        String strPhone = edtPhone.getText().toString().trim();
        if (strPhone.length() < 2 || !strPhone.matches(".*\\d.*") ) {
            warningColorEditText(edtPhone);
            isCorrect = false;
        } else {
            correctColorEditText(edtPhone);
        }

        String strEmail = edtEmail.getText().toString().trim();
        if (strEmail.length() < 4 || !strEmail.contains("@") ) {
            warningColorEditText(edtEmail);
            isCorrect = false;
        } else {
            correctColorEditText(edtEmail);
        }

        String strLogin = edtLogin.getText().toString().trim();
        if (strLogin.length() < 3) {
            warningColorEditText(edtLogin);
            isCorrect = false;
        } else {
            correctColorEditText(edtLogin);
        }

        String strPass = edtPassword.getText().toString().trim();
        if (strPass.length() < 6) {
            warningColorEditText(edtPassword);
            isCorrect = false;
        } else {
            correctColorEditText(edtPassword);
        }

        String strPassConfirm = edtPasswordConfirm.getText().toString().trim();
        if (!strPass.equals(strPassConfirm)) {
            tvPassDiffer.setVisibility(View.VISIBLE);
            isCorrect = false;
        } else {
            tvPassDiffer.setVisibility(View.GONE);
        }

        if (isCorrect) {
            saveData();
        }

        return isCorrect;
    }

    protected void warningColorTextView(TextView textView) {
        int colorWarning = ContextCompat.getColor(getContext(), R.color.colorWarning);
        textView.setTextColor(colorWarning);
    }

    protected void warningColorEditText(EditText editText) {
        int colorWarning = ContextCompat.getColor(getContext(), R.color.colorWarning);
        editText.setTextColor(colorWarning);
        editText.setHintTextColor(colorWarning);
    }
    protected void correctColorTextView(TextView textView) {
        int colorWarning = ContextCompat.getColor(getContext(), R.color.colorCorrect);
        textView.setTextColor(colorWarning);
    }

    protected void correctColorEditText(EditText editText) {
        int colorWarning = ContextCompat.getColor(getContext(), R.color.colorCorrect);
        editText.setTextColor(colorWarning);
        editText.setHintTextColor(colorWarning);
    }

    @Override
    public void selectSubway(List<Integer> listSelected) {
        this.listSelected = listSelected;
        String strSubway = "";

        for (Integer index : listSelected) {
            strSubway =  strSubway + listSubways.get(index).getTitle() + " ";
        }

        tvSubway.setText(strSubway);

    }

    @Override
    public void selectPromotion(int item) {
        selectedPromotion = item;
        String strPromotion = getResources().getStringArray(R.array.promotion)[item];
        tvAnketa.setText(strPromotion);

    }
}
