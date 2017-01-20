package ru.tulupov.alex.teachme.presenters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import ru.tulupov.alex.teachme.Constants;
import ru.tulupov.alex.teachme.MyApplications;
import ru.tulupov.alex.teachme.models.ModelUserInfo;
import ru.tulupov.alex.teachme.models.PupilRegistration;
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
                String login = (String) fields.get("login");
                String cityTitle = (String) fields.get("cityTitle");
                String email = (String) fields.get("email");
                Double cityId = (Double) fields.get("cityId");

                User user;
                if (type_user.equals(User.TYPE_USER_TEACHER)) {
                    String firstName = (String) fields.get("firstName");
                    String fatherName = (String) fields.get("fatherName");
                    String lastName = (String) fields.get("lastName");

                    user = new TeacherUser(context, type_user, userId.intValue(), accessToken,
                            enable.intValue(), firstName, lastName, fatherName, login, email, cityTitle,
                            cityId.intValue()
                    );
                } else {

                    user = new PupilUser(context, type_user, userId.intValue(), accessToken,
                            enable.intValue(), email, cityTitle, cityId.intValue(), login);
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
               view.registerTeacherError();
           }
       });
    }

    public void registrationPupil() {
        model.registerPupil(new ModelUserInfo.RegPupilCallBack() {
            @Override
            public void success(Map fields) {
                String accessToken = (String) fields.get("access_token");
                Double userId = (Double) fields.get("id");

                PupilRegistration pupilRegistration = PupilRegistration.getInstance();
                pupilRegistration.setAccessToken(accessToken);

                view.registerPupilSuccess();
            }

            @Override
            public void error(int type) {
                view.registerPupilError();
            }
        });
    }

    public void registerConfirmationPupil(String code, final Context context) {
        final PupilRegistration pupilRegistration = PupilRegistration.getInstance();
        String accessToken = pupilRegistration.getAccessToken();

        model.registerConfirmationPupil(accessToken, code, new ModelUserInfo.RegPupilConfirmCallBack() {
            @Override
            public void success(Map fields) {
                String type_user = (String) fields.get("type_user");
                Double enable = (Double) fields.get("enable");
                String accessToken = (String) fields.get("accessToken");
                Double userId = (Double) fields.get("id");
                String login = (String) fields.get("login");
                String cityTitle = (String) fields.get("cityTitle");
                String email = (String) fields.get("email");
                Double cityId = (Double) fields.get("cityId");

                PupilUser user = new PupilUser(context, type_user, userId.intValue(), accessToken,
                        enable.intValue(), email, cityTitle, cityId.intValue(), login);

                MyApplications.setUser(user);

                view.registerConfirmPupilSuccess();
            }

            @Override
            public void error(int type) {
                view.registerConfirmPupilError();
            }
        });
    }

    public void registerConfirmationTeacher(String code, final Context context) {
        final TeacherRegistration teacherRegistration = TeacherRegistration.getInstance();
        String accessToken = teacherRegistration.getAccessToken();

        model.registerConfirmationTeacher(accessToken, code, new ModelUserInfo.RegTeacherConfirmCallBack() {
            @Override
            public void success(Map fields) {
                String type_user = (String) fields.get("type_user");
                Double enable = (Double) fields.get("enable");
                String accessToken = (String) fields.get("accessToken");
                Double userId = (Double) fields.get("id");
                String cityTitle = (String) fields.get("cityTitle");
                String email = (String) fields.get("email");
                Double cityId = (Double) fields.get("cityId");

                String firstName = (String) fields.get("firstName");
                String fatherName = (String) fields.get("fatherName");
                String lastName = (String) fields.get("lastName");
                String login = (String) fields.get("login");

                User user = new TeacherUser(context, type_user, userId.intValue(), accessToken,
                        enable.intValue(), firstName, lastName, fatherName, login, email, cityTitle,
                        cityId.intValue()
                );

                Bitmap photo = teacherRegistration.getPhoto();
                try {
                    FileOutputStream out = context.openFileOutput("avatar.png", Context.MODE_PRIVATE);
                    photo.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException ignored) {

                }

                MyApplications.setUser(user);

                view.registerConfirmTeacherSuccess();
            }

            @Override
            public void error(int type) {
                view.registerConfirmTeacherError();
            }
        });
    }

    public void checkEmailAndLogin(String login, String email) {
        model.checkEmailAndLogin(login, email, new ModelUserInfo.CheckLoginEmailCallBack() {
            @Override
            public void success(Map fields) {
                Double login = (Double) fields.get("login");
                Double email = (Double) fields.get("email");

                int err = 0;
                if (login == 1) err++;
                if (email == 1) err = err + 2;

                switch (err) {
                    case 0:
                        view.emailAndLoginIsChecked(ModelUserInfo.TYPE_CORRECT_BOTH);
                        break;
                    case 1:
                        view.emailAndLoginIsChecked(ModelUserInfo.TYPE_ERROR_LOGIN);
                        break;
                    case 2:
                        view.emailAndLoginIsChecked(ModelUserInfo.TYPE_ERROR_EMAIL);
                        break;
                    case 3:
                        view.emailAndLoginIsChecked(ModelUserInfo.TYPE_ERROR_BOTH);
                        break;

                }


            }

            @Override
            public void error() {
                view.emailAndLoginIsCheckedError();
            }
        });
    }

    public void forgotPassword(String email) {
        model.forgotPassword(email, new ModelUserInfo.ForgotPasswordCallBack() {
            @Override
            public void success(Map fields) {
                String typeUser = (String) fields.get("typeUser");

                view.forgotPassEmailSuccess(typeUser);
            }

            @Override
            public void error() {
                view.forgotPassEmailError();
            }
        });
    }

    public void forgotPasswordConfirm(String email, String typeUser, String newPassword, String code) {
        model.forgotPasswordConfirmation(email, typeUser, newPassword, code, new ModelUserInfo.ForgotPasswordConfirmCallBack() {
            @Override
            public void success() {
                view.forgotPassConfirmSuccess();
            }

            @Override
            public void error(int type) {
                view.forgotPassConfirmError();
            }
        });
    }
}
