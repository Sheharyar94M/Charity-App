package com.example.charity.Model;

import java.io.Serializable;

public class BillPayment implements Serializable {
    private String reference_no;
    private String due_date;
    private String total_amount;
    private String staus;
    private String id;
    private String date_of_post;
    private String description;
    private String provider_name;

    public BillPayment() {
    }

    public BillPayment(String reference_no, String due_date, String total_amount, String staus, String id, String date_of_post, String description,String provider_name) {
        this.reference_no = reference_no;
        this.due_date = due_date;
        this.total_amount = total_amount;
        this.staus = staus;
        this.id = id;
        this.date_of_post = date_of_post;
        this.description = description;
        this.provider_name = provider_name;
    }

    @Override
    public String toString() {
        return "BillPayment{" +
                "reference_no='" + reference_no + '\'' +
                ", due_date='" + due_date + '\'' +
                ", total_amount='" + total_amount + '\'' +
                ", staus='" + staus + '\'' +
                ", id='" + id + '\'' +
                ", date_of_post='" + date_of_post + '\'' +
                ", description='" + description + '\'' +
                ", provider_name='" + provider_name + '\'' +
                '}';
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }
}

