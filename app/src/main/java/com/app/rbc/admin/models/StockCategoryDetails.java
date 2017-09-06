
package com.app.rbc.admin.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class StockCategoryDetails {

    @SerializedName("meta")
    private Meta mMeta;
    @SerializedName("po_details")
    private List<PoDetail> mPoDetails;
    @SerializedName("stock_details")
    private List<StockDetail> mStockDetails;
    @SerializedName("trans_details")
    private List<TransactionDetail> mTransactionDetails;

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

    public List<StockDetail> getStockDetails() {
        return mStockDetails;
    }

    public void setStockDetails(List<StockDetail> stockDetails) {
        mStockDetails = stockDetails;
    }

    public List<TransactionDetail> getTransactionDetails() {
        return mTransactionDetails;
    }

    public void setTransactionDetails(List<TransactionDetail> transactionDetails) {
        mTransactionDetails = transactionDetails;
    }
    public class StockDetail {

        @SerializedName("product")
        private String mProduct;
        @SerializedName("quantity")
        private Long mQuantity;
        @SerializedName("where")
        private String mWhere;
        @SerializedName("stock_type")
        private String mstock_type;

        public String getMstock_type() {
            return mstock_type;
        }

        public void setMstock_type(String mstock_type) {
            this.mstock_type = mstock_type;
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

        public String getWhere() {
            return mWhere;
        }

        public void setWhere(String where) {
            mWhere = where;
        }

    }

    public class PoDetail {

        @SerializedName("details")
        private List<Detail> mDetails;
        @SerializedName("po_details")
        private List<PoDetail> mPoDetails;
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

        public List<PoDetail> getPoDetails() {
            return mPoDetails;
        }

        public void setPoDetails(List<PoDetail> poDetails) {
            mPoDetails = poDetails;
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


    public class TransactionDetail {



            @SerializedName("details")
            private List<Detail> mDetails;
            @SerializedName("products")
            private List<Product> mProducts;
            @SerializedName("trans_id")
            private String mTransId;

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


}
