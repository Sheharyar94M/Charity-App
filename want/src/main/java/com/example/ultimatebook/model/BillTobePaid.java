package com.example.ultimatebook.model;

import java.io.Serializable;

public class BillTobePaid implements Serializable {
    private String reference_no;
    private String due_date;
    private String  total_amount;
    private String  staus;
    private String id;
    private String date_of_post;

    public BillTobePaid() {
    }

    public BillTobePaid(String reference_no, String due_date, String total_amount, String staus, String id) {
        this.reference_no = reference_no;
        this.due_date = due_date;
        this.total_amount = total_amount;
        this.staus = staus;
        this.id = id;
    }

    public BillTobePaid(String date_of_post) {
        this.date_of_post = date_of_post;
    }

    @Override
    public String toString() {
        return "BillTobePaid{" +
                "reference_no='" + reference_no + '\'' +
                ", due_date='" + due_date + '\'' +
                ", total_amount='" + total_amount + '\'' +
                ", staus='" + staus + '\'' +
                ", id='" + id + '\'' +
                ", date_of_post='" + date_of_post + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStaus() {
        return staus;
    }

    public void setStaus(String staus) {
        this.staus = staus;
    }

    public String getReference_no() {
        return reference_no;
    }

    public void setReference_no(String reference_no) {
        this.reference_no = reference_no;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getDate_of_post() {
        return date_of_post;
    }

    public void setDate_of_post(String date_of_post) {
        this.date_of_post = date_of_post;
    }
}

