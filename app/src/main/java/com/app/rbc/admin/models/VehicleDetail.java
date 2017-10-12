package com.app.rbc.admin.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rohit on 3/9/17.
 */


public class VehicleDetail {

    @SerializedName("details")
    private List<Detail> mDetails;
    @SerializedName("products")
    private List<Product> mProducts;
    @SerializedName("trans_id")
    private String mTransId;

    @SerializedName("src_details")
    private List<SiteDetail> src_details;
    @SerializedName("dest_details")
    private List<SiteDetail> dest_details;
    @SerializedName("vendor_details")
    private List<Vendors.VendorList> vendor_details;

    public List<SiteDetail> getSrc_details() {
        return src_details;
    }

    public void setSrc_details(List<SiteDetail> src_details) {
        this.src_details = src_details;
    }

    public List<SiteDetail> getDest_details() {
        return dest_details;
    }

    public void setDest_details(List<SiteDetail> dest_details) {
        this.dest_details = dest_details;
    }

    public List<Vendors.VendorList> getVendor_details() {
        return vendor_details;
    }

    public void setVendor_details(List<Vendors.VendorList> vendor_details) {
        this.vendor_details = vendor_details;
    }

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

    public String getTransId() {
        return mTransId;
    }

    public void setTransId(String transId) {
        mTransId = transId;
    }

    public class Detail {

        @SerializedName("challan_img")
        private String mChallanImg;
        @SerializedName("challan_num")
        private String mChallanNum;
        @SerializedName("dest_type")
        private String mDestType;
        @SerializedName("destination")
        private String mDestination;
        @SerializedName("dispatch_dt")
        private String mDispatchDt;
        @SerializedName("driver")
        private String mDriver;
        @SerializedName("source")
        private String mSource;
        @SerializedName("source_type")
        private String mSourceType;
        @SerializedName("status")
        private String mStatus;
        @SerializedName("vehicle_number")
        private String mVehicleNumber;

        public String getChallanImg() {
            return mChallanImg;
        }

        public void setChallanImg(String challanImg) {
            mChallanImg = challanImg;
        }

        public String getChallanNum() {
            return mChallanNum;
        }

        public void setChallanNum(String challanNum) {
            mChallanNum = challanNum;
        }

        public String getDestType() {
            return mDestType;
        }

        public void setDestType(String destType) {
            mDestType = destType;
        }

        public String getDestination() {
            return mDestination;
        }

        public void setDestination(String destination) {
            mDestination = destination;
        }

        public String getDispatchDt() {
            return mDispatchDt;
        }

        public void setDispatchDt(String dispatchDt) {
            mDispatchDt = dispatchDt;
        }

        public String getDriver() {
            return mDriver;
        }

        public void setDriver(String driver) {
            mDriver = driver;
        }

        public String getSource() {
            return mSource;
        }

        public void setSource(String source) {
            mSource = source;
        }

        public String getSourceType() {
            return mSourceType;
        }

        public void setSourceType(String sourceType) {
            mSourceType = sourceType;
        }

        public String getStatus() {
            return mStatus;
        }

        public void setStatus(String status) {
            mStatus = status;
        }

        public String getVehicleNumber() {
            return mVehicleNumber;
        }

        public void setVehicleNumber(String vehicleNumber) {
            mVehicleNumber = vehicleNumber;
        }

    }

    public class Product {

        @SerializedName("product")
        private String mProduct;
        @SerializedName("quantity")
        private Long mQuantity;

        public String getProduct() {
            return mProduct;
        }

        public void setProduct(String product) {
            mProduct = product;
        }

        public Long getQuantity() {
            return mQuantity;
        }

        public void setQuantity(Long quantity) {
            mQuantity = quantity;
        }

    }


}