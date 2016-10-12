package com.bugtsa.iceandfire.data.network.interceptors;

import com.bugtsa.iceandfire.data.managers.DataManager;
import com.bugtsa.iceandfire.data.managers.PreferencesManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        PreferencesManager pm = DataManager.getInstance().getPreferencesManager();

        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder()
                .header("X-Access-Token", pm.getAuthToken())
                .header("Request-House-Id", pm.getUserId())
                .header("House-Agent", "DevIntensiveApp")
                .header("Cache-Control", "max-age="+(60 * 60 * 1));

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
