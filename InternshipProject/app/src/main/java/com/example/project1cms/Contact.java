package com.example.project1cms;

public class Contact {
    String name;
    String mobile;
    String email;

    public Contact() {
        // Empty constructor required for Firebase
    }

    public Contact(String name, String mobile, String email) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
    }

    // Getters
    public String getName() { return name; }
    public String getMobile() { return mobile; }
    public String getEmail() { return email; }
}