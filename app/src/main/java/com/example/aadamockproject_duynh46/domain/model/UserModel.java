package com.example.aadamockproject_duynh46.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class UserModel  {
    private String image;
    private String fullName;
    private String email;
    private String birthday;
    private String gender;

    public UserModel(){}

    public UserModel(String image, String fullName, String email, String birthday, String gender) {
        this.image = image;
        this.fullName = fullName;
        this.email = email;
        this.birthday = birthday;
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}

