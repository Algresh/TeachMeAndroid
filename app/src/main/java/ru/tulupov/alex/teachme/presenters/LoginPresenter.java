package ru.tulupov.alex.teachme.presenters;

import android.content.Context;
import android.util.Log;

import java.util.Map;

import ru.tulupov.alex.teachme.Constants;
import ru.tulupov.alex.teachme.MyApplications;
import ru.tulupov.alex.teachme.models.ModelUserInfo;
import ru.tulupov.alex.teachme.models.TeacherRegistration;
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

    public void registrationTeacher() {
       model.registerTeacher(new ModelUserInfo.RegTeacherCallBack() {
           @Override
           public void success(Map fields) {
               String accessToken = (String) fields.get("access_token");
               Double userId = (Double) fields.get("id");

               TeacherRegistration teacherRegistration = TeacherRegistration.getInstance();
               teacherRegistration.setAccessToken(accessToken);

               if (teacherRegistration.getPhoto() == null) {
                   view.registerTeacherSuccess();
                   return;
               }

               model.setPhoto(accessToken, String.valueOf(userId), new ModelUserInfo.RegTeacherPhotoCallBack() {
                   @Override
                   public void success() {
                        view.registerTeacherSuccess();
                   }

                   @Override
                   public void error(int type) {

                   }
               });
           }

           @Override
           public void error(int type) {

           }
       });
    }

    public void registerConfirmationTeacher(String code) {
        TeacherRegistration teacherRegistration = TeacherRegistration.getInstance();
        String accessToken = teacherRegistration.getAccessToken();


        model.registerConfirmationTeacher(accessToken, code, new ModelUserInfo.RegTeacherConfirmCallBack() {
            @Override
            public void success() {
                view.registerConfirmTeacherSuccess();
            }

            @Override
            public void error(int type) {
                view.registerConfirmTeacherError();
            }
        });
    }

}
