package com.example.miniprojectno2.dal;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.miniprojectno2.entities.User;

import java.util.List;

@Dao
public interface DAO {
    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM Users WHERE username = :u AND password = :p")
    User login(String u, String p);

    @Query("SELECT * FROM Users WHERE username = :u")
    User getUserByUsername(String u);
}

