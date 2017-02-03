package ru.tulupov.alex.teachme.views.activivties;


import java.util.List;
import java.util.Map;

import ru.tulupov.alex.teachme.models.ContactsBlock;
import ru.tulupov.alex.teachme.models.FullNameBlock;
import ru.tulupov.alex.teachme.models.PriceList;

public interface ChangeTeacherView {

    void emailAndLoginIsChecked(int err);
    void emailAndLoginIsCheckedError();

    void showDataFullName(FullNameBlock block);
    void errorDataFullName();

    void showDataContacts(ContactsBlock block);
    void errorDataContacts();

    void showDataDescription(String description);
    void errorDataDescription();

    void showDataSubjects(List<PriceList> list);
    void errorDataSubjects();

    void changeFullNameSuccess(FullNameBlock block);
    void changeFullNameError();

    void changeContactsSuccess(ContactsBlock block);
    void changeContactsError();

    void changeDescriptionSuccess();
    void changeDescriptionError();

    void changeSubjectsSuccess();
    void changeSubjectsError();
}
