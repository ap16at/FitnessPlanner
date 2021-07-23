package com.example.fitnessplanner.models;


public class User {
    private String fullName;
    private String password;
    private String goal;

    public User(String fullName, String password, String goal)
    {
        this.fullName = fullName;
        this.password = password;
        this.goal = goal;
    }

    public String getfullName() {
        return fullName;
    }

    public void setFullname(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }
}
