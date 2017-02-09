package ru.tulupov.alex.teachme.views.activivties;

public interface LoginView {

    void logInSuccess();
    void logInSuccessNotConfirm(String typeUser);
    void logInFail();

    void registerTeacherSuccess();
    void registerTeacherError();

    void registerConfirmTeacherSuccess();
    void registerConfirmTeacherError();

    void emailAndLoginIsChecked(int err);
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
