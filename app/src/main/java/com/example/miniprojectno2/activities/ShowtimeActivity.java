package com.example.miniprojectno2.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniprojectno2.R;
import com.example.miniprojectno2.adapters.ShowtimeAdapter;
import com.example.miniprojectno2.dal.AppDB;
import com.example.miniprojectno2.entities.Movie;
import com.example.miniprojectno2.entities.Showtime;

import java.util.List;

public class ShowtimeActivity extends AppCompatActivity {

    RecyclerView rvShowtimes;
    TextView tvMovieDetail;
    AppDB db;
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showtime);

        db = AppDB.getInstance(this);
        rvShowtimes = findViewById(R.id.rvShowtimes);
        tvMovieDetail = findViewById(R.id.tvMovieDetail);

        movie = (Movie) getIntent().getSerializableExtra("movie");
        if (movie != null) {
            tvMovieDetail.setText("Lịch chiếu cho: " + movie.title);
            loadShowtimes();
        }
    }

    private void loadShowtimes() {
        List<Showtime> showtimes = db.dao().getShowtimesByMovie(movie.id);
        // Pass 'false' for showMovieTitle because the title is already at the top
        ShowtimeAdapter adapter = new ShowtimeAdapter(showtimes, showtime -> {
            checkLoginAndBook(showtime);
        }, db, false);
        rvShowtimes.setLayoutManager(new LinearLayoutManager(this));
        rvShowtimes.setAdapter(adapter);
    }

    private void checkLoginAndBook(Showtime showtime) {
        SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
        if (sp.getBoolean("isLogin", false)) {
            goToBooking(showtime);
        } else {
            Toast.makeText(this, "Vui lòng đăng nhập để đặt vé", Toast.LENGTH_SHORT).show();
            startActivityForResult(new Intent(this, LoginActivity.class), 200);
            pendingShowtime = showtime;
        }
    }

    private Showtime pendingShowtime;

    private void goToBooking(Showtime showtime) {
        Intent intent = new Intent(this, BookingActivity.class);
        intent.putExtra("showtime", showtime);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
            if (pendingShowtime != null) {
                goToBooking(pendingShowtime);
                pendingShowtime = null;
            }
        }
    }
}
