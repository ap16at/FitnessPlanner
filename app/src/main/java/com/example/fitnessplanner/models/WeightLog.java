package com.example.fitnessplanner.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WeightLog {

    String date;
    double weight;
    String units;

    public WeightLog(){}

    public WeightLog(String date, double weight, String units)
    {
        this.date = date;
        this.weight = weight;
        this.units = units;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getLog(){
        return date + ": " + weight + " " + units;
    }

}
