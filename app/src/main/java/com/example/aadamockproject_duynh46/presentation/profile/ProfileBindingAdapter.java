package com.example.aadamockproject_duynh46.presentation.profile;

import android.widget.ImageView;

import com.example.aadamockproject_duynh46.framework.utils.ImageUtils;

public class ProfileBindingAdapter {
    @androidx.databinding.BindingAdapter("android:imageBase64")
    public static void setImageBase64(ImageView view, String base64){
        view.setImageBitmap(ImageUtils.base64ToBitmap(base64));
    }
}
