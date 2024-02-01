package com.example.tabslayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DateAdaptor extends RecyclerView.Adapter<DateAdaptor.DateViewHolder> {

    private List<DateModel> dateList;
    private OnItemClickListener mListener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public interface OnItemClickListener {
        void onItemClick(View view, String selectedDate);
    }

    public DateAdaptor(List<DateModel> dateList, OnItemClickListener listener) {
        this.dateList = dateList;
        this.mListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cal, parent, false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        DateModel currentItem = dateList.get(position);
        holder.tvDate.setText(currentItem.getDate());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {

                    mListener.onItemClick(view, currentItem.getDate());
                    setSelectedPosition(holder.getAdapterPosition());
                }
            }
        });


        holder.itemView.setSelected(selectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    static class DateViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }


    public void startListening() {

    }


    public void stopListening() {

    }
}
