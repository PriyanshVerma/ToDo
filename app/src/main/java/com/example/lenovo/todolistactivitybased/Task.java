package com.example.lenovo.todolistactivitybased;

public class Task {
    private String title;
    private String description;
    private String date;
    private String time;
    private long id;



    public Task(String title, String description, String date, String time) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }

    public void setDescription(String description){
        this.description = description;
    }
    public String getDescription(){
        return this.description;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
}
