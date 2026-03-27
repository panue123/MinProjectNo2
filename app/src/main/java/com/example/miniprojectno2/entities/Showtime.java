package com.example.miniprojectno2.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Showtimes",
        foreignKeys = {
                @ForeignKey(entity = Movie.class,
                        parentColumns = "id",
                        childColumns = "movieId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Theater.class,
                        parentColumns = "id",
                        childColumns = "theaterId",
                        onDelete = ForeignKey.CASCADE)
        })
public class Showtime implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int movieId;
    public int theaterId;
    public String startTime; // e.g., "10:00 AM"
    public String date;      // e.g., "2023-12-01"
    public double price;
}
