
package com.app.rbc.admin.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class Todolist {

    @SerializedName("data")
    private List<Data> mData;
    @SerializedName("meta")
    private Meta mMeta;

    @SerializedName("data1")
    private List<Data1> mData1;


    public List<Data1> getData1() {
        return mData1;
    }

    public void setData1(List<Data1> data) {
        mData1 = data;
    }

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

    public class Meta {

        @SerializedName("message")
        private String mMessage;
        @SerializedName("status")
        private String mStatus;

        public String getMessage() {
            return mMessage;
        }

        public void setMessage(String message) {
            mMessage = message;
        }

        public String getStatus() {
            return mStatus;
        }

        public void setStatus(String status) {
            mStatus = status;
        }

    }

    public class Data {

        @SerializedName("deadline")
        private String mDeadline;
        @SerializedName("from_user")
        private String mFromUser;
        @SerializedName("status")
        private String mStatus;
        @SerializedName("title")
        private String mTitle;
        @SerializedName("to_user")
        private String mToUser;
        @SerializedName("task_id")
        private String mTask_id;

        @SerializedName("type")
        private String mTask_type;

        public String getTask_type() {
            return mTask_type;
        }

        public void setTask_type(String mTask_type) {
            this.mTask_type = mTask_type;
        }

        public String getTask_id() {
            return mTask_id;
        }

        public void setTask_id(String mTask_id) {
            this.mTask_id = mTask_id;
        }

        public String getDeadline() {
            return mDeadline;
        }

        public void setDeadline(String deadline) {
            mDeadline = deadline;
        }

        public String getFromUser() {
            return mFromUser;
        }

        public void setFromUser(String fromUser) {
            mFromUser = fromUser;
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

        public String getToUser() {
            return mToUser;
        }

        public void setToUser(String toUser) {
            mToUser = toUser;
        }

    }


    public class Data1 {

        @SerializedName("deadline")
        private String mDeadline;
        @SerializedName("from_user")
        private String mFromUser;
        @SerializedName("status")
        private String mStatus;
        @SerializedName("title")
        private String mTitle;
        @SerializedName("to_user")
        private String mToUser;

        @SerializedName("task_id")
        private String mTask_id;

        @SerializedName("type")
        private String mTask_type;

        public String getTask_type() {
            return mTask_type;
        }

        public String getTask_id() {
            return mTask_id;
        }

        public void setTask_id(String mTask_id) {
            this.mTask_id = mTask_id;
        }

        public String getDeadline() {
            return mDeadline;
        }

        public void setDeadline(String deadline) {
            mDeadline = deadline;
        }

        public String getFromUser() {
            return mFromUser;
        }

        public void setFromUser(String fromUser) {
            mFromUser = fromUser;
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

        public String getToUser() {
            return mToUser;
        }

        public void setToUser(String toUser) {
            mToUser = toUser;
        }

    }

}
