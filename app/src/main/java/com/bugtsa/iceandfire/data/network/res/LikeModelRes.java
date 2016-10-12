package com.bugtsa.iceandfire.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LikeModelRes {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("data")
    @Expose
    private UserModelRes.ProfileValues data;

    public UserModelRes.ProfileValues getData() {
        return data;
    }
}
