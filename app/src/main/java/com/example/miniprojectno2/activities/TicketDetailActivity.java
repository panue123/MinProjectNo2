package com.example.miniprojectno2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miniprojectno2.MainActivity;
import com.example.miniprojectno2.R;
import com.google.android.material.button.MaterialButton;

public class TicketDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

        TextView tvTicketDetail = findViewById(R.id.tvTicketDetail);
        MaterialButton btnBackHome = findViewById(R.id.btnBackHome);

        Intent intent = getIntent();
        String movieTitle = intent.getStringExtra("movieTitle");
        String theaterName = intent.getStringExtra("theaterName");
        String showtimeInfo = intent.getStringExtra("showtimeInfo");
        String seats = intent.getStringExtra("seats");
        double totalPrice = intent.getDoubleExtra("totalPrice", 0);

        String detail = "Phim: " + movieTitle +
                       "\nRạp: " + theaterName +
                       "\nThời gian: " + showtimeInfo +
                       "\nGhế: " + seats +
                       "\nTổng tiền: " + String.format("%,.0f", totalPrice) + " VND";
        
        tvTicketDetail.setText(detail);

        btnBackHome.setOnClickListener(v -> {
            Intent homeIntent = new Intent(this, MainActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
            finish();
        });
    }
}
