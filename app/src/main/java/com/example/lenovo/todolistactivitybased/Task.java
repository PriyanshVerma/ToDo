package com.example.lenovo.todolistactivitybased;

public class Task {
    private String title;
    private String desciptn;
    private long id;



    public Task(String title, String desciptn) {
        this.title = title;
        this.desciptn = desciptn;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public void setDesciptn(String desciptn){
        this.desciptn = desciptn;
    }
    public String getTitle(){
        return this.title;
    }
    public String getDesciptn(){
        return this.desciptn;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
}
