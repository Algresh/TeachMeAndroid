package ru.tulupov.alex.teachme.views.activivties;


public interface ShowTeacherView {

    void setFavorite();
    void deleteFavorite();
    void errorFavorite();

    void isFavoriteSuccess(boolean isFavorite);
    void isFavoriteError();
}
