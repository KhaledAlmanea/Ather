package com.example.ather.model;

public class Users {
    private String name, password, email, uId;


    public Users() {
    }

    public Users(String name, String password, String email, String uId) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.uId = uId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
}
