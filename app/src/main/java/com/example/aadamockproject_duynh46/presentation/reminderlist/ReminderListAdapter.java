package com.example.aadamockproject_duynh46.presentation.reminderlist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aadamockproject_duynh46.R;
import com.example.aadamockproject_duynh46.databinding.ItemViewReminderListBinding;
import com.example.aadamockproject_duynh46.databinding.ItemViewReminderNavViewBinding;
import com.example.aadamockproject_duynh46.domain.model.ReminderModel;

import com.example.aadamockproject_duynh46.presentation.main.ReminderListInNavAdapter;
import com.example.aadamockproject_duynh46.presentation.moviedetail.MovieDetailViewModel;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class ReminderListAdapter extends RecyclerView.Adapter<ReminderListAdapter.ViewHolder>{
    private final AsyncListDiffer<ReminderModel> differ = new AsyncListDiffer<>(this, DIFF_CALLBACK);
    private Context context;
    private ReminderListViewModel reminderListViewModel;
    private MovieDetailViewModel movieDetailViewModel;

    public ReminderListAdapter(Context context,
                               ReminderListViewModel reminderListViewModel,
                               MovieDetailViewModel movieDetailViewModel){
        this.context = context;
        this.reminderListViewModel = reminderListViewModel;
        this.movieDetailViewModel = movieDetailViewModel;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemViewReminderListBinding itemViewReminderListBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_view_reminder_list, parent, false);
        return new ViewHolder(itemViewReminderListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReminderModel reminder = differ.getCurrentList().get(position);
        holder.itemViewReminderListBinding.itemViewReminderListMovieMetadata.setText(String.format("%s - %s - %s/10", reminder.getMovie().getTitle(), reminder.getMovie().getReleaseDate().split("-")[0], reminder.getMovie().getVoteAverage()));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(reminder.getTimeInMillis());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        String formattedDate = sdf.format(calendar.getTime());

        holder.itemViewReminderListBinding.itemViewReminderListReminderDatetime.setText(formattedDate);

        Picasso.get().load("https://image.tmdb.org/t/p/original/"+reminder.getMovie().getPosterPath())
                .placeholder(R.drawable.ic_default_image)
                .error(R.drawable.ic_error_image)
                .into(holder.itemViewReminderListBinding.itemViewReminderListMoviePoster);
        holder.itemViewReminderListBinding.itemViewReminderListDeleteReminderBtn.setOnClickListener(l->{
            new AlertDialog.Builder(context)
                    .setTitle("Delete Item")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            reminderListViewModel.removeReminder(reminder);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
        holder.itemView.setOnClickListener(l->{
            movieDetailViewModel.getMutableLiveDataMovieDetail().setValue(reminder.getMovie());
        });
    }

    public void submitList(ArrayList<ReminderModel> newReminderList){
        differ.submitList(new ArrayList<>(newReminderList));
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemViewReminderListBinding itemViewReminderListBinding;
        public ViewHolder(@NonNull ItemViewReminderListBinding itemViewReminderListBinding) {
            super(itemViewReminderListBinding.getRoot());
            this.itemViewReminderListBinding = itemViewReminderListBinding;
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
