package com.app.rbc.admin.models.db.models;

import com.orm.SugarRecord;

/**
 * Created by jeet on 16/9/17.
 */

public class Vehicle extends SugarRecord{

    private String transid;
    private String status;
    private String driver;
    private String vehiclenumber;
    private String dispatchdt;
    private String source;
    private String sourcetype;
    private String destination;
    private String desttype;
    private String challannum;
    private String challanimg;

    private int statestore;

    private String products;
    private String quantities;

    public String getTransid() {
        return transid;
    }

    public void setTransid(String transid) {
        this.transid = transid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getVehiclenumber() {
        return vehiclenumber;
    }

    public void setVehiclenumber(String vehiclenumber) {
        this.vehiclenumber = vehiclenumber;
    }

    public String getDispatchdt() {
        return dispatchdt;
    }

    public void setDispatchdt(String dispatchdt) {
        this.dispatchdt = dispatchdt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourcetype() {
        return sourcetype;
    }

    public void setSourcetype(String sourcetype) {
        this.sourcetype = sourcetype;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDesttype() {
        return desttype;
    }

    public void setDesttype(String desttype) {
        this.desttype = desttype;
    }

    public String getChallannum() {
        return challannum;
    }

    public void setChallannum(String challannum) {
        this.challannum = challannum;
    }

    public String getChallanimg() {
        return challanimg;
    }

    public void setChallanimg(String challanimg) {
        this.challanimg = challanimg;
    }

    public int getStatestore() {
        return statestore;
    }

    public void setStatestore(int statestore) {
        this.statestore = statestore;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getQuantities() {
        return quantities;
    }

    public void setQuantities(String quantities) {
        this.quantities = quantities;
    }
}
