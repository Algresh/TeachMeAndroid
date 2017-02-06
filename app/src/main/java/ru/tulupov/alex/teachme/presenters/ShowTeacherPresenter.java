package ru.tulupov.alex.teachme.presenters;

import android.content.Context;

import ru.tulupov.alex.teachme.database.DataBaseHelper;
import ru.tulupov.alex.teachme.database.DataBaseManager;
import ru.tulupov.alex.teachme.models.ModelMainImpl;
import ru.tulupov.alex.teachme.models.Teacher;
import ru.tulupov.alex.teachme.views.activivties.ListTeachersView;
import ru.tulupov.alex.teachme.views.activivties.ShowTeacherView;


public class ShowTeacherPresenter {

    private ShowTeacherView view;
    private ModelMainImpl model;

    public void onCreate(ShowTeacherView view) {
        this.view = view;
        model = new ModelMainImpl();
    }

    public void setFavorite (String accessToken, int idTeacher) {
        model.setFavorite(accessToken, idTeacher, new ModelMainImpl.ModelFavoriteCallBack() {
            @Override
            public void success(int code) {
                if (view != null) {
                    if (code == 201) {
                        view.setFavorite();
                    } else if (code == 202) {
                        view.deleteFavorite();
                    }
                }
            }

            @Override
            public void error() {
                if (view != null) {
                    view.errorFavorite();
                }
            }
        });
    }

    public void isFavorite (String accessToken, int idTeacher) {
        model.isFavoriteTeacher(accessToken, idTeacher, new ModelMainImpl.ModelIsFavoriteCallBack() {
            @Override
            public void error() {
                if (view != null) {
                    view.isFavoriteError();
                }
            }

            @Override
            public void success(boolean isFavorite) {
                if (view != null) {
                    view.isFavoriteSuccess(isFavorite);
                }
            }

        });
    }

    public void saveFavorite(Context context, Teacher teacher) {
        DataBaseManager manager = new DataBaseManager(context);
        manager.saveFavoriteTeacherToDB(teacher);
        manager.closeDatabase();
    }

    public void deleteFavorite (Context context, int id) {
        DataBaseManager manager = new DataBaseManager(context);
        manager.deleteFromFavorite(id);
        manager.closeDatabase();
    }

    public boolean isTeacherFavorite (Context context, int id) {
        DataBaseManager manager = new DataBaseManager(context);
        boolean isFavorite = manager.teacherIsFavorite(id);
        manager.closeDatabase();

        return isFavorite;
    }

}
