package ru.tulupov.alex.teachme.presenters;

import android.content.Context;
import android.util.Log;

import java.util.Map;

import ru.tulupov.alex.teachme.Constants;
import ru.tulupov.alex.teachme.MyApplications;
import ru.tulupov.alex.teachme.models.ModelUserInfo;
import ru.tulupov.alex.teachme.models.user.PupilUser;
import ru.tulupov.alex.teachme.models.user.TeacherUser;
import ru.tulupov.alex.teachme.models.user.User;
import ru.tulupov.alex.teachme.views.activivties.LoginView;

public class LoginPresenter {

    private LoginView view;
    private ModelUserInfo model;

    public void onCreate(LoginView view) {
        this.view = view;
        model = new ModelUserInfo();
    }

    public void logIn(String login, String password, final Context context) {
        model.login(login, password, new ModelUserInfo.ModelCallBack() {
            @Override
            public void success(Map fields) {
                String type_user = (String) fields.get("type_user");
                Double enable = (Double) fields.get("enable");
                String accessToken = (String) fields.get("access_token");
                Double userId = (Double) fields.get("id");

                User user;
                if (type_user.equals(User.TYPE_USER_TEACHER)) {
                    String firstName = (String) fields.get("firstName");
                    String fatherName = (String) fields.get("fatherName");
                    String lastName = (String) fields.get("lastName");
                    String login = (String) fields.get("login");

                    user = new TeacherUser(context, type_user, userId.intValue(), accessToken,  enable.intValue(),
                            firstName, lastName, fatherName, login);
                } else {

                    user = new PupilUser(context, type_user, userId.intValue(), accessToken, enable.intValue());
                }
                Log.d(Constants.MY_TAG, type_user + enable + accessToken + userId);
                MyApplications.setUser(user);

                view.logInSuccess();
            }

            @Override
            public void error(int type) {
                view.logInFail();
            }
        });
    }

}
