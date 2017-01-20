package ru.tulupov.alex.teachme.views.activivties;

public interface LoginView {

    void logInSuccess();
    void logInFail();

    void registerTeacherSuccess();
    void registerTeacherError();

    void registerConfirmTeacherSuccess();
    void registerConfirmTeacherError();

    void emailAndLoginIsChecked(String type);
    void emailAndLoginIsCheckedError();

    void registerPupilSuccess();
    void registerPupilError();

    void registerConfirmPupilSuccess();
    void registerConfirmPupilError();

    void forgotPassEmailSuccess(String typeUser);
    void forgotPassEmailError();
    void forgotPassConfirmSuccess();
    void forgotPassConfirmError();
}
