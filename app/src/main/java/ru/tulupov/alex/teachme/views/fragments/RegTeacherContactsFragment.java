package ru.tulupov.alex.teachme.views.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.City;
import ru.tulupov.alex.teachme.models.Subway;
import ru.tulupov.alex.teachme.models.TeacherRegistration;
import ru.tulupov.alex.teachme.presenters.CitySubjectPresenter;

public class RegTeacherContactsFragment extends Fragment implements ShowSubway, RegDataCorrect, FragmentSubwayDialog.SelectSubway {

    TextView tvSubway;
    SwitchCompat scLeaveHouse;
    EditText edtPhone;
    EditText edtEmail;
    EditText edtPassword;
    EditText edtPasswordConfirm;
    TextView tvAnketa;

    CitySubjectPresenter presenter;

    City selectedCity;

    private List<Integer> listSelected;
    private List<Subway> listSubways;
    private boolean subwayDialogIsDownloading;


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
        edtPhone = (EditText) view.findViewById(R.id.edt_reg_teacher_phone);
        edtEmail = (EditText) view.findViewById(R.id.edt_reg_teacher_email);
        edtPassword = (EditText) view.findViewById(R.id.edt_reg_teacher_password);
        edtPasswordConfirm = (EditText) view.findViewById(R.id.edt_reg_teacher_passwordConfirm);
        tvAnketa = (TextView) view.findViewById(R.id.tv_reg_teacher_anketa);

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
        FragmentManager manager = getChildFragmentManager();
        dialog.show(manager, "subway");
        subwayDialogIsDownloading = false;
    }

    protected boolean[] getBooleanArr(List<Subway> listSubways) {
        boolean[] arr = new boolean[listSubways.size()];

        for (int i = 0; i < listSubways.size(); i++) {
            arr[i] = false;
        }

        return arr;
    }

    @Override
    public boolean dataIsCorrect() {
        return false;
    }

    @Override
    public void selectSubway(List<Integer> listSelected) {
        this.listSelected = listSelected;
    }
}
