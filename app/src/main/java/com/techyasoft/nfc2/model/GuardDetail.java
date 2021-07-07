package com.techyasoft.nfc2.model;

public class GuardDetail {
    String guard_id;
    String details;
    String box;
    String date;
    String tour_counter;

    public String getGuard_id() {
        return guard_id;
    }

    public void setGuard_id(String guard_id) {
        this.guard_id = guard_id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getBox() {
        return box;
    }

    public void setBox(String box) {
        this.box = box;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTour_counter() {
        return tour_counter;
    }

    public void setTour_counter(String tour_counter) {
        this.tour_counter = tour_counter;
    }

    @Override
    public String toString() {
        return "GuardDetail{" +
                "guard_id='" + guard_id + '\'' +
                ", details='" + details + '\'' +
                ", box='" + box + '\'' +
                ", date='" + date + '\'' +
                ", tour_counter='" + tour_counter + '\'' +
                '}';
    }
}
