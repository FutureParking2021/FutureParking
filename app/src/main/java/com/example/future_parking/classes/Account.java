package com.example.future_parking.classes;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Account {
    @SerializedName("email")
    private String email;

    @SerializedName("role")
    private String role;

    @SerializedName("username")
    private String username;

    @SerializedName("avatar")
    private String avatar;

    public Account() {
    }

    public Account(String email, String role, String username, String avatar) {
        this.email = email;
        this.role = role;
        this.username = username;
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public Account setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getRole() {
        return role;
    }

    public Account setRole(String role) {
        this.role = role;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Account setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public Account setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }


    @Override
    public String toString() {
        return "Account{" +
                "email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", username='" + username + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
