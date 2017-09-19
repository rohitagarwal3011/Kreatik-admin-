package com.app.rbc.admin.models.db.models.site_overview;

import com.orm.SugarRecord;

/**
 * Created by jeet on 19/9/17.
 */

public class Requirement extends SugarRecord{

    private String reqid;
    private String title;
    private String createdon;
    private String status,purpose,raisedby,fulfilled,site,category,desc;
    private String products;
    private String quantities;
    private String remquantities;

    public Requirement() {

    }


    public String getReqid() {
        return reqid;
    }

    public void setReqid(String reqid) {
        this.reqid = reqid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedon() {
        return createdon;
    }

    public void setCreatedon(String createdon) {
        this.createdon = createdon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getRaisedby() {
        return raisedby;
    }

    public void setRaisedby(String raisedby) {
        this.raisedby = raisedby;
    }

    public String getFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(String fulfilled) {
        this.fulfilled = fulfilled;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public String getRemquantities() {
        return remquantities;
    }

    public void setRemquantities(String remquantities) {
        this.remquantities = remquantities;
    }
}
