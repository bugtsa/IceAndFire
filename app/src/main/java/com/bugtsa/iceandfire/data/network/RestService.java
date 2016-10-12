package com.bugtsa.iceandfire.data.network;

import com.bugtsa.iceandfire.data.network.res.CharacterRes;
import com.bugtsa.iceandfire.data.network.res.HouseRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestService {

    @GET("houses/{house}")
    Call<HouseRes> getHouse(@Path("house") String house);

    @GET("characters/{character}")
    Call<CharacterRes> getCharacter(@Path("character") int character);
}
