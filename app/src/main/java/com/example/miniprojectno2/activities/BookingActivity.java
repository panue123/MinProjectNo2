package com.example.miniprojectno2.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniprojectno2.R;
import com.example.miniprojectno2.adapters.SeatAdapter;
import com.example.miniprojectno2.dal.AppDB;
import com.example.miniprojectno2.entities.Movie;
import com.example.miniprojectno2.entities.Showtime;
import com.example.miniprojectno2.entities.Theater;
import com.example.miniprojectno2.entities.Ticket;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookingActivity extends AppCompatActivity {

    TextView tvBookingInfo, tvTotalPrice;
    RecyclerView rvSeats;
    MaterialButton btnConfirmBooking;
    AppDB db;
    Showtime showtime;
    SeatAdapter seatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        db = AppDB.getInstance(this);
        tvBookingInfo = findViewById(R.id.tvBookingInfo);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        rvSeats = findViewById(R.id.rvSeats);
        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);

        showtime = (Showtime) getIntent().getSerializableExtra("showtime");

        if (showtime != null) {
            Movie movie = db.dao().getMovieById(showtime.movieId);
            Theater theater = db.dao().getTheaterById(showtime.theaterId);

            String info = "Phim: " + (movie != null ? movie.title : "") +
                    "\nRạp: " + (theater != null ? theater.name : "") +
                    "\nThời gian: " + showtime.date + " " + showtime.startTime +
                    "\nGiá vé: " + String.format("%,.0f", showtime.price) + " VND";
            tvBookingInfo.setText(info);

            setupSeats();
        }

        btnConfirmBooking.setOnClickListener(v -> confirmBooking());
    }

    private void setupSeats() {
        List<SeatAdapter.SeatItem> seatItems = new ArrayList<>();
        List<Ticket> bookedTickets = db.dao().getTicketsByShowtime(showtime.id);
        List<String> bookedSeatNames = new ArrayList<>();
        for (Ticket t : bookedTickets) {
            bookedSeatNames.add(t.seatNumber);
        }

        String[] rows = {"A", "B", "C", "D"};
        for (String row : rows) {
            for (int i = 1; i <= 8; i++) {
                String name = row + i;
                seatItems.add(new SeatAdapter.SeatItem(name, bookedSeatNames.contains(name)));
            }
        }

        seatAdapter = new SeatAdapter(seatItems, () -> {
            int count = seatAdapter.getSelectedSeats().size();
            double total = count * showtime.price;
            tvTotalPrice.setText(String.format("Tổng tiền: %,.0f VND", total));
        });

        rvSeats.setLayoutManager(new GridLayoutManager(this, 8));
        rvSeats.setAdapter(seatAdapter);
    }

    private void confirmBooking() {
        List<String> selectedSeats = seatAdapter.getSelectedSeats();
        if (selectedSeats.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ít nhất một ghế", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
        String username = sp.getString("username", "");
        String bookingTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        StringBuilder seatString = new StringBuilder();
        for (String seat : selectedSeats) {
            Ticket ticket = new Ticket();
            ticket.showtimeId = showtime.id;
            ticket.username = username;
            ticket.seatNumber = seat;
            ticket.totalPrice = showtime.price;
            ticket.bookingTime = bookingTime;
            db.dao().insertTicket(ticket);

            if (seatString.length() > 0) seatString.append(", ");
            seatString.append(seat);
        }

        Intent intent = new Intent(this, TicketDetailActivity.class);
        intent.putExtra("movieTitle", db.dao().getMovieById(showtime.movieId).title);
        intent.putExtra("theaterName", db.dao().getTheaterById(showtime.theaterId).name);
        intent.putExtra("showtimeInfo", showtime.date + " " + showtime.startTime);
        intent.putExtra("seats", seatString.toString());
        intent.putExtra("totalPrice", selectedSeats.size() * showtime.price);
        startActivity(intent);
        finish();
    }
}
