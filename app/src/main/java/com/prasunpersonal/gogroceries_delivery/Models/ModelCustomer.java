package com.prasunpersonal.gogroceries_delivery.Models;

import androidx.annotation.NonNull;

import org.parceler.Parcel;

import java.util.Objects;

@Parcel
public class ModelCustomer {

    private String uid, name, email, phone, dp;

    public ModelCustomer() {}

    public ModelCustomer(@NonNull String uid, @NonNull String name, @NonNull String email, @NonNull String phone) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModelCustomer)) return false;
        ModelCustomer modelUser = (ModelCustomer) o;
        return getUid().equals(modelUser.getUid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUid());
    }
}
