package com.example.miniprojectno2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniprojectno2.activities.LoginActivity;
import com.example.miniprojectno2.activities.ShowtimeActivity;
import com.example.miniprojectno2.adapters.MovieAdapter;
import com.example.miniprojectno2.adapters.ShowtimeAdapter;
import com.example.miniprojectno2.adapters.TheaterAdapter;
import com.example.miniprojectno2.dal.AppDB;
import com.example.miniprojectno2.entities.Movie;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvContent;
    MaterialButton btnLoginMain;
    ChipGroup chipGroup;
    AppDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDB.getInstance(this);
        rvContent = findViewById(R.id.rvContent);
        btnLoginMain = findViewById(R.id.btnLoginMain);
        chipGroup = findViewById(R.id.chipGroup);

        rvContent.setLayoutManager(new LinearLayoutManager(this));

        showMovies();

        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.chipMovies) {
                showMovies();
            } else if (checkedId == R.id.chipTheaters) {
                showTheaters();
            } else if (checkedId == R.id.chipShowtimes) {
                showAllShowtimes();
            } else if (checkedId == R.id.chipMyTickets) {
                showMyTickets();
            }
        });

        btnLoginMain.setOnClickListener(v -> {
            SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
            if (sp.getBoolean("isLogin", false)) {
                sp.edit().clear().apply();
                updateLoginButton();
            } else {
                startActivityForResult(new Intent(this, LoginActivity.class), 100);
            }
        });

        updateLoginButton();
    }

    private void updateLoginButton() {
        SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
        if (sp.getBoolean("isLogin", false)) {
            btnLoginMain.setText("Đăng xuất (" + sp.getString("username", "") + ")");
        } else {
            btnLoginMain.setText("Đăng nhập");
        }
    }

    private void showMovies() {
        List<Movie> movies = db.dao().getAllMovies();
        MovieAdapter adapter = new MovieAdapter(movies, movie -> {
            Intent intent = new Intent(this, ShowtimeActivity.class);
            intent.putExtra("movie", movie);
            startActivity(intent);
        });
        rvContent.setAdapter(adapter);
    }

    private void showTheaters() {
        TheaterAdapter adapter = new TheaterAdapter(db.dao().getAllTheaters());
        rvContent.setAdapter(adapter);
    }

    private void showAllShowtimes() {
        ShowtimeAdapter adapter = new ShowtimeAdapter(db.dao().getAllShowtimes(), showtime -> {
            Intent intent = new Intent(this, ShowtimeActivity.class);
            Movie movie = db.dao().getMovieById(showtime.movieId);
            intent.putExtra("movie", movie);
            startActivity(intent);
        }, db, true);
        rvContent.setAdapter(adapter);
    }

    private void showMyTickets() {
        // Simple implementation: just show ticket info in a list or toast
        // For now, let's just show a message if not logged in
        SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
        if (!sp.getBoolean("isLogin", false)) {
            btnLoginMain.performClick();
            return;
        }
        // Logic to show user's tickets can be added here
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            updateLoginButton();
        }
    }
}
