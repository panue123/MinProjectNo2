package com.example.miniprojectno2.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Users")
public class User {
    @PrimaryKey
    @NonNull
    public String username;
    public String fullName;
    public String password;
    public String role;
}

