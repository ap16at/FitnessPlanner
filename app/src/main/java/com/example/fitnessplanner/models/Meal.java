package com.example.fitnessplanner.models;

import java.util.Date;

public class Meal {

    String Description;
    int servingSize;
    int totalCalories;
    int protein;
    int carbs;
    int fat;
    String date;

    public Meal() {}

//    public Meal(String description, int servingSize, int totalCalories, int protein, int carbs, int fat) {
//        Description = description;
//        this.servingSize = servingSize;
//        this.totalCalories = totalCalories;
//        this.protein = protein;
//        this.carbs = carbs;
//        this.fat = fat;
//    }

    public Meal(String description, int servingSize, int totalCalories, int protein, int carbs, int fat, String date) {
        Description = description;
        this.servingSize = servingSize;
        this.totalCalories = totalCalories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.date = date;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getServingSize() {
        return servingSize;
    }

    public void setServingSize(int servingSize) {
        this.servingSize = servingSize;
    }

    public int getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(int totalCalories) {
        this.totalCalories = totalCalories;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
