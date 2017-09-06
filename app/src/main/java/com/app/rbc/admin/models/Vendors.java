
package com.app.rbc.admin.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Vendors {

    @SerializedName("meta")
    private Meta mMeta;
    @SerializedName("vendor_list")
    private List<VendorList> mVendorList;

    public Meta getMeta() {
        return mMeta;
    }

    public void setMeta(Meta meta) {
        mMeta = meta;
    }

    public List<VendorList> getVendorList() {
        return mVendorList;
    }

    public void setVendorList(List<VendorList> vendorList) {
        mVendorList = vendorList;
    }


    public class VendorList {

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
