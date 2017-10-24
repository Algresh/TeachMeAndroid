package ru.tulupov.alex.teachme.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.City;
import ru.tulupov.alex.teachme.models.PupilRegistration;
import ru.tulupov.alex.teachme.presenters.CitySubjectPresenter;


public class RegPupilContacts extends Fragment implements ShowCity,
        RegDataCorrect ,FragmentCityDialog.SelectCity, CheckLoginEmailExisted{

    private TextView tvPassDiffer;
    private TextView tvCity;
    private EditText edtEmail;
    private EditText edtLogin;
    private EditText edtPassword;
    private EditText edtPasswordConfirm;

    private City selectedCity;
    private int indexSelectedCity = -1;
    private List<City> listCities;

    private boolean cityDialogIsDownloading = false;
    private CitySubjectPresenter presenter;

    private TextView tvLoginExisted;
    private TextView tvEmailExisted;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        presenter = new CitySubjectPresenter();
        presenter.onCreate(this, null, null);

        View view = inflater.inflate(R.layout.fragment_reg_pupil_contacts, container, false);
        tvPassDiffer = (TextView) view.findViewById(R.id.tv_pass_is_differ);
        tvCity = (TextView) view.findViewById(R.id.register_pupil_city);
        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cityDialogIsDownloading) {
                    cityDialogIsDownloading = true;
                    presenter.getListCities();
                }
            }
        });
        edtEmail = (EditText) view.findViewById(R.id.et_register_pupil_email);
        edtLogin = (EditText) view.findViewById(R.id.et_register_pupil_login);
        edtPassword = (EditText) view.findViewById(R.id.et_register_pupil_password);
        edtPasswordConfirm = (EditText) view.findViewById(R.id.et_register_pupil_password_confirm);
        tvLoginExisted = (TextView) view.findViewById(R.id.tv_login_existed);
        tvEmailExisted = (TextView) view.findViewById(R.id.tv_email_existed);
        return view;
    }

    @Override
    public void showCities(List<City> list) {
        if (list != null && list.size() > 0) {
            listCities = list;
            FragmentCityDialog dialog = new FragmentCityDialog();
            dialog.setListCities(list);
            dialog.setSelectedItem(indexSelectedCity);
            FragmentManager manager = getChildFragmentManager();
            dialog.show(manager, "city");
            cityDialogIsDownloading = false;
        }
    }

    @Override
    public void selectCity(int cityIndex) {
        indexSelectedCity = cityIndex;
        selectedCity = listCities.get(cityIndex);
        tvCity.setText(selectedCity.getTitle());
    }

    @Override
    public boolean dataIsCorrect() {
        boolean isCorrect = true;


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

        if (selectedCity == null) {
            warningColorTextView(tvCity);
            isCorrect = false;
        } else {
            correctColorTextView(tvCity);
        }

        if (!isCorrect) {
            String msg = getResources().getString(R.string.reg_warning_message);
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT ).show();
        } else {
            saveData();
        }

        return isCorrect;

    }

    public void saveData() {
        PupilRegistration pupilRegistration = PupilRegistration.getInstance();

        pupilRegistration.setEmail(edtEmail.getText().toString());
        pupilRegistration.setLogin(edtLogin.getText().toString());
        pupilRegistration.setPassword(edtPassword.getText().toString());
        pupilRegistration.setCity(selectedCity);
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
    public String getEmail() {
        return  edtEmail.getText().toString();
    }

    @Override
    public void showLoginExisted() {
        tvLoginExisted.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmailExisted() {
        tvEmailExisted.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPhoneExisted() {

    }

    @Override
    public void showLoginEmailNotExisted() {
        tvLoginExisted.setVisibility(View.GONE);
        tvEmailExisted.setVisibility(View.GONE);
    }
}
