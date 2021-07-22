package com.example.fitnessplanner.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WeightLog {

    Date date;
    double weight;
    String units;

    public WeightLog(){}

    public WeightLog(Date date, double weight, String units)
    {
        this.date = date;
        this.weight = weight;
        this.units = units;
    }

    public Date getDate()
    {
        return date;
    }

    public String getMonth() {
        return (new SimpleDateFormat("MMM").format(date));
    }

    public int getDay() {
        return date.getDate();
    }

    public int getYear() {
        return date.getYear();
    }

    public double getWeight() {
        return weight;
    }

    public String getUnits() {
        return units;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getLog()
    {
        return getMonth() + " " + getDay() + ", " + getYear() + ": " + getWeight() + " " + getUnits();
    }
}
