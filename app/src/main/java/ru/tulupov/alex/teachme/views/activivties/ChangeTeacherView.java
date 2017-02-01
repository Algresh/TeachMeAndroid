package ru.tulupov.alex.teachme.views.activivties;


import java.util.Map;

public interface ChangeTeacherView {

    void emailAndLoginIsChecked(int err);
    void emailAndLoginIsCheckedError();

    void showDataFullName(Map map);
    void errorDataFullName();
}
