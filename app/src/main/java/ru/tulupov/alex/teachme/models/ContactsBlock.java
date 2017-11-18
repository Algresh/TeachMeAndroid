package ru.tulupov.alex.teachme.models;


public class ContactsBlock {

    private boolean leaveHome;
    private boolean showEmail;
    private boolean showPhone;
    private boolean showBirthDate;
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

    public boolean isShowEmail() {
        return showEmail;
    }

    public void setShowEmail(boolean showEmail) {
        this.showEmail = showEmail;
    }

    public boolean isShowPhone() {
        return showPhone;
    }

    public void setShowPhone(boolean showPhone) {
        this.showPhone = showPhone;
    }

    public boolean isShowBirthDate() {
        return showBirthDate;
    }

    public void setShowBirthDate(boolean showBirthDate) {
        this.showBirthDate = showBirthDate;
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
