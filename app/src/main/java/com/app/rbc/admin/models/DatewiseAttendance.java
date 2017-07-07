
package com.app.rbc.admin.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DatewiseAttendance {

    @SerializedName("meta")
    private Meta mMeta;
    @SerializedName("search_result")
    private List<SearchResult> mSearchResult;

    public Meta getMeta() {
        return mMeta;
    }

    public void setMeta(Meta meta) {
        mMeta = meta;
    }

    public List<SearchResult> getSearchResult() {
        return mSearchResult;
    }

    public void setSearchResult(List<SearchResult> searchResult) {
        mSearchResult = searchResult;
    }


    public class SearchResult {

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
