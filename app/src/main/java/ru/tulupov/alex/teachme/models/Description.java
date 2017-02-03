package ru.tulupov.alex.teachme.models;


public class Description {

    private String description;

    public Description(String text) {
        this.description = text;
    }

    public String getText() {
        return description;
    }

    public void setText(String text) {
        this.description = text;
    }

    @Override
    public String toString() {
        return description;
    }
}
