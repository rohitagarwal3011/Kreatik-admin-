
package com.app.rbc.admin.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Tasklogs {

    @SerializedName("data")
    private List<Data> mData;
    @SerializedName("logs")
    private List<Log> mLogs;
    @SerializedName("meta")
    private Meta mMeta;

    public List<Data> getData() {
        return mData;
    }

    public void setData(List<Data> data) {
        mData = data;
    }

    public List<Log> getLogs() {
        return mLogs;
    }

    public void setLogs(List<Log> logs) {
        mLogs = logs;
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

        @SerializedName("create_time")
        private String mCreateTime;
        @SerializedName("deadline")
        private String mDeadline;
        @SerializedName("docs")
        private String mDocs;
        @SerializedName("from_user")
        private String mFromUser;
        @SerializedName("status")
        private String mStatus;
        @SerializedName("task_desc")
        private String mTaskDesc;
        @SerializedName("task_id")
        private String mTaskId;
        @SerializedName("title")
        private String mTitle;
        @SerializedName("to_user")
        private String mToUser;
        @SerializedName("type")
        private String mType;

        public String getCreateTime() {
            return mCreateTime;
        }

        public void setCreateTime(String createTime) {
            mCreateTime = createTime;
        }

        public String getDeadline() {
            return mDeadline;
        }

        public void setDeadline(String deadline) {
            mDeadline = deadline;
        }

        public String getDocs() {
            return mDocs;
        }

        public void setDocs(String docs) {
            mDocs = docs;
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

        public String getTaskDesc() {
            return mTaskDesc;
        }

        public void setTaskDesc(String taskDesc) {
            mTaskDesc = taskDesc;
        }

        public String getTaskId() {
            return mTaskId;
        }

        public void setTaskId(String taskId) {
            mTaskId = taskId;
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

        public String getType() {
            return mType;
        }

        public void setType(String type) {
            mType = type;
        }

    }

    public class Log {

        @SerializedName("change_time")
        private String mChangeTime;
        @SerializedName("changed_by")
        private String mChangedBy;
        @SerializedName("comment")
        private String mComment;
        @SerializedName("docs")
        private String mDocs;
        @SerializedName("status")
        private String mStatus;
        @SerializedName("task_id")
        private String mTaskId;



        @SerializedName("log_type")
        private String mLogtype;


        public String getmLogtype() {
            return mLogtype;
        }

        public void setmLogtype(String mLogtype) {
            this.mLogtype = mLogtype;
        }

        public String getChangeTime() {
            return mChangeTime;
        }

        public void setChangeTime(String changeTime) {
            mChangeTime = changeTime;
        }

        public String getChangedBy() {
            return mChangedBy;
        }

        public void setChangedBy(String changedBy) {
            mChangedBy = changedBy;
        }

        public String getComment() {
            return mComment;
        }

        public void setComment(String comment) {
            mComment = comment;
        }

        public String getDocs() {
            return mDocs;
        }

        public void setDocs(String docs) {
            mDocs = docs;
        }

        public String getStatus() {
            return mStatus;
        }

        public void setStatus(String status) {
            mStatus = status;
        }

        public String getTaskId() {
            return mTaskId;
        }

        public void setTaskId(String taskId) {
            mTaskId = taskId;
        }

    }

}
