package com.example.miniprojectno2.dal;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.miniprojectno2.entities.Movie;
import com.example.miniprojectno2.entities.Showtime;
import com.example.miniprojectno2.entities.Theater;
import com.example.miniprojectno2.entities.Ticket;
import com.example.miniprojectno2.entities.User;

import java.util.concurrent.Executors;

@Database(entities = {User.class, Movie.class, Theater.class, Showtime.class, Ticket.class}, version = 2)
public abstract class AppDB extends RoomDatabase {
    public abstract DAO dao();

    private static AppDB INSTANCE;

    public static synchronized AppDB getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDB.class, "MovieBookingDB")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadExecutor().execute(() -> {
                                fillInitialData(INSTANCE);
                            });
                        }
                    })
                    .build();
        }
        return INSTANCE;
    }

    private static void fillInitialData(AppDB db) {
        DAO dao = db.dao();
        
        // Add Movies
        Movie m1 = new Movie();
        m1.title = "Avengers: Endgame";
        m1.genre = "Action, Sci-Fi";
        m1.duration = "181 min";
        m1.description = "After the devastating events of Infinity War...";
        dao.insertMovie(m1);

        Movie m2 = new Movie();
        m2.title = "Inception";
        m2.genre = "Action, Sci-Fi, Thriller";
        m2.duration = "148 min";
        m2.description = "A thief who steals corporate secrets...";
        dao.insertMovie(m2);

        // Add Theaters
        Theater t1 = new Theater();
        t1.name = "CGV Vincom";
        t1.address = "191 Ba Trieu, Hanoi";
        dao.insertTheater(t1);

        Theater t2 = new Theater();
        t2.name = "Lotte Cinema";
        t2.address = "54 Lieu Giai, Hanoi";
        dao.insertTheater(t2);

        // Add Showtimes
        Showtime s1 = new Showtime();
        s1.movieId = 1;
        s1.theaterId = 1;
        s1.startTime = "10:00 AM";
        s1.date = "2023-12-15";
        s1.price = 100000;
        dao.insertShowtime(s1);

        Showtime s2 = new Showtime();
        s2.movieId = 1;
        s2.theaterId = 2;
        s2.startTime = "02:00 PM";
        s2.date = "2023-12-15";
        s2.price = 90000;
        dao.insertShowtime(s2);
    }
}
