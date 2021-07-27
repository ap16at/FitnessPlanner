package com.example.fitnessplanner.models;

public class WorkoutItem {

    public String workoutType;
    public String workoutInfo;
    public String videoID;

    public WorkoutItem() {
    }

    public WorkoutItem(String workoutType, String workoutInfo, String videoID) {
        this.workoutType = workoutType;
        this.workoutInfo = workoutInfo;
        this.videoID = videoID;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    public String getWorkoutInfo() {
        return workoutInfo;
    }

    public void setWorkoutInfo(String workoutInfo) {
        this.workoutInfo = workoutInfo;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

}
