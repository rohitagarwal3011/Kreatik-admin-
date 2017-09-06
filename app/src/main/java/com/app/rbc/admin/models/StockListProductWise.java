
package com.app.rbc.admin.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class StockListProductWise {

    @SerializedName("meta")
    private Meta mMeta;
    @SerializedName("stock_details")
    private List<StockDetail> mStockDetails;

    public Meta getMeta() {
        return mMeta;
    }

    public void setMeta(Meta meta) {
        mMeta = meta;
    }

    public List<StockDetail> getStockDetails() {
        return mStockDetails;
    }

    public void setStockDetails(List<StockDetail> stockDetails) {
        mStockDetails = stockDetails;
    }

    public class StockDetail {

        @SerializedName("product")
        private String mProduct;
        @SerializedName("stocks")
        private List<StockCategoryDetails.StockDetail> mStocks;

        public String getProduct() {
            return mProduct;
        }

        public void setProduct(String product) {
            mProduct = product;
        }

        public List<StockCategoryDetails.StockDetail> getStocks() {
            return mStocks;
        }

        public void setStocks(List<StockCategoryDetails.StockDetail> stocks) {
            mStocks = stocks;
        }

    }


}
