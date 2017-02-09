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
                Boolean cityHasSub = (Boolean) fields.get("cityHasSub");

                User user;
                if (type_user.equals(User.TYPE_USER_TEACHER)) {
                    String firstName = (String) fields.get("firstName");
                    String fatherName = (String) fields.get("fatherName");
                    String lastName = (String) fields.get("lastName");
                    String photoSrc = (String) fields.get("photoSrc");

                    user = new TeacherUser(context, type_user, userId.intValue(), accessToken,
                            enable.intValue(), firstName, lastName, fatherName, login, email, cityTitle,
                            cityId.intValue(), photoSrc
                    );
                } else {

                    user = new PupilUser(context, type_user, userId.intValue(), accessToken,
                            enable.intValue(), email, cityTitle, cityId.intValue(), login);
                }
                user.setCityHasSub(context, cityHasSub);
                Log.d(Constants.MY_TAG, type_user + enable + accessToken + userId);
                MyApplications.setUser(user);

                view.logInSuccess();
            }

            @Override
            public void successNotEnable(Map fields) {
                String type_user = (String) fields.get("type_user");
                String accessToken = (String) fields.get("access_token");
                if (type_user.equals(User.TYPE_USER_TEACHER)) {
                    TeacherRegistration.getInstance().setAccessToken(accessToken);
                } else if (type_user.equals(User.TYPE_USER_PUPIL)) {
                    PupilRegistration.getInstance().setAccessToken(accessToken);
                }

                if (view != null) {
                    view.logInSuccessNotConfirm(type_user);
                }
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
        PupilRegistration pupilRegistration = PupilRegistration.getInstance();
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
                String photoSrc = (String) fields.get("photoSrc");

                User user = new TeacherUser(context, type_user, userId.intValue(), accessToken,
                        enable.intValue(), firstName, lastName, fatherName, login, email, cityTitle,
                        cityId.intValue(), photoSrc
                );

//                Bitmap photo = teacherRegistration.getPhoto();
//                if (photo != null) {
//                    try {
//                        FileOutputStream out = context.openFileOutput("avatar.png", Context.MODE_PRIVATE);
//                        photo.compress(Bitmap.CompressFormat.PNG, 100, out);
//                        out.flush();
//                        out.close();
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException ignored) {
//
//                    }
//                }

                MyApplications.setUser(user);

                view.registerConfirmTeacherSuccess();
            }

            @Override
            public void error(int type) {
                view.registerConfirmTeacherError();
            }
        });
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
