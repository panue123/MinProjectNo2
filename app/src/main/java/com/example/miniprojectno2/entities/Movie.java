package com.example.miniprojectno2.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Movies")
public class Movie implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String genre;
    public String duration;
    public String description;
    public String imageRes; // For simplicity, we can use resource name or URL
}
