package ru.tulupov.alex.teachme.models;

import android.graphics.Bitmap;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private String subways;
    private boolean leaveHome;
    private boolean onlyDistanceLearning;
    private boolean distanceLearning;
    private boolean showEmail;
    private boolean showPhone;
    private boolean showBirthDate;
    private int anketa;

    private int enable;
    private Bitmap photo;

    private String accessToken;

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

    public String getSubways() {
        return subways;
    }

    public void setSubways(String subways) {
        this.subways = subways;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isOnlyDistanceLearning() {
        return onlyDistanceLearning;
    }

    public void setOnlyDistanceLearning(boolean onlyDistanceLearning) {
        this.onlyDistanceLearning = onlyDistanceLearning;
    }

    public boolean isDistanceLearning() {
        return distanceLearning;
    }

    public void setDistanceLearning(boolean distanceLearning) {
        this.distanceLearning = distanceLearning;
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

    public Map<String, String> getMapData() {
        Map<String, String> map = new HashMap<>();

        map.put("firstName", firstName);
        map.put("lastName", lastName);
        map.put("fatherName", fatherName);
        if (city != null) {
            map.put("city", String.valueOf(city.getId()));
        } else {
            map.put("city", String.valueOf(-1));
        }
        map.put("okrug", okrug);
        map.put("district", district);
        map.put("birthDate", birthDate);
        map.put("description", description);
        map.put("leaveHouse", String.valueOf(leaveHome));
        map.put("showPhone", String.valueOf(showPhone? 1: 0));
        map.put("showEmail", String.valueOf(showEmail? 1: 0));
        map.put("distanceLearning", String.valueOf(distanceLearning));
        map.put("onlyDistanceLearning", String.valueOf(onlyDistanceLearning));
        map.put("subwayStation", subways);
        map.put("phoneNumber", phoneNumber);
        map.put("email", email);
        map.put("password", password);
        map.put("login", login);
        map.put("priceLIstNum", String.valueOf(getPriceLists().size()));
        map.put("showBirthDate", String.valueOf(showBirthDate? 1: 0));

        return  map;
    }

    @Override
    public String toString() {
        return "TeacherRegistration{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", city=" + city +
                ", okrug='" + okrug + '\'' +
                ", district='" + district + '\'' +
                ", priceLists=" + priceLists +
                ", description='" + description + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", subways='" + subways + '\'' +
                ", leaveHome=" + leaveHome +
                ", onlyDistanceLearning=" + onlyDistanceLearning +
                ", distanceLearning=" + distanceLearning +
                ", anketa=" + anketa +
                ", enable=" + enable +
                ", photo=" + photo +
                '}';
    }
}
