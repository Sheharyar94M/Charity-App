package com.example.ultimatebook.model;

public class HelpingPerson {

    private String Username;
    private String Email;
    private String password;
    private String id;
    private String card_number;
    private String cvc;
    private String expiry;


    public HelpingPerson() {
    }

    public HelpingPerson(String username, String email, String password, String id) {
        Username = username;
        Email = email;
        this.password = password;
        this.id = id;
    }

    @Override
    public String toString() {
        return "HelpingPerson{" +
                "Username='" + Username + '\'' +
                ", Email='" + Email + '\'' +
                ", password='" + password + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
