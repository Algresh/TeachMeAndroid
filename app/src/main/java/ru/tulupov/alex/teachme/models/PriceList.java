package ru.tulupov.alex.teachme.models;

public class PriceList {

    protected int id;
    protected int price;
    protected String experience;
    protected Subject subject;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "PriceList{" +
                "id=" + id +
                ", price=" + price +
                ", experience='" + experience + '\'' +
                ", subject=" + subject +
                '}';
    }
}
