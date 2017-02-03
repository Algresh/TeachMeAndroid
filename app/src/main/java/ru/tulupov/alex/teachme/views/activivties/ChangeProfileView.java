package ru.tulupov.alex.teachme.views.activivties;


public interface ChangeProfileView {

    void changedPasswordSuccess();
    void changedPasswordWrongOld();
    void changedPasswordWrongOther();

    void changedEmailSuccess();
    void changedEmailWrongOther();

    void changedEmailConfirmSuccess();
    void changedEmailConfirmWrongOther();
}
