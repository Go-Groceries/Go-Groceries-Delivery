package com.prasunpersonal.gogroceries_delivery.Models;

//import com.google.firebase.firestore.Exclude;

import org.parceler.Parcel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Parcel
public class ModelProduct {
    public static final int SINGLE_QUANTITY_PRODUCT = 1;
    public static final int WEIGHTED_QUANTITY_PRODUCT = 2;
    public static final int LIQUID_QUANTITY_PRODUCT = 3;

    public static final Map<String, Double> SINGLE_UNITS = new HashMap<>();
    public static final Map<String, Double> WEIGHTED_UNITS = new HashMap<>();
    public static final Map<String, Double> LIQUID_UNITS = new HashMap<>();

    static {
        SINGLE_UNITS.put("Piece", 1.0);
        SINGLE_UNITS.put("Dozen", 12.0);

        WEIGHTED_UNITS.put("Kilogram", 1.0);
        WEIGHTED_UNITS.put("Gram", 0.001);

        LIQUID_UNITS.put("Liter", 1.0);
        LIQUID_UNITS.put("Milliliter", 0.001);
    }

    private String productId, productName, productImg,productDescription;
    private int productType;
    private double availableQuantity,originalPrice, discountPercentage;

    public ModelProduct(){}

    public ModelProduct(String productId, String productName, int productType, double originalPrice, double discountPercentage, double availableQuantity, String productDescription, String productImg) {
        this.productId = productId;
        this.productName = productName;
        this.productType = productType;
        this.originalPrice = originalPrice;
        this.discountPercentage = discountPercentage;
        this.availableQuantity = availableQuantity;
        this.productDescription = productDescription;
        this.productImg = productImg;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public double getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(double availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

//    @Exclude
    public Map<String, Double> getUnitMap() {
        return (this.getProductType() == SINGLE_QUANTITY_PRODUCT) ? SINGLE_UNITS : (this.getProductType() == WEIGHTED_QUANTITY_PRODUCT) ? WEIGHTED_UNITS : LIQUID_UNITS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModelProduct)) return false;
        ModelProduct product = (ModelProduct) o;
        return getProductId().equals(product.getProductId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProductId());
    }
}
