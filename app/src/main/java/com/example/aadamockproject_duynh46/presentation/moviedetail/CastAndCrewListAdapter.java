package com.example.aadamockproject_duynh46.presentation.moviedetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aadamockproject_duynh46.R;
import com.example.aadamockproject_duynh46.databinding.ItemViewCastAndCrewBinding;
import com.example.aadamockproject_duynh46.domain.model.MovieModel;
import com.example.aadamockproject_duynh46.domain.model.PeopleModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CastAndCrewListAdapter extends PagingDataAdapter<PeopleModel, CastAndCrewListAdapter.ViewHolder> {
    public CastAndCrewListAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemViewCastAndCrewBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_view_cast_and_crew, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PeopleModel people = getItem(position);
        holder.binding.setPeople(people);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemViewCastAndCrewBinding binding;

        public ViewHolder(@NonNull ItemViewCastAndCrewBinding binding) {
            super(binding.getRoot());
            this.binding= binding;
        }
    }

    public static DiffUtil.ItemCallback<PeopleModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@android.support.annotation.NonNull PeopleModel oldItem, @android.support.annotation.NonNull PeopleModel newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@android.support.annotation.NonNull PeopleModel oldItem, @android.support.annotation.NonNull PeopleModel newItem) {
            return oldItem.equals(newItem);
        }
    };
}
