
package com.app.rbc.admin.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class RequirementDetails {

    @SerializedName("meta")
    private Meta mMeta;
    @SerializedName("po_req_vehicle_details")
    private List<VehicleDetail> mPoReqVehicleDetails;
    @SerializedName("req_details")
    private List<ReqDetail> mReqDetails;
    @SerializedName("vehicle_details")
    private List<VehicleDetail> mVehicleDetails;

    public Meta getMeta() {
        return mMeta;
    }

    public void setMeta(Meta meta) {
        mMeta = meta;
    }

    public List<VehicleDetail> getPoReqVehicleDetails() {
        return mPoReqVehicleDetails;
    }

    public void setPoReqVehicleDetails(List<VehicleDetail> poReqVehicleDetails) {
        mPoReqVehicleDetails = poReqVehicleDetails;
    }

    public List<ReqDetail> getReqDetails() {
        return mReqDetails;
    }

    public void setReqDetails(List<ReqDetail> reqDetails) {
        mReqDetails = reqDetails;
    }

    public List<VehicleDetail> getVehicleDetails() {
        return mVehicleDetails;
    }

    public void setVehicleDetails(List<VehicleDetail> vehicleDetails) {
        mVehicleDetails = vehicleDetails;
    }


    public class ReqDetail {

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

            @SerializedName("site_details")
            private List<SiteDetail> siteDetails;

            public List<SiteDetail> getSiteDetails() {
                return siteDetails;
            }

            public void setSiteDetails(List<SiteDetail> siteDetails) {
                this.siteDetails = siteDetails;
            }

            public String getmCategory() {
                return mCategory;
            }

            public void setmCategory(String mCategory) {
                this.mCategory = mCategory;
            }

            public String getmCreatedOn() {
                return mCreatedOn;
            }

            public void setmCreatedOn(String mCreatedOn) {
                this.mCreatedOn = mCreatedOn;
            }

            public String getmDesc() {
                return mDesc;
            }

            public void setmDesc(String mDesc) {
                this.mDesc = mDesc;
            }

            public Boolean getmFulfilled() {
                return mFulfilled;
            }

            public void setmFulfilled(Boolean mFulfilled) {
                this.mFulfilled = mFulfilled;
            }

            public String getmPurpose() {
                return mPurpose;
            }

            public void setmPurpose(String mPurpose) {
                this.mPurpose = mPurpose;
            }

            public String getmRaisedBy() {
                return mRaisedBy;
            }

            public void setmRaisedBy(String mRaisedBy) {
                this.mRaisedBy = mRaisedBy;
            }

            public String getmSite() {
                return mSite;
            }

            public void setmSite(String mSite) {
                this.mSite = mSite;
            }

            public String getmStatus() {
                return mStatus;
            }

            public void setmStatus(String mStatus) {
                this.mStatus = mStatus;
            }

            public String getmTitle() {
                return mTitle;
            }

            public void setmTitle(String mTitle) {
                this.mTitle = mTitle;
            }
        }



    }

}
