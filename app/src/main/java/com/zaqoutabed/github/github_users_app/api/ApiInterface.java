package com.zaqoutabed.github.github_users_app.api;

import com.zaqoutabed.github.github_users_app.model.Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
        @GET("/search/users")
        Call<Response> getUsers(@Query("q") String q, @Query("per_page") int perPage, @Query("page") int page);
}
