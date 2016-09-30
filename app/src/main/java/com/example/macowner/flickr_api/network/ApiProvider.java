package com.example.macowner.flickr_api.network;

import com.example.macowner.flickr_api.model.GalleryItem;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mikhail on 8/3/16.
 */
public class ApiProvider {

    public static final String API_URL = " https://api.flickr.com/services/rest";
    public static final String EXTRA_SMALL_URL = "url_s";
    private static final String API_KEY = "f55905acfce160a08a05c0d94a1d2961";


    public interface ApiCall {
        
        @GET("/rest/")
        Call<GalleryItem> getRecent(@Query("method") String method,
                                    @Query("api_key") String API_KEY,
                                    @Query("format") String format,
                                    @Query("nojsoncallback") String set,
                                    @Query("extras") String EXTRA_SMALL_URL);

    }

    public static ApiCall create() {
        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiProvider.ApiCall.class);
    }

}
