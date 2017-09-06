
package com.app.rbc.admin.models;


import com.google.gson.annotations.SerializedName;


public class SiteDetail {

    @SerializedName("id")
    private Long mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("site_type")
    private String mSiteType;

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getSiteType() {
        return mSiteType;
    }

    public void setSiteType(String siteType) {
        mSiteType = siteType;
    }

}
