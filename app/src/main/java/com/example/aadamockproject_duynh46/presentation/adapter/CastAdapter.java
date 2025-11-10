package com.example.aadamockproject_duynh46.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aadamockproject_duynh46.R;
import com.example.aadamockproject_duynh46.presentation.Model.Cast;

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {

    private final Context context;
    private final List<Cast> castList;

    public CastAdapter(Context context, List<Cast> castList) {
        this.context = context;
        this.castList = castList;
    }

    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dat_item_cast, parent, false);
        return new CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastViewHolder holder, int position) {
        Cast castMember = castList.get(position);

        holder.castName.setText(castMember.name);
        holder.castCharacter.setText(castMember.character);

        Glide.with(context)
                .load(castMember.profilePath)
                .placeholder(R.drawable.person_icon)
                .into(holder.castProfileImage);
    }

    @Override
    public int getItemCount() {
        return castList.size();
    }

    public static class CastViewHolder extends RecyclerView.ViewHolder {
        ImageView castProfileImage;
        TextView castName;
        TextView castCharacter;

        public CastViewHolder(@NonNull View itemView) {
            super(itemView);
            castProfileImage = itemView.findViewById(R.id.cast_profile_image);
            castName = itemView.findViewById(R.id.cast_name);
            castCharacter = itemView.findViewById(R.id.cast_character);
        }
    }
}
