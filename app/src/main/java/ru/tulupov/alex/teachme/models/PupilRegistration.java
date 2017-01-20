package ru.tulupov.alex.teachme.models;

import java.util.HashMap;
import java.util.Map;

public class PupilRegistration {

    private static PupilRegistration pupilRegistration;

    public static PupilRegistration getInstance() {
        if (pupilRegistration == null) {
            pupilRegistration = new PupilRegistration();
        }

        return pupilRegistration;
    }

    public static void clearInstance() {
        pupilRegistration = null;
    }


    private City city;
    private String login;
    private String password;
    private String email;
    private int enable;
    private String accessToken;

    private PupilRegistration() {

    }

    public Map<String, String> getMapData() {
        Map<String, String> map = new HashMap<>();

        map.put("city", String.valueOf(city.getId()));
        map.put("email", email);
        map.put("password", password);
        map.put("login", login);

        return  map;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
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

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "PupilRegistration{" +
                "city=" + city +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", enable=" + enable +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}
