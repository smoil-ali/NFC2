package com.techyasoft.nfc2.model;

public class History {
    String id;
    String guard_id;
    String card_number;
    String date;
    String comment;
    String tour_counter;

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

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTour_counter() {
        return tour_counter;
    }

    public void setTour_counter(String tour_counter) {
        this.tour_counter = tour_counter;
    }
}
