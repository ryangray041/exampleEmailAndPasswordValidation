package com.example.week1project;

public class User {

    public String email, password;

    public User(){}

    public User(String email){
        this.email = email;
    }

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

}
