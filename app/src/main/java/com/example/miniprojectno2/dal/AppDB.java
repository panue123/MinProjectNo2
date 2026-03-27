package com.example.miniprojectno2.dal;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.miniprojectno2.entities.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDB extends RoomDatabase {
    public abstract DAO dao();

    private static AppDB INSTANCE;

    public static AppDB getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDB.class, "ShoppingDB")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
