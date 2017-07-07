
package com.app.rbc.admin.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Employee {

    @SerializedName("data")
    private List<Data> mData;
    @SerializedName("meta")
    private Meta mMeta;

    public List<Data> getData() {
        return mData;
    }

    public void setData(List<Data> data) {
        mData = data;
    }

    public Meta getMeta() {
        return mMeta;
    }

    public void setMeta(Meta meta) {
        mMeta = meta;
    }

    public class Data {

        @SerializedName("user_id")
        private String mUserId;
        @SerializedName("user_name")
        private String mUserName;

        @SerializedName("pic_url")
        private String mpic_url;

        public String getMpic_url() {
            return mpic_url;
        }

        public void setMpic_url(String mpic_url) {
            this.mpic_url = mpic_url;
        }

        public String getUserId() {
            return mUserId;
        }

        public void setUserId(String userId) {
            mUserId = userId;
        }

        public String getUserName() {
            return mUserName;
        }

        public void setUserName(String userName) {
            mUserName = userName;
        }

    }

    public class Meta {

        @SerializedName("status")
        private String mStatus;
        @SerializedName("message")
        private String mMessage;

        public String getCode() {
            return mStatus;
        }

        public void setCode(String code) {
            mStatus = code;
        }

        public String getMessage() {
            return mMessage;
        }

        public void setMessage(String message) {
            mMessage = message;
        }

    }



}
