package com.example.future_parking.classes;

import java.util.ArrayList;

public class Account {

    private String name;
    private ArrayList carNumber;
    private String email;
    private String password;
    private String type;

    public Account() {
    }

    public Account(String name, String email, String password, ArrayList carNumber, String type) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.carNumber = new ArrayList();
        this.type = type;
    }


    public String getName() {
        return name;
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList getCarNumber() {
        return carNumber;
    }

    public String getType() {return type;}

}
