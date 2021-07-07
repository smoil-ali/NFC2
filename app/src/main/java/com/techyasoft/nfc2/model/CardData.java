package com.techyasoft.nfc2.model;

public class CardData {
    String id;
    String box_name;
    String card_number;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBox_name() {
        return box_name;
    }

    public void setBox_name(String box_name) {
        this.box_name = box_name;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    @Override
    public String toString() {
        return "CardData{" +
                "id='" + id + '\'' +
                ", box_name='" + box_name + '\'' +
                ", card_number='" + card_number + '\'' +
                '}';
    }
}
