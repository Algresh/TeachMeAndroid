package ru.tulupov.alex.teachme.presenters;

import java.util.List;

import ru.tulupov.alex.teachme.models.City;
import ru.tulupov.alex.teachme.models.ModelMainImpl;
import ru.tulupov.alex.teachme.models.Subject;
import ru.tulupov.alex.teachme.views.fragments.ShowCity;
import ru.tulupov.alex.teachme.views.fragments.ShowSubject;

public class CitySubjectPresenter {

    private ModelMainImpl modelMain;

    private List<City> listCities;
    private List<Subject> listSubjects;
    private ShowCity cityView;
    private ShowSubject subjectView;

    public void onCreate(ShowCity cityView, ShowSubject subjectView) {
        this.cityView = cityView;
        this.subjectView = subjectView;
        modelMain = new ModelMainImpl();
    }

    public void getListCities() {
        if (listCities != null) {
            cityView.showCities(listCities);
            return;
        }

        modelMain.getCities(new ModelMainImpl.ModelMainCitiesCallBack() {
            @Override
            public void success(List<City> list) {
                listCities = list;
                cityView.showCities(list);
            }

            @Override
            public void error() {

            }
        });
    }

    public void getListSubjects(final int tag) {
        modelMain.getSubjects(new ModelMainImpl.ModelMainSubjectsCallBack() {
            @Override
            public void success(List<Subject> list) {
                listSubjects = list;
                subjectView.showSubjects(list, tag);
            }

            @Override
            public void error() {

            }
        });
    }
}
