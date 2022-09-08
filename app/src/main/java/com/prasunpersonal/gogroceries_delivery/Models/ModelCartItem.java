package com.prasunpersonal.gogroceries_delivery.Models;

import org.parceler.Parcel;

import java.util.Objects;

@Parcel
public class ModelCartItem {
    private ModelProduct product;
    private double quantity;
    private String unit;

    public ModelCartItem() {}

    public ModelCartItem(ModelProduct product) {
        this.product = product;
    }

    public ModelCartItem(ModelProduct product, double quantity, String unit) {
        this.product = product;
        this.quantity = quantity;
        this.unit = unit;
    }

    public ModelProduct getProduct() {
        return product;
    }

    public void setProduct(ModelProduct product) {
        this.product = product;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModelCartItem)) return false;
        ModelCartItem that = (ModelCartItem) o;
        return getProduct().equals(that.getProduct());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProduct());
    }
}
