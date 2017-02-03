package ru.tulupov.alex.teachme.models;


public class ContactsBlock {

    private boolean leaveHome;
    private String subways;
    private String phoneNumber;
    private String login;
    private int typeAnketa;
    private String idSubways;

    public boolean isLeaveHome() {
        return leaveHome;
    }

    public void setLeaveHome(boolean leaveHome) {
        this.leaveHome = leaveHome;
    }

    public String getSubways() {
        return subways;
    }

    public void setSubways(String subways) {
        this.subways = subways;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getTypeAnketa() {
        return typeAnketa;
    }

    public void setTypeAnketa(int typeAnketa) {
        this.typeAnketa = typeAnketa;
    }

    public String getIdSubways() {
        return idSubways;
    }

    public void setIdSubways(String idSubways) {
        this.idSubways = idSubways;
    }

    @Override
    public String toString() {
        return "ContactsBlock{" +
                "leaveHome=" + leaveHome +
                ", subways='" + subways + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", login='" + login + '\'' +
                ", typeAnketa=" + typeAnketa +
                ", idSubways='" + idSubways + '\'' +
                '}';
    }
}
