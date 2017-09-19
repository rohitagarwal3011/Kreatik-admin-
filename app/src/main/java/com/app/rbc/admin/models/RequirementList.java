
package com.app.rbc.admin.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RequirementList {

    @SerializedName("meta")
    private Meta mMeta;
    @SerializedName("req_list")
    private List<ReqList> mReqList;

    public Meta getMeta() {
        return mMeta;
    }

    public void setMeta(Meta meta) {
        mMeta = meta;
    }

    public List<ReqList> getReqList() {
        return mReqList;
    }

    public void setReqList(List<ReqList> reqList) {
        mReqList = reqList;
    }

    public class ReqList {

        @SerializedName("details")
        private List<Detail> mDetails;
        @SerializedName("products")
        private List<Product> mProducts;
        @SerializedName("rq_id")
        private String mRqId;

        public List<Detail> getDetails() {
            return mDetails;
        }

        public void setDetails(List<Detail> details) {
            mDetails = details;
        }

        public List<Product> getProducts() {
            return mProducts;
        }

        public void setProducts(List<Product> products) {
            mProducts = products;
        }


        public String getRqId() {
            return mRqId;
        }

        public void setRqId(String rqId) {
            mRqId = rqId;
        }

        @SerializedName("site_details")
        private List<SiteDetail> siteDetails;

        public List<SiteDetail> getSiteDetails() {
            return siteDetails;
        }

        public void setSiteDetails(List<SiteDetail> siteDetails) {
            this.siteDetails = siteDetails;
        }

        public class Detail {

            @SerializedName("category")
            private String mCategory;
            @SerializedName("created_on")
            private String mCreatedOn;
            @SerializedName("desc")
            private String mDesc;
            @SerializedName("fulfilled")
            private Boolean mFulfilled;
            @SerializedName("purpose")
            private String mPurpose;
            @SerializedName("raised_by")
            private String mRaisedBy;
            @SerializedName("site")
            private String mSite;
            @SerializedName("status")
            private String mStatus;
            @SerializedName("title")
            private String mTitle;



            public String getCategory() {
                return mCategory;
            }

            public void setCategory(String category) {
                mCategory = category;
            }

            public String getCreatedOn() {
                return mCreatedOn;
            }

            public void setCreatedOn(String createdOn) {
                mCreatedOn = createdOn;
            }

            public String getDesc() {
                return mDesc;
            }

            public void setDesc(String desc) {
                mDesc = desc;
            }

            public Boolean getFulfilled() {
                return mFulfilled;
            }

            public void setFulfilled(Boolean fulfilled) {
                mFulfilled = fulfilled;
            }

            public String getPurpose() {
                return mPurpose;
            }

            public void setPurpose(String purpose) {
                mPurpose = purpose;
            }

            public String getRaisedBy() {
                return mRaisedBy;
            }

            public void setRaisedBy(String raisedBy) {
                mRaisedBy = raisedBy;
            }

            public String getSite() {
                return mSite;
            }

            public void setSite(String site) {
                mSite = site;
            }

            public String getStatus() {
                return mStatus;
            }

            public void setStatus(String status) {
                mStatus = status;
            }

            public String getTitle() {
                return mTitle;
            }

            public void setTitle(String title) {
                mTitle = title;
            }

        }


    }

}
