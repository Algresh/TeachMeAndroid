package ru.tulupov.alex.teachme.presenters;

import java.util.List;

import ru.tulupov.alex.teachme.models.City;
import ru.tulupov.alex.teachme.models.ModelMainImpl;
import ru.tulupov.alex.teachme.models.Subject;
import ru.tulupov.alex.teachme.models.Subway;
import ru.tulupov.alex.teachme.views.fragments.ShowCity;
import ru.tulupov.alex.teachme.views.fragments.ShowSubject;
import ru.tulupov.alex.teachme.views.fragments.ShowSubway;

public class CitySubjectPresenter {

    private ModelMainImpl modelMain;

    private List<City> listCities;
    private List<Subject> listSubjects;
    private List<Subway> listSubways;
    private ShowCity cityView;
    private ShowSubject subjectView;
    private ShowSubway subwayView;

    private int cityId;

    public void onCreate(ShowCity cityView, ShowSubject subjectView, ShowSubway subwayView) {
        this.cityView = cityView;
        this.subjectView = subjectView;
        this.subwayView = subwayView;
        modelMain = new ModelMainImpl();
    }

    public List<Subject> getListSubjects () {
        return listSubjects;
    }

    public void preUploadSubjects() {
        if (listSubjects != null) {
            modelMain.getSubjects(new ModelMainImpl.ModelMainSubjectsCallBack() {
                @Override
                public void success(List<Subject> list) {
                    listSubjects = list;
                }

                @Override
                public void error() {

                }
            });
        }
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
        if (listSubjects != null) {
            subjectView.showSubjects(listSubjects, tag);
            return;
        }

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

    public void getListSubways(int city) {
        if (listSubways != null && city == cityId) {
            subwayView.showSubways(listSubways);
            return;
        }
        cityId = city;

        modelMain.getSubways(city, new ModelMainImpl.ModelMainSubwaysCallBack() {
            @Override
            public void success(List<Subway> list) {
                listSubways = list;
                subwayView.showSubways(list);
            }

            @Override
            public void error() {

            }
        });
    }
}
