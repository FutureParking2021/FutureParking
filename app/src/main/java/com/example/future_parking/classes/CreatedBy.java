package com.example.future_parking.classes;

public class CreatedBy {

    private UserId userId;

    public CreatedBy() {
    }
    public CreatedBy(UserId userId) {
        super();
        this.userId = userId;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }
}
