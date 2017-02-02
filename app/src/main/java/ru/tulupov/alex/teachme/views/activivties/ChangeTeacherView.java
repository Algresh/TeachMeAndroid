package ru.tulupov.alex.teachme.views.activivties;


import java.util.List;
import java.util.Map;

import ru.tulupov.alex.teachme.models.PriceList;

public interface ChangeTeacherView {

    void emailAndLoginIsChecked(int err);
    void emailAndLoginIsCheckedError();

    void showDataFullName(Map map);
    void errorDataFullName();

    void showDataContacts(Map map);
    void errorDataContacts();

    void showDataDescription(String description);
    void errorDataDescription();

    void showDataSubjects(List<PriceList> list);
    void errorDataSubjects();

    void changeFullNameSuccess(Map map);
    void changeFullNameError();

    void changeContactsSuccess(Map map);
    void changeContactsError();

    void changeDescriptionSuccess();
    void changeDescriptionError();

    void changeSubjectsSuccess();
    void changeSubjectsError();
}
