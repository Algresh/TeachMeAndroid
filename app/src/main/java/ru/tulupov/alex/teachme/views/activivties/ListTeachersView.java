package ru.tulupov.alex.teachme.views.activivties;


import java.util.List;

import ru.tulupov.alex.teachme.models.Teacher;

public interface ListTeachersView {

    void showListTeachers(List<Teacher> teacherList);
    void errorListTeachers();

    void addToListTeachers(List<Teacher> teacherList);
    void errorAddListTeachers();
}
