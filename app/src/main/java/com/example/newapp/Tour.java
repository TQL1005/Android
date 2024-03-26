package com.example.newapp;

public class Tour {
    public String getTour_name() {
        return tour_name;
    }

    public void setTour_name(String tour_name) {
        this.tour_name = tour_name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    String tour_name;
String start_date;
String end_date;
Integer amount;

    public Tour(String tour_name, String start_date, String end_date, Integer amount) {
        this.tour_name = tour_name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.amount = amount;
    }
}
