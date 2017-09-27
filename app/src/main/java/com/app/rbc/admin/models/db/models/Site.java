package com.app.rbc.admin.models.db.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

/**
 * Created by jeet on 6/9/17.
 */

public class Site extends SugarRecord{

    public Site() {

    }

    @SerializedName("name")
    @Expose
    private String name;


    @SerializedName("site_type")
    @Expose
    private String type;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("site_incharge")
    @Expose
    private String incharge;

    private int statestore;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIncharge() {
        return incharge;
    }

    public void setIncharge(String incharge) {
        this.incharge = incharge;
    }

    public int getStatestore() {
        return statestore;
    }

    public void setStatestore(int statestore) {
        this.statestore = statestore;
    }
}
