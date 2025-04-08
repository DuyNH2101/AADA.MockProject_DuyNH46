package com.example.aadamockproject_duynh46.presentation.moviedetail;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.databinding.InverseBindingListener;

import com.example.aadamockproject_duynh46.R;
import com.squareup.picasso.Picasso;

public class MovieDetailBindingAdapter {
    @androidx.databinding.BindingAdapter("android:srcImage")
    public static void setImageUrl(ImageView view, String imageUrl) {
        Picasso.get().load("https://image.tmdb.org/t/p/original/" + imageUrl)
                .placeholder(R.drawable.ic_default_image)
                .error(R.drawable.ic_error_image)
                .into(view);
    }
    @androidx.databinding.BindingAdapter("android:isFav")
    public static void setFavStar(ImageView view, boolean isFav) {
        if(isFav){
            view.setImageResource(R.drawable.ic_fav_star);
        } else {
            view.setImageResource(R.drawable.ic_not_fav_star);
        }
    }
}
