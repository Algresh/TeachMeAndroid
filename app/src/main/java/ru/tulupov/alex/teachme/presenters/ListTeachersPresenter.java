package ru.tulupov.alex.teachme.presenters;


import java.util.List;

import ru.tulupov.alex.teachme.models.ModelMainImpl;
import ru.tulupov.alex.teachme.models.Teacher;
import ru.tulupov.alex.teachme.views.activivties.ListTeachersView;

public class ListTeachersPresenter {

    ListTeachersView view;
    ModelMainImpl model;

    public void onCreate(ListTeachersView view) {
        this.view = view;
        model = new ModelMainImpl();
    }

    public void getTeachersByCity(int city) {
        model.getTeachersByCity(city, 0, new ModelMainImpl.ModelMainTeachersCallBack() {
            @Override
            public void success(List<Teacher> list) {
                if (view != null) {
                    view.showListTeachers(list);
                }
            }

            @Override
            public void error() {
                if (view != null) {
                    view.errorListTeachers();
                }
            }
        });
    }

    public void addTeachersByCity(int city, int page) {
        model.getTeachersByCity(city, page, new ModelMainImpl.ModelMainTeachersCallBack() {
            @Override
            public void success(List<Teacher> list) {
                if (view != null) {
                    view.addToListTeachers(list);
                }
            }

            @Override
            public void error() {
                if (view != null) {
                    view.errorAddListTeachers();
                }
            }
        });
    }
}
