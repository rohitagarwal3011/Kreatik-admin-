package com.app.rbc.admin.models.db.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

/**
 * Created by jeet on 6/9/17.
 */

public class Vendor extends SugarRecord{

    public Vendor() {

    }
    @SerializedName("vendor_id")
    @Expose
    private String vendor_id;

    @SerializedName("vendor_name")
    @Expose
    private String name;
    @SerializedName("vendor_add")
    @Expose
    private String address;
    @SerializedName("vendor_phone")
    @Expose
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }
}
