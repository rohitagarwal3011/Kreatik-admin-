
package com.app.rbc.admin.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class EmployeewiseAttendance {

    @SerializedName("meta")
    private Meta mMeta;
    @SerializedName("month_absent_cnt")
    private Long mMonthAbsentCnt;
    @SerializedName("month_data")
    private List<MonthData> mMonthData;
    @SerializedName("month_hd_cnt")
    private Long mMonthHdCnt;
    @SerializedName("month_present_cnt")
    private Long mMonthPresentCnt;
    @SerializedName("today_attendance")
    private String mTodayAttendance;
    @SerializedName("today_data")
    private List<TodayData> mTodayData;
    @SerializedName("week_absent_cnt")
    private Long mWeekAbsentCnt;
    @SerializedName("week_data")
    private List<WeekData> mWeekData;
    @SerializedName("week_hd_cnt")
    private Long mWeekHdCnt;
    @SerializedName("week_present_cnt")
    private Long mWeekPresentCnt;

    public Meta getMeta() {
        return mMeta;
    }

    public void setMeta(Meta meta) {
        mMeta = meta;
    }

    public Long getMonthAbsentCnt() {
        return mMonthAbsentCnt;
    }

    public void setMonthAbsentCnt(Long monthAbsentCnt) {
        mMonthAbsentCnt = monthAbsentCnt;
    }

    public List<MonthData> getMonthData() {
        return mMonthData;
    }

    public void setMonthData(List<MonthData> monthData) {
        mMonthData = monthData;
    }

    public Long getMonthHdCnt() {
        return mMonthHdCnt;
    }

    public void setMonthHdCnt(Long monthHdCnt) {
        mMonthHdCnt = monthHdCnt;
    }

    public Long getMonthPresentCnt() {
        return mMonthPresentCnt;
    }

    public void setMonthPresentCnt(Long monthPresentCnt) {
        mMonthPresentCnt = monthPresentCnt;
    }

    public String getTodayAttendance() {
        return mTodayAttendance;
    }

    public void setTodayAttendance(String todayAttendance) {
        mTodayAttendance = todayAttendance;
    }

    public List<TodayData> getTodayData() {
        return mTodayData;
    }

    public void setTodayData(List<TodayData> todayData) {
        mTodayData = todayData;
    }

    public Long getWeekAbsentCnt() {
        return mWeekAbsentCnt;
    }

    public void setWeekAbsentCnt(Long weekAbsentCnt) {
        mWeekAbsentCnt = weekAbsentCnt;
    }

    public List<WeekData> getWeekData() {
        return mWeekData;
    }

    public void setWeekData(List<WeekData> weekData) {
        mWeekData = weekData;
    }

    public Long getWeekHdCnt() {
        return mWeekHdCnt;
    }

    public void setWeekHdCnt(Long weekHdCnt) {
        mWeekHdCnt = weekHdCnt;
    }

    public Long getWeekPresentCnt() {
        return mWeekPresentCnt;
    }

    public void setWeekPresentCnt(Long weekPresentCnt) {
        mWeekPresentCnt = weekPresentCnt;
    }


    public class MonthData {

        @SerializedName("date")
        private String mDate;
        @SerializedName("employee")
        private String mEmployee;
        @SerializedName("remarks")
        private String mRemarks;
        @SerializedName("status")
        private String mStatus;

        public String getDate() {
            return mDate;
        }

        public void setDate(String date) {
            mDate = date;
        }

        public String getEmployee() {
            return mEmployee;
        }

        public void setEmployee(String employee) {
            mEmployee = employee;
        }

        public String getRemarks() {
            return mRemarks;
        }

        public void setRemarks(String remarks) {
            mRemarks = remarks;
        }

        public String getStatus() {
            return mStatus;
        }

        public void setStatus(String status) {
            mStatus = status;
        }

    }

    public class WeekData {

        @SerializedName("date")
        private String mDate;
        @SerializedName("employee")
        private String mEmployee;
        @SerializedName("remarks")
        private String mRemarks;
        @SerializedName("status")
        private String mStatus;

        public String getDate() {
            return mDate;
        }

        public void setDate(String date) {
            mDate = date;
        }

        public String getEmployee() {
            return mEmployee;
        }

        public void setEmployee(String employee) {
            mEmployee = employee;
        }

        public String getRemarks() {
            return mRemarks;
        }

        public void setRemarks(String remarks) {
            mRemarks = remarks;
        }

        public String getStatus() {
            return mStatus;
        }

        public void setStatus(String status) {
            mStatus = status;
        }

    }

    public class TodayData {

        @SerializedName("date")
        private String mDate;
        @SerializedName("employee")
        private String mEmployee;
        @SerializedName("remarks")
        private String mRemarks;
        @SerializedName("status")
        private String mStatus;

        public String getDate() {
            return mDate;
        }

        public void setDate(String date) {
            mDate = date;
        }

        public String getEmployee() {
            return mEmployee;
        }

        public void setEmployee(String employee) {
            mEmployee = employee;
        }

        public String getRemarks() {
            return mRemarks;
        }

        public void setRemarks(String remarks) {
            mRemarks = remarks;
        }

        public String getStatus() {
            return mStatus;
        }

        public void setStatus(String status) {
            mStatus = status;
        }

    }



}
