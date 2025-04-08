package com.example.aadamockproject_duynh46.presentation.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.example.aadamockproject_duynh46.R;
import com.example.aadamockproject_duynh46.databinding.ItemViewReminderNavViewBinding;
import com.example.aadamockproject_duynh46.databinding.MovieItemViewGridBinding;
import com.example.aadamockproject_duynh46.domain.model.MovieModel;
import com.example.aadamockproject_duynh46.domain.model.ReminderModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class ReminderListInNavAdapter extends RecyclerView.Adapter<ReminderListInNavAdapter.ViewHolder>{

    private final AsyncListDiffer<ReminderModel> differ = new AsyncListDiffer<>(this, DIFF_CALLBACK);

    public ReminderListInNavAdapter(){
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemViewReminderNavViewBinding itemViewReminderNavViewBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_view_reminder_nav_view, parent, false);
        return new ViewHolder(itemViewReminderNavViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReminderModel ReminderModel = differ.getCurrentList().get(position);
        holder.itemViewReminderNavViewBinding.itemViewReminderNavViewMovieMetadata.setText(String.format("%s - %s - %s/10", ReminderModel.getMovie().getTitle(), ReminderModel.getMovie().getReleaseDate().split("-")[0], ReminderModel.getMovie().getVoteAverage()));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ReminderModel.getTimeInMillis());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        String formattedDate = sdf.format(calendar.getTime());

        holder.itemViewReminderNavViewBinding.itemViewReminderNavViewReminderDatetime.setText(formattedDate);

    }
    public void submitList(ArrayList<ReminderModel> newReminderModelList){
        differ.submitList(new ArrayList<>(newReminderModelList));
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemViewReminderNavViewBinding itemViewReminderNavViewBinding;

        public ViewHolder(@NonNull ItemViewReminderNavViewBinding itemViewReminderNavViewBinding) {
            super(itemViewReminderNavViewBinding.getRoot());
            this.itemViewReminderNavViewBinding = itemViewReminderNavViewBinding;
        }
    }
    public static DiffUtil.ItemCallback<ReminderModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@android.support.annotation.NonNull ReminderModel oldItem, @android.support.annotation.NonNull ReminderModel newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@android.support.annotation.NonNull ReminderModel oldItem, @android.support.annotation.NonNull ReminderModel newItem) {
            return oldItem.equals(newItem);
        }
    };
}

