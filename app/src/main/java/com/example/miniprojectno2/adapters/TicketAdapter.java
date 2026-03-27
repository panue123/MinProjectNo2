package com.example.miniprojectno2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniprojectno2.R;
import com.example.miniprojectno2.dal.AppDB;
import com.example.miniprojectno2.entities.Movie;
import com.example.miniprojectno2.entities.Showtime;
import com.example.miniprojectno2.entities.Theater;
import com.example.miniprojectno2.entities.Ticket;

import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private List<Ticket> tickets;
    private AppDB db;

    public TicketAdapter(List<Ticket> tickets, AppDB db) {
        this.tickets = tickets;
        this.db = db;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);
        Showtime showtime = db.dao().getShowtimeById(ticket.showtimeId);
        
        if (showtime != null) {
            Movie movie = db.dao().getMovieById(showtime.movieId);
            Theater theater = db.dao().getTheaterById(showtime.theaterId);
            
            holder.tvTicketMovie.setText(movie != null ? movie.title : "N/A");
            holder.tvTicketInfo.setText(String.format("%s | %s %s", 
                    theater != null ? theater.name : "N/A", 
                    showtime.date, 
                    showtime.startTime));
        }
        
        holder.tvTicketSeats.setText("Ghế: " + ticket.seatNumber + " | Tổng: " + String.format("%,.0f", ticket.totalPrice) + " VND");
        holder.tvTicketBookingTime.setText("Ngày đặt: " + ticket.bookingTime);
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView tvTicketMovie, tvTicketInfo, tvTicketSeats, tvTicketBookingTime;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTicketMovie = itemView.findViewById(R.id.tvTicketMovie);
            tvTicketInfo = itemView.findViewById(R.id.tvTicketInfo);
            tvTicketSeats = itemView.findViewById(R.id.tvTicketSeats);
            tvTicketBookingTime = itemView.findViewById(R.id.tvTicketBookingTime);
        }
    }
}
