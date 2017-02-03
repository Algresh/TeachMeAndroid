package ru.tulupov.alex.teachme.presenters;

import java.util.Map;

import ru.tulupov.alex.teachme.models.ModelMainImpl;
import ru.tulupov.alex.teachme.models.ModelUserInfo;
import ru.tulupov.alex.teachme.views.activivties.ChangeProfileView;

public class ChangeProfilePresenter {

    private ModelUserInfo model;
    private ChangeProfileView view;

    public void onCreate(ChangeProfileView view) {
        this.view = view;
        model = new ModelUserInfo();
    }

    public void changePassword(Map<String, String> map , String accessToken) {
        model.changePassword(map, accessToken, new ModelUserInfo.ChangePasswordCallBack() {
            @Override
            public void success() {
                if (view != null) {
                    view.changedPasswordSuccess();
                }
            }

            @Override
            public void errorOldPass() {
                if (view != null) {
                    view.changedPasswordWrongOld();
                }
            }

            @Override
            public void errorOther() {
                if (view != null) {
                    view.changedPasswordWrongOther();
                }
            }
        });
    }


}
