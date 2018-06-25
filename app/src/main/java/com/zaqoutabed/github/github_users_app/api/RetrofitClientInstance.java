package com.zaqoutabed.github.github_users_app.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static final String BASE_URL = "https://api.github.com";
    private static Retrofit retrofitClientInstance;

    public static ApiInterface getRetrofitClientInstance() {
        if (retrofitClientInstance == null)
            retrofitClientInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        return retrofitClientInstance.create(ApiInterface.class);
    }
}
