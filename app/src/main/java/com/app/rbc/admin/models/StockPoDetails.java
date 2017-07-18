
package com.app.rbc.admin.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class StockPoDetails {

    @SerializedName("meta")
    private Meta mMeta;
    @SerializedName("po_details")
    private List<PoDetail> mPoDetails;
    @SerializedName("vehicle_details")
    private List<VehicleDetail> mVehicleDetails;
    @SerializedName("vendor_details")
    private List<VendorDetail> mVendorDetails;

    public Meta getMeta() {
        return mMeta;
    }

    public void setMeta(Meta meta) {
        mMeta = meta;
    }

    public List<PoDetail> getPoDetails() {
        return mPoDetails;
    }

    public void setPoDetails(List<PoDetail> poDetails) {
        mPoDetails = poDetails;
    }

    public List<VehicleDetail> getVehicleDetails() {
        return mVehicleDetails;
    }

    public void setVehicleDetails(List<VehicleDetail> vehicleDetails) {
        mVehicleDetails = vehicleDetails;
    }

    public List<VendorDetail> getVendorDetails() {
        return mVendorDetails;
    }

    public void setVendorDetails(List<VendorDetail> vendorDetails) {
        mVendorDetails = vendorDetails;
    }

    public class VendorDetail {

        @SerializedName("vendor_add")
        private String mVendorAdd;
        @SerializedName("vendor_name")
        private String mVendorName;
        @SerializedName("vendor_phone")
        private Long mVendorPhone;

        public String getVendorAdd() {
            return mVendorAdd;
        }

        public void setVendorAdd(String vendorAdd) {
            mVendorAdd = vendorAdd;
        }

        public String getVendorName() {
            return mVendorName;
        }

        public void setVendorName(String vendorName) {
            mVendorName = vendorName;
        }

        public Long getVendorPhone() {
            return mVendorPhone;
        }

        public void setVendorPhone(Long vendorPhone) {
            mVendorPhone = vendorPhone;
        }

    }
    public class VehicleDetail {

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
        @SerializedName("product")
        private String mProduct;
        @SerializedName("quantity")
        private Long mQuantity;
        @SerializedName("source")
        private String mSource;
        @SerializedName("source_type")
        private String mSourceType;
        @SerializedName("status")
        private String mStatus;
        @SerializedName("vehicle_number")
        private String mVehicleNumber;

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
    public class PoDetail {

        @SerializedName("created_by")
        private String mCreatedBy;
        @SerializedName("creation_dt")
        private String mCreationDt;
        @SerializedName("for_product")
        private String mForProduct;
        @SerializedName("pay_mode")
        private String mPayMode;
        @SerializedName("po_num")
        private String mPoNum;
        @SerializedName("price")
        private Long mPrice;
        @SerializedName("quantity")
        private Long mQuantity;
        @SerializedName("status")
        private String mStatus;
        @SerializedName("title")
        private String mTitle;

        public String getCreatedBy() {
            return mCreatedBy;
        }

        public void setCreatedBy(String createdBy) {
            mCreatedBy = createdBy;
        }

        public String getCreationDt() {
            return mCreationDt;
        }

        public void setCreationDt(String creationDt) {
            mCreationDt = creationDt;
        }

        public String getForProduct() {
            return mForProduct;
        }

        public void setForProduct(String forProduct) {
            mForProduct = forProduct;
        }

        public String getPayMode() {
            return mPayMode;
        }

        public void setPayMode(String payMode) {
            mPayMode = payMode;
        }

        public String getPoNum() {
            return mPoNum;
        }

        public void setPoNum(String poNum) {
            mPoNum = poNum;
        }

        public Long getPrice() {
            return mPrice;
        }

        public void setPrice(Long price) {
            mPrice = price;
        }

        public Long getQuantity() {
            return mQuantity;
        }

        public void setQuantity(Long quantity) {
            mQuantity = quantity;
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
