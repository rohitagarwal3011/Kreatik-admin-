
package com.app.rbc.admin.models;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class StockCategories {

    @SerializedName("category_list")
    private List<CategoryList> mCategoryList;
    @SerializedName("meta")
    private Meta mMeta;

    public List<CategoryList> getCategoryList() {
        return mCategoryList;
    }

    public void setCategoryList(List<CategoryList> categoryList) {
        mCategoryList = categoryList;
    }

    public Meta getMeta() {
        return mMeta;
    }

    public void setMeta(Meta meta) {
        mMeta = meta;
    }

    public class CategoryList {

        @SerializedName("category")
        private String mCategory;
        @SerializedName("products")
        private List<Product> mProducts;
        @SerializedName("unit")
        private String unit;

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }




        public String getCategory() {
            return mCategory;
        }

        public void setCategory(String category) {
            mCategory = category;
        }

        public List<Product> getProducts() {
            return mProducts;
        }

        public void setProducts(List<Product> products) {
            mProducts = products;
        }

        public class Product {

            @SerializedName("product")
            private String mProduct;

            public String getProduct() {
                return mProduct;
            }

            public void setProduct(String product) {
                mProduct = product;
            }

        }

    }

}
