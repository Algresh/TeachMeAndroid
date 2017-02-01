package ru.tulupov.alex.teachme.presenters;

import java.util.Map;

import ru.tulupov.alex.teachme.models.ModelUserInfo;
import ru.tulupov.alex.teachme.views.activivties.ChangeTeacherView;
import ru.tulupov.alex.teachme.views.activivties.LoginView;


public class ChangeTeacherPresenter {

    ChangeTeacherView view;

    private ModelUserInfo model;

    public void onCreate(ChangeTeacherView view) {
        this.view = view;
        model = new ModelUserInfo();
    }


    public void checkEmailAndLogin(String login, String email, String phone) {
        model.checkEmailAndLogin(login, email, phone, new ModelUserInfo.CheckLoginEmailCallBack() {
            @Override
            public void success(Map fields) {
                Double login = (Double) fields.get("login");
                Double email = (Double) fields.get("email");
                Double phone = (Double) fields.get("phoneNumber");

                int err = 0;
                if (login == 1) err++;
                if (email == 1) err = err + 10;
                if (phone == 1) err = err + 100;

                if (view != null) {
                    // передается число в которое означает какие ошибки произошли
                    // Пример: 101 - ошибка в логине и в номере телефона
                    view.emailAndLoginIsChecked(err);
                }


            }

            @Override
            public void error() {
                view.emailAndLoginIsCheckedError();
            }
        });
    }


    public void showTeacherFullName(String accessToken) {
        model.getTeacherFullName(accessToken, new ModelUserInfo.ChangeTeacherCallBack() {
            @Override
            public void success(Map map) {
                if (view != null) {
                    view.showDataFullName(map);
                }

            }

            @Override
            public void error() {
                if (view != null) {
                    view.errorDataFullName();
                }
            }
        });
    }
}
