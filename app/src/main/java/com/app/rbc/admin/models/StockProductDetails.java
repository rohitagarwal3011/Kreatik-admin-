
package com.app.rbc.admin.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class StockProductDetails {

    @SerializedName("meta")
    private Meta mMeta;
    @SerializedName("po_details")
    private List<PoDetail> mPoDetails;
    @SerializedName("stock_details")
    private List<StockDetail> mStockDetails;
    @SerializedName("transaction_details")
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

        @SerializedName("creation_dt")
        private String mCreationDt;
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

        public String getCreationDt() {
            return mCreationDt;
        }

        public void setCreationDt(String creationDt) {
            mCreationDt = creationDt;
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

    public class TransactionDetail {

        @SerializedName("destination")
        private String mDestination;
        @SerializedName("dispatch_dt")
        private String mDispatchDt;
        @SerializedName("quantity")
        private Long mQuantity;
        @SerializedName("source")
        private String mSource;

        @SerializedName("source_type")
        private String mSource_type;
        @SerializedName("dest_type")
        private String mDestination_type;

        public String getSource_type() {
            return mSource_type;
        }

        public void setSource_type(String mSource_type) {
            this.mSource_type = mSource_type;
        }

        public String getDestination_type() {
            return mDestination_type;
        }

        public void setDestination_type(String mDestination_type) {
            this.mDestination_type = mDestination_type;
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

    }


}
