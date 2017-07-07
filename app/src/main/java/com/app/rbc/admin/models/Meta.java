
package com.app.rbc.admin.models;


import com.google.gson.annotations.SerializedName;

public class Meta {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private int mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

}
