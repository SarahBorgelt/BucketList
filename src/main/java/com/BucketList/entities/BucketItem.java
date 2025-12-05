package com.BucketList.entities;

import jakarta.persistence.*;

//Entity is a JPA notation that informs Spring that this class should be mapped to a database table
@Entity
public class BucketItem {

    //@Id marks the primary key of entity and uniquely identifies each row in the database
    @Id

    //GeneratedValue tells JPA how to generate the primary key automatically.
    //In this instance, it means that the database will auto-increment the ID for you.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private boolean completed;

    //Create getters and setters to allow access from other classes or frameworks while keeping them private
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
