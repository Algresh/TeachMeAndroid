package ru.tulupov.alex.teachme.models;

import android.graphics.Bitmap;


import java.util.List;

public class TeacherRegistration {

    private static TeacherRegistration teacherRegistration;

    public static TeacherRegistration getInstance() {
        if (teacherRegistration == null) {
            teacherRegistration = new TeacherRegistration();
        }

        return teacherRegistration;
    }

    public static void clearInstance() {
        teacherRegistration = null;
    }

    private String firstName;
    private String lastName;
    private String fatherName;
    private String birthDate;
    private City city;
    private String okrug;
    private String district;
    private List<PriceList> priceLists;

    private String description;
    private String login;
    private String password;
    private String email;
    private String phoneNumber;
    private boolean leaveHome;
    private int anketa;

    private int enable;
    private Bitmap photo;

    private TeacherRegistration () {

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getOkrug() {
        return okrug;
    }

    public void setOkrug(String okrug) {
        this.okrug = okrug;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public List<PriceList> getPriceLists() {
        return priceLists;
    }

    public void setPriceLists(List<PriceList> priceLists) {
        this.priceLists = priceLists;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isLeaveHome() {
        return leaveHome;
    }

    public void setLeaveHome(boolean leaveHome) {
        this.leaveHome = leaveHome;
    }

    public int getAnketa() {
        return anketa;
    }

    public void setAnketa(int anketa) {
        this.anketa = anketa;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
}
