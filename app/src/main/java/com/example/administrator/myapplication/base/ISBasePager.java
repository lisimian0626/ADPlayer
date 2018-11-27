package com.example.administrator.myapplication.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * author: Hanson
 * date:   2018/1/17
 * describe:
 */

public class ISBasePager<T> implements Serializable {
    /**
     * current_page : 1
     * data : [{"_id":"5a556181006f55ac84738bf7","name":"我的方案一","user_id":220}]
     * from : 1
     * last_page : 1
     * next_page_url : null
     * path : https://www.ad4.com/v3.6/ad/web/user/220/targeting
     * per_page : 10
     * prev_page_url : null
     * to : 1
     * total : 1
     */

    @SerializedName("current_page")
    private int mCurrentPage;
    @Expose
    @SerializedName("from")
    private int mFrom;
    @SerializedName("last_page")
    private int mLastPage;
    @SerializedName("path")
    private String mPath;
    @SerializedName("per_page")
    private int mPerPage;
    @SerializedName("to")
    @Expose
    private int mTo;
    @SerializedName("total")
    private int mTotal;
    @SerializedName("data")
    private List<T> mData;

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public void setCurrentPage(int currentPage) {
        mCurrentPage = currentPage;
    }

    public int getFrom() {
        return mFrom;
    }

    public void setFrom(int from) {
        mFrom = from;
    }

    public int getLastPage() {
        return mLastPage;
    }

    public void setLastPage(int lastPage) {
        mLastPage = lastPage;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public int getPerPage() {
        return mPerPage;
    }

    public void setPerPage(int perPage) {
        mPerPage = perPage;
    }

    public int getTo() {
        return mTo;
    }

    public void setTo(int to) {
        mTo = to;
    }

    public int getTotal() {
        return mTotal;
    }

    public void setTotal(int total) {
        mTotal = total;
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> data) {
        mData = data;
    }
}
