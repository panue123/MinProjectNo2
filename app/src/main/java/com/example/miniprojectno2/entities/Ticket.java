package com.example.miniprojectno2.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Tickets",
        foreignKeys = {
                @ForeignKey(entity = Showtime.class,
                        parentColumns = "id",
                        childColumns = "showtimeId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = User.class,
                        parentColumns = "username",
                        childColumns = "username",
                        onDelete = ForeignKey.CASCADE)
        })
public class Ticket implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int showtimeId;
    public String username;
    public String seatNumber;
    public double totalPrice;
    public String bookingTime;
}
