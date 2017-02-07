package ru.tulupov.alex.teachme.views.activivties;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.City;
import ru.tulupov.alex.teachme.models.Subject;
import ru.tulupov.alex.teachme.presenters.CitySubjectPresenter;
import ru.tulupov.alex.teachme.presenters.QuickSearchPresenter;
import ru.tulupov.alex.teachme.views.fragments.FragmentCityDialog;
import ru.tulupov.alex.teachme.views.fragments.FragmentSubjectDialog;
import ru.tulupov.alex.teachme.views.fragments.ShowCity;
import ru.tulupov.alex.teachme.views.fragments.ShowSubject;

import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.SEARCH_FIELD_CITY;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.SEARCH_FIELD_SUBJECT;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.SEARCH_FIELD_LEAVE_HOUSE;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.TYPE_SEARCH;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.TYPE_SEARCH_QUICK;

public class QuickSearchActivity extends BaseActivity implements ShowCity, ShowSubject,
        QuickSearchView, FragmentCityDialog.SelectCity, FragmentSubjectDialog.SelectSubject {

    private CitySubjectPresenter presenterCitySubject;
    private QuickSearchPresenter presenterSearch;

    private TextView tvCity;
    private TextView tvSubject;
    private SwitchCompat swLeaveHouse;
    private Button btnSearch;

    private List<City> listCities;
    private List<Subject> listSubjects;
    private int indexSelectedCity = -1;
    private int indexSelectedSubject = -1;
    private City selectedCity;
    private Subject selectedSubject;

    private boolean leaveHouse = false;


    private boolean dialogIsDownloading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_search);

        initToolbar(R.string.btn_quick_search, R.id.toolbarQuickSearch);
        presenterCitySubject = new CitySubjectPresenter();
        presenterCitySubject.onCreate(this, this, null);
        presenterSearch = new QuickSearchPresenter();
        presenterSearch.onCreate(this);

        tvCity = (TextView) findViewById(R.id.sp_quick_search_city);
        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dialogIsDownloading) {
                    if (checkConnection()) {
                        dialogIsDownloading = true;
                        presenterCitySubject.getListCities();
                    } else {
                        Toast.makeText(QuickSearchActivity.this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        tvSubject = (TextView) findViewById(R.id.sp_quick_search_subject);
        tvSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dialogIsDownloading) {
                    if (checkConnection()) {
                        dialogIsDownloading = true;
                        presenterCitySubject.getListSubjects(0);
                    } else {
                        Toast.makeText(QuickSearchActivity.this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        swLeaveHouse = (SwitchCompat) findViewById(R.id.sc_quick_search_leave);
        swLeaveHouse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                leaveHouse = isChecked;
            }
        });
        btnSearch = (Button) findViewById(R.id.btn_quick_search_exec);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkFields()) {
                    if (checkConnection()) {
                        Intent intent = new Intent(QuickSearchActivity.this, ListTeachersActivity.class);
                        intent.putExtra(TYPE_SEARCH, TYPE_SEARCH_QUICK);
                        intent.putExtra(SEARCH_FIELD_CITY, listCities.get(indexSelectedCity).getId());
                        intent.putExtra(SEARCH_FIELD_SUBJECT, listSubjects.get(indexSelectedSubject).getId());
                        intent.putExtra(SEARCH_FIELD_LEAVE_HOUSE, leaveHouse);
                        startActivity(intent);
                    } else {
                        Toast.makeText(QuickSearchActivity.this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @Override
    public void showCities(List<City> list) {
        if (list != null && list.size() > 0) {
            listCities = list;
            FragmentCityDialog dialog = new FragmentCityDialog();
            dialog.setListCities(list);
            dialog.setSelectedItem(indexSelectedCity);
            FragmentManager manager = getSupportFragmentManager();
            dialog.show(manager, "city");
            dialogIsDownloading = false;
        }
    }


    @Override
    public void selectCity(int cityIndex) {
        indexSelectedCity = cityIndex;
        selectedCity = listCities.get(cityIndex);
        tvCity.setText(selectedCity.getTitle());
    }

    @Override
    public void showSubjects(List<Subject> list, int tag) {
        listSubjects = list;
        FragmentSubjectDialog dialog = new FragmentSubjectDialog();
        dialog.setListSubject(list);
        dialog.setSelectedItem(indexSelectedSubject);
        FragmentManager manager = getSupportFragmentManager();
        dialog.show(manager, "subjects");
        dialogIsDownloading = false;
    }

    @Override
    public void selectSubject(int subjectIndex, int tag) {
        indexSelectedSubject = subjectIndex;
        selectedSubject = listSubjects.get(subjectIndex);
        tvSubject.setText(selectedSubject.getTitle());
    }

    protected boolean checkFields() {

        if (selectedSubject == null || selectedCity == null) {
            return false;
        }

        return true;


    }
}
