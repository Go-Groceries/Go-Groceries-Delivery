package com.prasunpersonal.gogroceries_delivery.Models;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

@Parcel
public class ModelOrder {
    public static final int ORDER_PLACED = 0;
    public static final int ORDER_PACKED = 1;
    public static final int OUT_FOR_DELIVERY = 2;
    public static final int MONEY_RECEIVED = 3;
    public static final int DELIVERED = 4;

    private String orderId, customerId, shopId, deliveryGuyId, deliveryAddress, invoice, secretCode;
    private double latitude, longitude, deliveryCharge;
    private long orderTime;
    private int orderStatus;
    private ArrayList<ModelCartItem> cartItems;

    public ModelOrder() {}

    public ModelOrder(String orderId, String customerId, String shopId, double deliveryCharge, double latitude, double longitude, String deliveryAddress, ArrayList<ModelCartItem> cartItems) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.shopId = shopId;
        this.deliveryCharge = deliveryCharge;
        this.deliveryAddress = deliveryAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cartItems = cartItems;
        this.orderTime = System.currentTimeMillis();
        this.orderStatus = ORDER_PLACED;
        this.invoice = null;
        this.deliveryGuyId = null;
        this.secretCode = String.format(Locale.getDefault(), "%04d", new Random().nextInt(9999));
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public ArrayList<ModelCartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(ArrayList<ModelCartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
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

    public double getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(double deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(String secretCode) {
        this.secretCode = secretCode;
    }

    public String getDeliveryGuyId() {
        return deliveryGuyId;
    }

    public void setDeliveryGuyId(String deliveryGuyId) {
        this.deliveryGuyId = deliveryGuyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModelOrder)) return false;
        ModelOrder that = (ModelOrder) o;
        return getOrderId().equals(that.getOrderId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderId());
    }
}
