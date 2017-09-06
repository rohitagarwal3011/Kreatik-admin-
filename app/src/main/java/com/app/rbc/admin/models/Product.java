
package com.app.rbc.admin.models;

import com.google.gson.annotations.SerializedName;


public class Product {

    @SerializedName("product")
    private String mProduct;
    @SerializedName("quantity")
    private Long mQuantity;
    @SerializedName("rem_quantity")
    private Long mRemQuantity;

    public String getProduct() {
        return mProduct;
    }

    public void setProduct(String product) {
        mProduct = product;
    }

    public Long getQuantity() {
        return mQuantity;
    }

    public void setQuantity(Long quantity) {
        mQuantity = quantity;
    }

    public Long getRemQuantity() {
        return mRemQuantity;
    }

    public void setRemQuantity(Long remQuantity) {
        mRemQuantity = remQuantity;
    }

}
