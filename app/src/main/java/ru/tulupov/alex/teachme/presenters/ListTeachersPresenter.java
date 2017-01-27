package ru.tulupov.alex.teachme.presenters;


import android.content.Context;

import java.util.List;
import java.util.Map;

import ru.tulupov.alex.teachme.database.DataBaseManager;
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

    public void getTeachersQuickSearch(int city, boolean leaveHouse, int subject) {
        model.getTeachersSearchQuick(city, leaveHouse, subject , 0, new ModelMainImpl.ModelMainTeachersCallBack() {
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

    public void addTeachersQuickSearch(int city, boolean leaveHouse, int subject, int page) {
        model.getTeachersSearchQuick(city, leaveHouse, subject , page, new ModelMainImpl.ModelMainTeachersCallBack() {
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

    public void getTeachersFullSearch(Map<String, String> map) {
        model.getTeachersSearchFull(map, new ModelMainImpl.ModelMainTeachersCallBack() {
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

    public void addTeachersFullSearch(Map<String, String> map) {
        model.getTeachersSearchFull(map, new ModelMainImpl.ModelMainTeachersCallBack() {
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

    public void getListFavoriteTeachers(Context context) {
        DataBaseManager manager = new DataBaseManager(context);
        List<Teacher> teacherList =  manager.getFavoriteTeachers();
        if (view != null){
            view.showListTeachers(teacherList);
        }
    }
}
