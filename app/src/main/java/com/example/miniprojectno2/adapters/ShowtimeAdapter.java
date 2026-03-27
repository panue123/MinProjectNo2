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
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class ShowtimeAdapter extends RecyclerView.Adapter<ShowtimeAdapter.ShowtimeViewHolder> {

    private List<Showtime> showtimes;
    private OnShowtimeClickListener listener;
    private AppDB db;
    private boolean showMovieTitle;

    public interface OnShowtimeClickListener {
        void onBookClick(Showtime showtime);
    }

    public ShowtimeAdapter(List<Showtime> showtimes, OnShowtimeClickListener listener, AppDB db, boolean showMovieTitle) {
        this.showtimes = showtimes;
        this.listener = listener;
        this.db = db;
        this.showMovieTitle = showMovieTitle;
    }

    @NonNull
    @Override
    public ShowtimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_showtime, parent, false);
        return new ShowtimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowtimeViewHolder holder, int position) {
        Showtime showtime = showtimes.get(position);
        Theater theater = db.dao().getTheaterById(showtime.theaterId);

        if (showMovieTitle) {
            Movie movie = db.dao().getMovieById(showtime.movieId);
            holder.tvMovieTitleShowtime.setVisibility(View.VISIBLE);
            holder.tvMovieTitleShowtime.setText(movie != null ? movie.title : "Unknown Movie");
        } else {
            holder.tvMovieTitleShowtime.setVisibility(View.GONE);
        }

        holder.tvTheaterInfo.setText(theater != null ? theater.name : "Unknown Theater");
        holder.tvShowtimeInfo.setText(showtime.date + " | " + showtime.startTime);
        holder.tvPrice.setText(String.format("Giá: %,.0f VND", showtime.price));

        holder.btnSelectShowtime.setOnClickListener(v -> listener.onBookClick(showtime));
    }

    @Override
    public int getItemCount() {
        return showtimes.size();
    }

    static class ShowtimeViewHolder extends RecyclerView.ViewHolder {
        TextView tvMovieTitleShowtime, tvTheaterInfo, tvShowtimeInfo, tvPrice;
        MaterialButton btnSelectShowtime;

        public ShowtimeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovieTitleShowtime = itemView.findViewById(R.id.tvMovieTitleShowtime);
            tvTheaterInfo = itemView.findViewById(R.id.tvTheaterInfo);
            tvShowtimeInfo = itemView.findViewById(R.id.tvShowtimeInfo);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnSelectShowtime = itemView.findViewById(R.id.btnSelectShowtime);
        }
    }
}
