package com.app.rbc.admin.models.db.models.site_overview;

import com.orm.SugarRecord;

/**
 * Created by jeet on 19/9/17.
 */

public class Stock extends SugarRecord{


    private String site;
    private String wheresite;
    private String product;
    private String stocktype;
    private String quantity;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Stock() {

    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setWheresite(String wheresite) {
        this.wheresite = wheresite;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getStocktype() {
        return stocktype;
    }

    public void setStocktype(String stocktype) {
        this.stocktype = stocktype;
    }

    private int statestore;
    public int getStatestore() {
        return statestore;
    }

    public void setStatestore(int statestore) {
        this.statestore = statestore;
    }
}
