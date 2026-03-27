package com.example.miniprojectno2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniprojectno2.activities.LoginActivity;
import com.example.miniprojectno2.activities.ShowtimeActivity;
import com.example.miniprojectno2.adapters.MovieAdapter;
import com.example.miniprojectno2.adapters.ShowtimeAdapter;
import com.example.miniprojectno2.adapters.TheaterAdapter;
import com.example.miniprojectno2.adapters.TicketAdapter;
import com.example.miniprojectno2.dal.AppDB;
import com.example.miniprojectno2.entities.Movie;
import com.example.miniprojectno2.entities.Ticket;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvContent;
    MaterialButton btnLoginMain;
    ChipGroup chipGroup;
    EditText edtSearch;
    TextInputLayout tilSearch;
    AppDB db;
    List<Movie> allMovies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDB.getInstance(this);
        rvContent = findViewById(R.id.rvContent);
        btnLoginMain = findViewById(R.id.btnLoginMain);
        chipGroup = findViewById(R.id.chipGroup);
        edtSearch = findViewById(R.id.edtSearch);
        tilSearch = findViewById(R.id.tilSearch);

        rvContent.setLayoutManager(new LinearLayoutManager(this));

        loadInitialData();
        showMovies();

        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (tilSearch != null) {
                tilSearch.setVisibility(checkedId == R.id.chipMovies ? View.VISIBLE : View.GONE);
            }
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

        if (edtSearch != null) {
            edtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filterMovies(s.toString());
                }
                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        btnLoginMain.setOnClickListener(v -> {
            SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
            if (sp.getBoolean("isLogin", false)) {
                sp.edit().clear().apply();
                updateLoginButton();
                if (chipGroup.getCheckedChipId() == R.id.chipMyTickets) {
                    rvContent.setAdapter(null);
                }
            } else {
                startActivityForResult(new Intent(this, LoginActivity.class), 100);
            }
        });

        updateLoginButton();
    }

    private void loadInitialData() {
        allMovies = db.dao().getAllMovies();
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
        allMovies = db.dao().getAllMovies();
        updateMovieAdapter(allMovies);
    }

    private void filterMovies(String query) {
        List<Movie> filtered = allMovies.stream()
                .filter(m -> m.title.toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        updateMovieAdapter(filtered);
    }

    private void updateMovieAdapter(List<Movie> list) {
        MovieAdapter adapter = new MovieAdapter(list, movie -> {
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
        SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
        if (!sp.getBoolean("isLogin", false)) {
            rvContent.setAdapter(null);
            startActivityForResult(new Intent(this, LoginActivity.class), 100);
            return;
        }
        String user = sp.getString("username", "");
        List<Ticket> tickets = db.dao().getTicketsByUser(user);
        TicketAdapter adapter = new TicketAdapter(tickets, db);
        rvContent.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            updateLoginButton();
            if (chipGroup.getCheckedChipId() == R.id.chipMyTickets) {
                showMyTickets();
            }
        }
    }
}
