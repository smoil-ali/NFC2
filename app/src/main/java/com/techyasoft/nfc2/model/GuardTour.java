package com.techyasoft.nfc2.model;

public class GuardTour {
    String id;
    String guard_id;
    String tour_minutes;
    String tour_hour;
    String tour_seconds;
    String tour_container;

    public GuardTour() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGuard_id() {
        return guard_id;
    }

    public void setGuard_id(String guard_id) {
        this.guard_id = guard_id;
    }

    public String getTour_minutes() {
        return tour_minutes;
    }

    public void setTour_minutes(String tour_minutes) {
        this.tour_minutes = tour_minutes;
    }

    public String getTour_hour() {
        return tour_hour;
    }

    public void setTour_hour(String tour_hour) {
        this.tour_hour = tour_hour;
    }

    public String getTour_seconds() {
        return tour_seconds;
    }

    public void setTour_seconds(String tour_seconds) {
        this.tour_seconds = tour_seconds;
    }

    public String getTour_container() {
        return tour_container;
    }

    public void setTour_container(String tour_container) {
        this.tour_container = tour_container;
    }

    @Override
    public String toString() {
        return "GuardTour{" +
                "id='" + id + '\'' +
                ", guard_id='" + guard_id + '\'' +
                ", tour_minutes='" + tour_minutes + '\'' +
                ", tour_hour='" + tour_hour + '\'' +
                ", tour_seconds='" + tour_seconds + '\'' +
                ", tour_container='" + tour_container + '\'' +
                '}';
    }
}
