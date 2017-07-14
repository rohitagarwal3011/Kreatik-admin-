
package com.app.rbc.admin.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class TodaysAbsentees {

    @SerializedName("leave_cnt")
    private List<LeaveCnt> mLeaveCnt;

    @SerializedName("hd_cnt")
    private List<HdCnt> mHdCnt;
    @SerializedName("present_cnt")
    private List<PresentCnt> mPresentCnt;



    @SerializedName("meta")
    private Meta mMeta;
    @SerializedName("today_absent")
    private List<TodayAbsent> mTodayAbsent;
    @SerializedName("today_hd")
    private List<TodayHd> mTodayHd;

    public Boolean getToday_status() {
        return today_status;
    }

    public void setToday_status(Boolean today_status) {
        this.today_status = today_status;
    }

    @SerializedName("today_status")
    private Boolean today_status;



    public List<LeaveCnt> getLeaveCnt() {
        return mLeaveCnt;
    }

    public void setLeaveCnt(List<LeaveCnt> leaveCnt) {
        mLeaveCnt = leaveCnt;
    }
    public List<HdCnt> getmHdCnt() {
        return mHdCnt;
    }

    public void setmHdCnt(List<HdCnt> mHdCnt) {
        this.mHdCnt = mHdCnt;
    }

    public List<PresentCnt> getmPresentCnt() {
        return mPresentCnt;
    }

    public void setmPresentCnt(List<PresentCnt> mPresentCnt) {
        this.mPresentCnt = mPresentCnt;
    }

    public Meta getMeta() {
        return mMeta;
    }

    public void setMeta(Meta meta) {
        mMeta = meta;
    }

    public List<TodayAbsent> getTodayAbsent() {
        return mTodayAbsent;
    }

    public void setTodayAbsent(List<TodayAbsent> todayAbsent) {
        mTodayAbsent = todayAbsent;
    }

    public List<TodayHd> getTodayHd() {
        return mTodayHd;
    }

    public void setTodayHd(List<TodayHd> todayHd) {
        mTodayHd = todayHd;
    }


    public class TodayHd {

        @SerializedName("employee")
        private String mEmployee;

        public String getEmployee() {
            return mEmployee;
        }

        public void setEmployee(String employee) {
            mEmployee = employee;
        }

    }
    public class TodayAbsent {

        @SerializedName("employee")
        private String mEmployee;

        public String getEmployee() {
            return mEmployee;
        }

        public void setEmployee(String employee) {
            mEmployee = employee;
        }

    }

    public class LeaveCnt {

        @SerializedName("dcount")
        private Long mDcount;
        @SerializedName("employee")
        private String mEmployee;

        public Long getDcount() {
            return mDcount;
        }

        public void setDcount(Long dcount) {
            mDcount = dcount;
        }

        public String getEmployee() {
            return mEmployee;
        }

        public void setEmployee(String employee) {
            mEmployee = employee;
        }

    }

    public class HdCnt {

        @SerializedName("dcount")
        private Long mDcount;
        @SerializedName("employee")
        private String mEmployee;

        public Long getDcount() {
            return mDcount;
        }

        public void setDcount(Long dcount) {
            mDcount = dcount;
        }

        public String getEmployee() {
            return mEmployee;
        }

        public void setEmployee(String employee) {
            mEmployee = employee;
        }

    }
    public class PresentCnt {

        @SerializedName("dcount")
        private Long mDcount;
        @SerializedName("employee")
        private String mEmployee;

        public Long getDcount() {
            return mDcount;
        }

        public void setDcount(Long dcount) {
            mDcount = dcount;
        }

        public String getEmployee() {
            return mEmployee;
        }

        public void setEmployee(String employee) {
            mEmployee = employee;
        }

    }
}
