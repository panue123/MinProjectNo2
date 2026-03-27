package com.example.miniprojectno2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniprojectno2.R;
import com.example.miniprojectno2.entities.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private OnMovieClickListener listener;

    public interface OnMovieClickListener {
        void onBookClick(Movie movie);
    }

    public MovieAdapter(List<Movie> movies, OnMovieClickListener listener) {
        this.movies = movies;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.tvMovieTitle.setText(movie.title);
        holder.tvMovieGenre.setText(movie.genre);
        holder.tvMovieDuration.setText(movie.duration);
        holder.btnBookNow.setOnClickListener(v -> listener.onBookClick(movie));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView tvMovieTitle, tvMovieGenre, tvMovieDuration;
        Button btnBookNow;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovieTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvMovieGenre = itemView.findViewById(R.id.tvMovieGenre);
            tvMovieDuration = itemView.findViewById(R.id.tvMovieDuration);
            btnBookNow = itemView.findViewById(R.id.btnBookNow);
        }
    }
}
