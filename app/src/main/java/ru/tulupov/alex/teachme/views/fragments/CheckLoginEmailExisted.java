package ru.tulupov.alex.teachme.views.fragments;

public interface CheckLoginEmailExisted {
    String getEmail();

    String getLogin();

    void showLoginExisted();

    void showEmailExisted();

    void showLoginEmailNotExisted() ;
}
