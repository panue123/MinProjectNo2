package com.example.miniprojectno2.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniprojectno2.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {

    private List<SeatItem> seats;
    private OnSeatSelectedListener listener;

    public interface OnSeatSelectedListener {
        void onSeatSelectionChanged();
    }

    public static class SeatItem {
        public String seatName;
        public boolean isBooked;
        public boolean isSelected;

        public SeatItem(String seatName, boolean isBooked) {
            this.seatName = seatName;
            this.isBooked = isBooked;
            this.isSelected = false;
        }
    }

    public SeatAdapter(List<SeatItem> seats, OnSeatSelectedListener listener) {
        this.seats = seats;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seat, parent, false);
        return new SeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        SeatItem item = seats.get(position);
        holder.tvSeatName.setText(item.seatName);

        if (item.isBooked) {
            holder.cardSeat.setCardBackgroundColor(Color.LTGRAY);
            holder.cardSeat.setStrokeColor(Color.TRANSPARENT);
            holder.cardSeat.setEnabled(false);
            holder.tvSeatName.setTextColor(Color.WHITE);
        } else if (item.isSelected) {
            holder.cardSeat.setCardBackgroundColor(Color.parseColor("#1565C0")); // primaryColor
            holder.cardSeat.setStrokeColor(Color.TRANSPARENT);
            holder.tvSeatName.setTextColor(Color.WHITE);
        } else {
            holder.cardSeat.setCardBackgroundColor(Color.WHITE);
            holder.cardSeat.setStrokeColor(Color.parseColor("#BDBDBD"));
            holder.tvSeatName.setTextColor(Color.BLACK);
        }

        holder.itemView.setOnClickListener(v -> {
            if (!item.isBooked) {
                item.isSelected = !item.isSelected;
                notifyItemChanged(position);
                listener.onSeatSelectionChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return seats.size();
    }

    public List<String> getSelectedSeats() {
        List<String> selected = new ArrayList<>();
        for (SeatItem item : seats) {
            if (item.isSelected) selected.add(item.seatName);
        }
        return selected;
    }

    static class SeatViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardSeat;
        TextView tvSeatName;

        public SeatViewHolder(@NonNull View itemView) {
            super(itemView);
            cardSeat = itemView.findViewById(R.id.cardSeat);
            tvSeatName = itemView.findViewById(R.id.tvSeatName);
        }
    }
}
