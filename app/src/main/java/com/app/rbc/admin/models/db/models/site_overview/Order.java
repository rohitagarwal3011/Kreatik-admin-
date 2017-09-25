package com.app.rbc.admin.models.db.models.site_overview;

import com.orm.SugarRecord;

/**
 * Created by jeet on 9/22/17.
 */

public class Order extends SugarRecord{

    public Order() {

    }

    private String category;
    private String quantity;
    private String product;

    private long site;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public long getSite() {
        return site;
    }

    public void setSite(long site) {
        this.site = site;
    }
}
