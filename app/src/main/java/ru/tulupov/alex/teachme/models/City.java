package ru.tulupov.alex.teachme.models;

public class City {

    private int id;
    private String title;
    private boolean hasSubway;

    public City(int id, String title, boolean hasSubway) {
        this.id = id;
        this.title = title;
        this.hasSubway = hasSubway;
    }

    public City() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isHasSubway() {
        return hasSubway;
    }

    public void setHasSubway(boolean hasSubway) {
        this.hasSubway = hasSubway;
    }

    @Override
    public String toString() {
        return "City{" +
                "title='" + title + '\'' +
                ", hasSubway=" + hasSubway +
                '}';
    }
}
