package com.example.charity.Model;

public class PersonNeed {

    private String Username;
    private String Email;
    private String Contact;
    private String password;
    private String id;


    public PersonNeed() {

    }

    public PersonNeed(String username, String email, String contact, String password, String id) {
        Username = username;
        Email = email;
        Contact = contact;
        this.password = password;
        this.id = id;
    }

    public PersonNeed(String email, String password) {
        Email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "PersonNeed{" +
                "Username='" + Username + '\'' +
                ", Email='" + Email + '\'' +
                ", Contact='" + Contact + '\'' +
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

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
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
