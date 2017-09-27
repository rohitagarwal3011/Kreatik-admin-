package com.app.rbc.admin.models.db.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

/**
 * Created by jeet on 6/9/17.
 */

public class Categoryproduct extends SugarRecord{

    public Categoryproduct() {

    }

    @SerializedName("category")
    @Expose
    private String category;


    @SerializedName("product")
    @Expose
    private String product;

    @SerializedName("unit")
    @Expose
    private String unit;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
