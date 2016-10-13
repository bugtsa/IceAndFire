package com.bugtsa.iceandfire.data.network;

import com.bugtsa.iceandfire.data.network.res.CharacterRes;
import com.bugtsa.iceandfire.data.network.res.HouseRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestService {

    @GET("houses/{house}")
    Call<HouseRes> getHouse(@Path("house") String house);

    @GET("characters/{character}")
    Call<CharacterRes> getCharacter(@Path("character") String characterId);

    @GET("characters/")
    Call<List<CharacterRes>> getCharacterPage(@Query("page") String currentPage, @Query("pageSize") String perPage);

}
