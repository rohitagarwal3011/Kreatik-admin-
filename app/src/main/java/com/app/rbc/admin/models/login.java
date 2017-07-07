
package com.app.rbc.admin.models;


import com.google.gson.annotations.SerializedName;


public class login {

    @SerializedName("data")
    private Data mData;
    @SerializedName("meta")
    private Meta mMeta;

    public Data getData() {
        return mData;
    }

    public void setData(Data data) {
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


    public class Data {

        @SerializedName("Email")
        private String mEmail;
        @SerializedName("Gcm_id")
        private String mGcmId;
        @SerializedName("Mobile")
        private Long mMobile;
        @SerializedName("Role")
        private String mRole;
        @SerializedName("State")
        private Boolean mState;
        @SerializedName("User Token")
        private String mUserToken;
        @SerializedName("Username")
        private String mUsername;
        @SerializedName("User_id")
        private String mUser_id;

        public String getmProfile_image() {
            return mProfile_image;
        }

        public void setmProfile_image(String mProfile_image) {
            this.mProfile_image = mProfile_image;
        }

        @SerializedName("Profile_image")
        private String mProfile_image;

        public String getUser_id() {
            return mUser_id;
        }

        public void setUser_id(String mUser_id) {
            this.mUser_id = mUser_id;
        }

        public String getEmail() {
            return mEmail;
        }

        public void setEmail(String Email) {
            mEmail = Email;
        }

        public String getGcmId() {
            return mGcmId;
        }

        public void setGcmId(String GcmId) {
            mGcmId = GcmId;
        }

        public Long getMobile() {
            return mMobile;
        }

        public void setMobile(Long Mobile) {
            mMobile = Mobile;
        }

        public String getRole() {
            return mRole;
        }

        public void setRole(String Role) {
            mRole = Role;
        }

        public Boolean getState() {
            return mState;
        }

        public void setState(Boolean State) {
            mState = State;
        }

        public String getUserToken() {
            return mUserToken;
        }

        public void setUserToken(String UserToken) {
            mUserToken = UserToken;
        }

        public String getUsername() {
            return mUsername;
        }

        public void setUsername(String Username) {
            mUsername = Username;
        }

    }


}
