package com.prasunpersonal.gogroceries_delivery.Models;

import org.parceler.Parcel;

import java.util.Objects;

@Parcel
public class ModelShop {

    private String uid,shopName,ownerName, ownerPhone, ownerEmail, country,state,city,address, dp,shopOpen, pinCode, shopClose;
    private double latitude,longitude, deliveryCharge;

    public ModelShop(){}

    public ModelShop(String uid, String shopName, String ownerName, String ownerPhone, String ownerEmail, double deliveryCharge, String country, String state, String city, String address, String pinCode, double latitude, double longitude, String dp, String shopOpen, String shopClose) {
        this.uid = uid;
        this.shopName = shopName;
        this.ownerName = ownerName;
        this.ownerPhone = ownerPhone;
        this.ownerEmail = ownerEmail;
        this.deliveryCharge = deliveryCharge;
        this.country = country;
        this.state = state;
        this.city = city;
        this.address = address;
        this.pinCode = pinCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dp = dp;
        this.shopOpen = shopOpen;
        this.shopClose = shopClose;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getShopOpen() {
        return shopOpen;
    }

    public void setShopOpen(String shopOpen) {
        this.shopOpen = shopOpen;
    }

    public String getShopClose() {
        return shopClose;
    }

    public void setShopClose(String shopClose) {
        this.shopClose = shopClose;
    }

    public double getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(double deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModelShop)) return false;
        ModelShop modelShop = (ModelShop) o;
        return getUid().equals(modelShop.getUid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUid());
    }
}
