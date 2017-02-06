package ru.tulupov.alex.teachme.views.activivties;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.City;
import ru.tulupov.alex.teachme.models.Subject;
import ru.tulupov.alex.teachme.models.Subway;
import ru.tulupov.alex.teachme.presenters.CitySubjectPresenter;
import ru.tulupov.alex.teachme.presenters.QuickSearchPresenter;
import ru.tulupov.alex.teachme.views.fragments.ExperienceDialogFragment;
import ru.tulupov.alex.teachme.views.fragments.FragmentCityDialog;
import ru.tulupov.alex.teachme.views.fragments.FragmentSubjectDialog;
import ru.tulupov.alex.teachme.views.fragments.FragmentSubwayDialog;
import ru.tulupov.alex.teachme.views.fragments.ShowCity;
import ru.tulupov.alex.teachme.views.fragments.ShowSubject;
import ru.tulupov.alex.teachme.views.fragments.ShowSubway;

import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.SEARCH_FIELD_CITY;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.SEARCH_FIELD_EXPERIENCE;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.SEARCH_FIELD_LEAVE_HOUSE;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.SEARCH_FIELD_PHOTO;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.SEARCH_FIELD_PRICE;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.SEARCH_FIELD_SUBJECT;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.SEARCH_FIELD_SUBWAY;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.TYPE_SEARCH;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.TYPE_SEARCH_FULL;
import static ru.tulupov.alex.teachme.views.activivties.SelectSearchActivity.TYPE_SEARCH_QUICK;

public class MainSearchActivity  extends BaseActivity implements ShowCity, ShowSubject,
        FragmentCityDialog.SelectCity, FragmentSubjectDialog.SelectSubject, ShowSubway,
        ExperienceDialogFragment.SelectExperience, FragmentSubwayDialog.SelectSubway {

    CitySubjectPresenter presenterCitySubject;

    protected TextView tvCity;
    protected TextView tvSubject;
    protected TextView tvExp;
    protected TextView tvSubway;
    protected View vSubwayLine;
    protected EditText edtPrice;
    protected SwitchCompat swLeaveHouse;
    protected SwitchCompat swPhoto;
    protected Button btnSearch;

    protected List<City> listCities;
    protected List<Subject> listSubjects;
    protected List<Integer> listSelectedSubways;
    protected List<Subway> listSubways;
    protected int indexSelectedCity = -1;
    protected int indexSelectedSubject = -1;
    protected int indexSelectedExp = -1;
    protected City selectedCity;
    protected Subject selectedSubject;

    protected boolean leaveHouse = false;
    protected boolean photo = false;


    private boolean dialogIsDownloading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);
        initToolbar(R.string.btn_search, R.id.toolbarMainSearch);

        presenterCitySubject = new CitySubjectPresenter();
        presenterCitySubject.onCreate(this, this, this);

        initViews();
    }

    @Override
    public void showCities(List<City> list) {
        listCities = list;
        FragmentCityDialog dialog = new FragmentCityDialog();
        dialog.setListCities(list);
        dialog.setSelectedItem(indexSelectedCity);
        FragmentManager manager = getSupportFragmentManager();
        dialog.show(manager, "city");
        dialogIsDownloading = false;
    }


    @Override
    public void selectCity(int cityIndex) {
        if (cityIndex != indexSelectedCity) {
            listSelectedSubways = null;
            tvSubway.setText(getString(R.string.hint_subway));
            indexSelectedCity = cityIndex;
            selectedCity = listCities.get(cityIndex);
            tvCity.setText(selectedCity.getTitle());

            if (selectedCity.isHasSubway()) {
                tvSubway.setVisibility(View.VISIBLE);
                vSubwayLine.setVisibility(View.VISIBLE);
            } else {
                tvSubway.setVisibility(View.GONE);
                vSubwayLine.setVisibility(View.GONE);
            }
        }
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
    public void showSubways(List<Subway> list) {
        listSubways = list;
        FragmentSubwayDialog dialog = new FragmentSubwayDialog();
        dialog.setListSubways(list);
        dialog.setArrSelected(getBooleanArr(list));
        dialog.setListSelected(listSelectedSubways);
        FragmentManager manager = getSupportFragmentManager();
        dialog.show(manager, "subway");
        dialogIsDownloading = false;
    }

    protected boolean[] getBooleanArr(List<Subway> listSubways) {
        boolean[] arr = new boolean[listSubways.size()];

        for (int i = 0; i < listSubways.size(); i++) {
            if (listSelectedSubways != null && listSelectedSubways.contains(i)) {
                arr[i] = true;
            } else {
                arr[i] = false;
            }
        }

        return arr;
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

    protected void initViews () {
        edtPrice = (EditText) findViewById(R.id.et_main_search_price);
        tvCity = (TextView) findViewById(R.id.sp_main_search_city);
        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dialogIsDownloading) {
                    if (checkConnection()) {
                        dialogIsDownloading = true;
                        presenterCitySubject.getListCities();
                    } else {
                        Toast.makeText(MainSearchActivity.this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        tvSubject = (TextView) findViewById(R.id.sp_main_search_subject);
        tvSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dialogIsDownloading) {
                    if (checkConnection()) {
                        dialogIsDownloading = true;
                        presenterCitySubject.getListSubjects(0);
                    } else {
                        Toast.makeText(MainSearchActivity.this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        tvExp = (TextView) findViewById(R.id.et_main_search_experience);
        tvExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dialogIsDownloading) {
                    dialogIsDownloading = true;
                    showExperience();
                }
            }
        });
        tvSubway= (TextView) findViewById(R.id.et_main_search_subway);
        vSubwayLine = findViewById(R.id.et_main_search_subway_line);
        tvSubway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dialogIsDownloading && selectedCity != null && selectedCity.isHasSubway()) {
                    if (checkConnection()) {
                        dialogIsDownloading = true;
                        presenterCitySubject.getListSubways(selectedCity.getId());
                    } else {
                        Toast.makeText(MainSearchActivity.this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        swLeaveHouse = (SwitchCompat) findViewById(R.id.sc_main_search_leave);
        swLeaveHouse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                leaveHouse = isChecked;
            }
        });
        swPhoto = (SwitchCompat) findViewById(R.id.sc_main_search_photo);
        swPhoto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                photo = isChecked;
            }
        });

        btnSearch = (Button) findViewById(R.id.btn_main_search_exec);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchTeachers();
            }
        });
    }

    protected void showExperience() {
        ExperienceDialogFragment fragment = new ExperienceDialogFragment();
        fragment.setTagItem(0);
        fragment.setSelectedItem(indexSelectedExp);
        FragmentManager manager = getSupportFragmentManager();
        fragment.show(manager, "exp");
        dialogIsDownloading = false;
    }

    protected void searchTeachers() {
        if (checkConnection()) {
            if (checkFields()) {
                Intent intent = new Intent(this, ListTeachersActivity.class);
                intent.putExtra(TYPE_SEARCH, TYPE_SEARCH_FULL);
                intent.putExtra(SEARCH_FIELD_CITY, listCities.get(indexSelectedCity).getId());
                intent.putExtra(SEARCH_FIELD_SUBJECT, listSubjects.get(indexSelectedSubject).getId());
                intent.putExtra(SEARCH_FIELD_LEAVE_HOUSE, leaveHouse);

                intent.putExtra(SEARCH_FIELD_PHOTO, photo);
                intent.putExtra(SEARCH_FIELD_EXPERIENCE, indexSelectedExp);
                intent.putExtra(SEARCH_FIELD_PRICE, Integer.parseInt(edtPrice.getText().toString()));
                String strSubIds = "";
                for (Integer i : listSelectedSubways) {
                    strSubIds = strSubIds + listSubways.get(i).getId() + " ";
                }
                intent.putExtra(SEARCH_FIELD_SUBWAY, strSubIds.trim());
                startActivity(intent);
            }
        } else {
            Toast.makeText(MainSearchActivity.this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void selectExperience(int item, int tagItem) {
        indexSelectedExp = item;
        String str = getResources().getStringArray(R.array.typeExperience)[item];
        tvExp.setText(str);
    }

    @Override
    public void selectSubway(List<Integer> listSelected) {
        this.listSelectedSubways = listSelected;
        String strSubway = "";

        for (Integer index : listSelected) {
            strSubway =  strSubway + listSubways.get(index).getTitle() + " ";
        }

        tvSubway.setText(strSubway);
    }
}
