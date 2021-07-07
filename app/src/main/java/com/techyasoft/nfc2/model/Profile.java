package com.techyasoft.nfc2.model;

public class Profile {
    String id;
    String userName;
    String password;
    String tour_minutes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTour_minutes() {
        return tour_minutes;
    }

    public void setTour_minutes(String tour_minutes) {
        this.tour_minutes = tour_minutes;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", tour_minutes='" + tour_minutes + '\'' +
                '}';
    }
}
