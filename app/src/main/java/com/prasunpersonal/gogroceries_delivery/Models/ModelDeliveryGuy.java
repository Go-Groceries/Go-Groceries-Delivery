package com.prasunpersonal.gogroceries_delivery.Models;

import java.util.Objects;

public class ModelDeliveryGuy {
    public static final int AVAILABLE = 0;
    public static final int UNAVAILABLE = 1;

    private String uid, name, email, phone, dp;
    private boolean available;
    long lastOrderAssigned;

    public ModelDeliveryGuy() {}

    public ModelDeliveryGuy(String uid, String name, String email, String phone) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.dp = null;
        this.available = false;
        this.lastOrderAssigned = 0;
    }

    public long getLastOrderAssigned() {
        return lastOrderAssigned;
    }

    public void setLastOrderAssigned(long lastOrderAssigned) {
        this.lastOrderAssigned = lastOrderAssigned;
    }

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
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
        if (!(o instanceof ModelDeliveryGuy)) return false;
        ModelDeliveryGuy that = (ModelDeliveryGuy) o;
        return getUid().equals(that.getUid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUid());
    }
}
