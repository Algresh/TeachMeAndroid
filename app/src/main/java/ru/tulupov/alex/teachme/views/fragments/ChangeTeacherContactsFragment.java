package ru.tulupov.alex.teachme.views.fragments;


import android.content.res.Resources;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.tulupov.alex.teachme.Constants;
import ru.tulupov.alex.teachme.MyApplications;
import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.City;
import ru.tulupov.alex.teachme.models.ContactsBlock;
import ru.tulupov.alex.teachme.models.Subway;
import ru.tulupov.alex.teachme.models.user.TeacherUser;
import ru.tulupov.alex.teachme.presenters.CitySubjectPresenter;

public class ChangeTeacherContactsFragment extends Fragment implements ShowSubway, RegDataCorrect,
        FragmentSubwayDialog.SelectSubway, CheckLoginEmailExisted {

    private TextView tvPhoneExisted;
    private TextView tvSubway;
    private SwitchCompat scLeaveHouse;
    private EditText edtPhone;
//    private EditText edtLogin;
    private SwitchCompat scShowEmail;
    private SwitchCompat scShowPhone;
    private SwitchCompat scShowBirthDate;
//    private TextView tvAnketa;
    private TextView tvLoginExisted;

    private CitySubjectPresenter presenter;

    private City selectedCity;

    private List<Integer> listSelected;
    private List<Integer> listIdSelected; // нужен если станции метро не были изменены
    private List<Subway> listSubways;
    private boolean subwayDialogIsDownloading;
//    private int selectedPromotion = 0;

    boolean leaveHouse = false;
    int onlyDistance = 0;

    private String oldLogin;
    private String oldPhone;

    private ContactsBlock block;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        TeacherUser teacherUser = (TeacherUser) MyApplications.getUser();

        onlyDistance = teacherUser.getOnlyDistance(getContext());
        boolean cityHasSub =false;
        if (onlyDistance == 0) {
            int cityId = teacherUser.getCityId(getContext());
            String cityTitle = teacherUser.getCityTitle(getContext());
            cityHasSub = teacherUser.getCityHasSub(getContext());
            selectedCity = new City(cityId, cityTitle, cityHasSub);
        }




        View view = inflater.inflate(R.layout.fragment_change_teacher_contacts, container, false);
//        Resources res = getResources();

        tvSubway = (TextView) view.findViewById(R.id.et_reg_teacher_subway);

        String subwayStation = block.getSubways();
        String phoneNumber = block.getPhoneNumber();
        oldPhone = phoneNumber;
        String login =  block.getLogin();
        oldLogin = login;
//        int typeAnketa = block.getTypeAnketa();
        leaveHouse = block.isLeaveHome();
        Log.d(Constants.MY_TAG, leaveHouse + "||");

        if (selectedCity != null && onlyDistance == 0 && selectedCity.isHasSubway()) {

            tvSubway.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!subwayDialogIsDownloading) {
                        subwayDialogIsDownloading = true;
                        presenter.getListSubways(selectedCity.getId());
                    }
                }
            });
            tvSubway.setText(subwayStation);
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
        scLeaveHouse.setChecked(leaveHouse);
        edtPhone = (EditText) view.findViewById(R.id.edt_reg_teacher_phone);
        edtPhone.setText(String.valueOf(phoneNumber));
//        edtLogin = (EditText) view.findViewById(R.id.edt_reg_teacher_login);
//        edtLogin.setText(login);
        tvPhoneExisted = (TextView) view.findViewById(R.id.tv_phone_existed);
        tvLoginExisted = (TextView) view.findViewById(R.id.tv_login_existed);
        scShowEmail = (SwitchCompat) view.findViewById(R.id.sc_reg_teacher_show_email);
        scShowEmail.setChecked(block.isShowEmail());
        scShowEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    scShowPhone.setChecked(true);
                }
            }
        });
        scShowPhone = (SwitchCompat) view.findViewById(R.id.sc_reg_teacher_show_phone);
        scShowPhone.setChecked(block.isShowPhone());
        scShowPhone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    scShowEmail.setChecked(true);
                }
            }
        });

        scShowBirthDate = (SwitchCompat) view.findViewById(R.id.sc_reg_teacher_show_birth_date);
        scShowBirthDate.setChecked(block.isShowBirthDate());
//        selectedPromotion = typeAnketa;
//        String strAnketa = getResources().getStringArray(R.array.promotion)[typeAnketa];
//        tvAnketa = (TextView) view.findViewById(R.id.tv_reg_teacher_anketa);
//        tvAnketa.setText(strAnketa);
//        if (selectedPromotion != 0) {
//            String[] arr = res.getStringArray(R.array.promotion);
//            tvAnketa.setText(arr[selectedPromotion]);
//        }
//        tvAnketa.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PromotionDialogFragment dialog = new PromotionDialogFragment();
//                dialog.setSelectedItem(selectedPromotion);
//                dialog.show(getChildFragmentManager(), "promotion");
//            }
//        });


        presenter = new CitySubjectPresenter();
        presenter.onCreate(null, null, this);
        if (cityHasSub) {
            fullListIdSelected(block.getIdSubways());
        }


        return view;
    }

    public void setContactsBlock(ContactsBlock block) {
        this.block = block;
    }

    private void fullListIdSelected (String strSubId) {
        listIdSelected = new ArrayList<>();
        if (!strSubId.trim().equals("")) {
            String[] arrStr = strSubId.split(" ");
            for (String s : arrStr) {
                listIdSelected.add(Integer.parseInt(s));
            }
        } else {
            tvSubway.setText(R.string.hint_subway);
        }
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

    public Map<String, String> getDataMap() {
        Map<String, String> map = new HashMap<>();

        String strSubwayIds = "";
        if (listSelected != null && selectedCity.isHasSubway()) {
            for (int i = 0; i < listSelected.size(); i ++) {
                strSubwayIds = strSubwayIds + listSubways.get( listSelected.get(i) ).getId() + " ";
            }
        } else if (listIdSelected != null){
            for (int i = 0; i < listIdSelected.size(); i ++) {
                strSubwayIds = strSubwayIds + listIdSelected.get(i) + " ";
            }
        }

        map.put("leaveHouse", String.valueOf(leaveHouse? 1: 0));
        map.put("subwayStation", strSubwayIds);
        map.put("phoneNumber", edtPhone.getText().toString());
//        map.put("login", edtLogin.getText().toString());
        map.put("showPhone", String.valueOf(scShowPhone.isChecked()? 1: 0));
        map.put("showEmail", String.valueOf(scShowEmail.isChecked()? 1: 0));
        map.put("showBirthDate", String.valueOf(scShowBirthDate.isChecked()? 1: 0));
//        map.put("typeAnketa", String.valueOf(selectedPromotion));

        return map;
    }

//    public boolean isChangedLogin() {
//        if (oldLogin.equals(edtLogin.getText().toString())) {
//            return false;
//        }
//
//        return true;
//    }

    public boolean isChangedPhone() {
        if (oldPhone.equals(edtPhone.getText().toString())) {
            return false;
        }

        return true;
    }

    @Override
    public boolean dataIsCorrect() {
        boolean isCorrect = true;

        String strSubway = tvSubway.getText().toString().trim();
        String textSubway = getResources().getString(R.string.hint_subway);
        if(selectedCity != null && onlyDistance == 1 && selectedCity.isHasSubway() && strSubway.equals(textSubway)) {
            warningColorTextView(tvSubway);
            isCorrect = false;
        } else {
            correctColorTextView(tvSubway);
        }

//        String strAnketa = tvAnketa.getText().toString().trim();
//        String textAnketa = getResources().getString(R.string.btn_select_promotion);
//        if(strAnketa.equals(textAnketa)) {
//            warningColorTextView(tvAnketa);
//            isCorrect = false;
//        } else {
//            correctColorTextView(tvAnketa);
//        }

        String strPhone = edtPhone.getText().toString().trim();
        if (strPhone.length() < 2 || !strPhone.matches(".*\\d.*") ) {
            warningColorEditText(edtPhone);
            isCorrect = false;
        } else {
            correctColorEditText(edtPhone);
        }

//
//        String strLogin = edtLogin.getText().toString().trim();
//        if (strLogin.length() < 3) {
//            warningColorEditText(edtLogin);
//            isCorrect = false;
//        } else {
//            correctColorEditText(edtLogin);
//        }


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

//    @Override
//    public void selectPromotion(int item) {
//        selectedPromotion = item;
//        String strPromotion = getResources().getStringArray(R.array.promotion)[item];
//        tvAnketa.setText(strPromotion);
//    }


    @Override
    public String getEmail() {
        return null;
    }


    public String getPhone() {
        return  edtPhone.getText().toString();
    }

    @Override
    public void showLoginExisted() {
        tvLoginExisted.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmailExisted() {

    }

    @Override
    public void showPhoneExisted() {
        tvPhoneExisted.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoginEmailNotExisted() {
        tvLoginExisted.setVisibility(View.GONE);
        tvPhoneExisted.setVisibility(View.GONE);
    }

}
