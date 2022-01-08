package com.example.d1vtask.model;

public class Task {
    String taskname, description,date;

    public Task(){

    }

    public Task(String taskname, String description, String date) {
        this.taskname = taskname;
        this.description = description;
        this.date = date;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
