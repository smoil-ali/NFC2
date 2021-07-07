package com.techyasoft.nfc2.model;

public class Tour {
    String id;
    String guard_id;
    String tour_number;
    String total_swipes;
    String tour_counter;
    String date;


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

    public String getTour_number() {
        return tour_number;
    }

    public void setTour_number(String tour_number) {
        this.tour_number = tour_number;
    }

    public String getTotal_swipes() {
        return total_swipes;
    }

    public void setTotal_swipes(String total_swipes) {
        this.total_swipes = total_swipes;
    }

    public String getTour_counter() {
        return tour_counter;
    }

    public void setTour_counter(String tour_counter) {
        this.tour_counter = tour_counter;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
