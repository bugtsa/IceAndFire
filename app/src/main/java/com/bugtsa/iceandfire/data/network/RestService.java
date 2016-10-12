package com.bugtsa.iceandfire.data.network;

import com.bugtsa.iceandfire.data.network.res.HouseRes;
import com.bugtsa.iceandfire.data.network.res.LikeModelRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestService {

    @GET("houses/378")
    Call<HouseRes> getHouse();

    @POST("user/{userId}/like")
    Call<LikeModelRes> likeUser(@Path("userId") String userId);
}
