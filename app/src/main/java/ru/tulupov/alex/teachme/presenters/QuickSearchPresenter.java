package ru.tulupov.alex.teachme.presenters;


import ru.tulupov.alex.teachme.models.ModelMainImpl;
import ru.tulupov.alex.teachme.views.activivties.QuickSearchView;

public class QuickSearchPresenter {

    QuickSearchView view;
    ModelMainImpl model;


    public void onCreate(QuickSearchView view) {
        this.view = view;
        model = new ModelMainImpl();
    }
}
