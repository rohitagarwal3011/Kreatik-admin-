package com.app.rbc.admin.models.db.models.site_overview;

import com.orm.SugarRecord;

/**
 * Created by jeet on 19/9/17.
 */

public class Trans extends SugarRecord{


    private String fromto;
    private String transid;
    private String status,vehiclenumber,driver,dispatchdt,source,sourcetype,
    destination,desttype,challannum,challanimg;
    private String products;
    private String quantites;

    public Trans() {

    }


    public String getFromto() {
        return fromto;
    }

    public void setFromto(String fromto) {
        this.fromto = fromto;
    }


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

    public String getVehiclenumber() {
        return vehiclenumber;
    }

    public void setVehiclenumber(String vehiclenumber) {
        this.vehiclenumber = vehiclenumber;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
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

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getQuantites() {
        return quantites;
    }

    public void setQuantites(String quantites) {
        this.quantites = quantites;
    }
}
