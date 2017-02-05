package ru.tulupov.alex.teachme.presenters;

import ru.tulupov.alex.teachme.models.ModelMainImpl;
import ru.tulupov.alex.teachme.views.activivties.BaseView;

public class BasePresenter {

    private ModelMainImpl model;
    private BaseView view;

    public void onCreate(BaseView view) {
        this.view = view;
        model = new ModelMainImpl();
    }

    public void freezeTeacher(String accessToken) {

        model.freezeTeacher(accessToken, new ModelMainImpl.FreezeCallBack() {
            @Override
            public void freezeSuccess() {
                if (view != null) {
                    view.freezeTeacherSuccess();
                }
            }

            @Override
            public void unfreezeSuccess() {
                if (view != null) {
                    view.freezeTeacherSuccess();
                }
            }

            @Override
            public void error() {
                if (view != null) {
                    view.freezeTeacherError();
                }
            }
        });
    }
}
