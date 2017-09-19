
package com.app.rbc.admin.models;

import com.google.gson.annotations.SerializedName;
public class SiteDetail {

    @SerializedName("id")
    private Long mId;
    @SerializedName("location")
    private String mLocation;
    @SerializedName("name")
    private String mName;
    @SerializedName("site_incharge")
    private String mSiteIncharge;
    @SerializedName("site_type")
    private String mSiteType;

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getSiteIncharge() {
        return mSiteIncharge;
    }

    public void setSiteIncharge(String siteIncharge) {
        mSiteIncharge = siteIncharge;
    }

    public String getSiteType() {
        return mSiteType;
    }

    public void setSiteType(String siteType) {
        mSiteType = siteType;
    }

}
