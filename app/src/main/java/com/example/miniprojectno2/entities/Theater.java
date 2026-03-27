package com.example.miniprojectno2.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Theaters")
public class Theater implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String address;
}