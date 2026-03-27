package com.example.miniprojectno2.dal;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.miniprojectno2.entities.Movie;
import com.example.miniprojectno2.entities.Showtime;
import com.example.miniprojectno2.entities.Theater;
import com.example.miniprojectno2.entities.Ticket;
import com.example.miniprojectno2.entities.User;

import java.util.List;

@Dao
public interface DAO {
    // User
    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM Users WHERE username = :u AND password = :p")
    User login(String u, String p);

    @Query("SELECT * FROM Users WHERE username = :u")
    User getUserByUsername(String u);

    // Movie
    @Insert
    void insertMovie(Movie movie);

    @Query("SELECT * FROM Movies")
    List<Movie> getAllMovies();

    @Query("SELECT * FROM Movies WHERE id = :id")
    Movie getMovieById(int id);

    // Theater
    @Insert
    void insertTheater(Theater theater);

    @Query("SELECT * FROM Theaters")
    List<Theater> getAllTheaters();

    @Query("SELECT * FROM Theaters WHERE id = :id")
    Theater getTheaterById(int id);

    // Showtime
    @Insert
    void insertShowtime(Showtime showtime);

    @Query("SELECT * FROM Showtimes WHERE movieId = :mId")
    List<Showtime> getShowtimesByMovie(int mId);

    @Query("SELECT * FROM Showtimes WHERE id = :id")
    Showtime getShowtimeById(int id);

    @Query("SELECT * FROM Showtimes")
    List<Showtime> getAllShowtimes();

    // Ticket
    @Insert
    long insertTicket(Ticket ticket);

    @Query("SELECT * FROM Tickets WHERE username = :u")
    List<Ticket> getTicketsByUser(String u);

    @Query("SELECT * FROM Tickets WHERE showtimeId = :sId")
    List<Ticket> getTicketsByShowtime(int sId);
}
