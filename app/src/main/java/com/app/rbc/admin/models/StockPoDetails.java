
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

    public class PoDetail {

        @SerializedName("details")
        private List<Detail> mDetails;
        @SerializedName("po_id")
        private String mPoId;
        @SerializedName("products")
        private List<Product> mProducts;

        public List<Detail> getDetails() {
            return mDetails;
        }

        public void setDetails(List<Detail> details) {
            mDetails = details;
        }

        public String getPoId() {
            return mPoId;
        }

        public void setPoId(String poId) {
            mPoId = poId;
        }

        public List<Product> getProducts() {
            return mProducts;
        }

        public void setProducts(List<Product> products) {
            mProducts = products;
        }

        public class Detail {

            @SerializedName("category")
            private String mCategory;
            @SerializedName("created_by")
            private String mCreatedBy;
            @SerializedName("creation_dt")
            private String mCreationDt;
            @SerializedName("fulfilled")
            private Boolean mFulfilled;
            @SerializedName("pay_mode")
            private String mPayMode;
            @SerializedName("po_img")
            private String mPoImg;
            @SerializedName("price")
            private Long mPrice;
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

            public Boolean getFulfilled() {
                return mFulfilled;
            }

            public void setFulfilled(Boolean fulfilled) {
                mFulfilled = fulfilled;
            }

            public String getPayMode() {
                return mPayMode;
            }

            public void setPayMode(String payMode) {
                mPayMode = payMode;
            }

            public String getPoImg() {
                return mPoImg;
            }

            public void setPoImg(String poImg) {
                mPoImg = poImg;
            }

            public Long getPrice() {
                return mPrice;
            }

            public void setPrice(Long price) {
                mPrice = price;
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

        public class Product {

            @SerializedName("for_product")
            private String mForProduct;
            @SerializedName("quantity")
            private Long mQuantity;
            @SerializedName("rem_quantity")
            private Long mRemQuantity;

            public String getForProduct() {
                return mForProduct;
            }

            public void setForProduct(String forProduct) {
                mForProduct = forProduct;
            }

            public Long getQuantity() {
                return mQuantity;
            }

            public void setQuantity(Long quantity) {
                mQuantity = quantity;
            }

            public Long getRemQuantity() {
                return mRemQuantity;
            }

            public void setRemQuantity(Long remQuantity) {
                mRemQuantity = remQuantity;
            }

        }


    }

    public class VendorDetail {

        @SerializedName("vendor_add")
        private String mVendorAdd;
        @SerializedName("vendor_id")
        private String mVendorId;
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

        public String getVendorId() {
            return mVendorId;
        }

        public void setVendorId(String vendorId) {
            mVendorId = vendorId;
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


}