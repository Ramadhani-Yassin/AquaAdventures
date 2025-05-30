package com.jokhakhamisjuma.AquaAdventures.api.clients;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jokhakhamisjuma.AquaAdventures.api.LoggingInterceptor;
import com.jokhakhamisjuma.AquaAdventures.api.RestService;
import com.jokhakhamisjuma.AquaAdventures.api.ToStringConverterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * RetrofitExample
 * https://github.com/Ramadhani-Yassin
 * Created on 05-April-2025.
 */

public class RestClient {

    public static final String BASE_URL = "http://192.168.13.248/aquaadventures/";
    //public static final String BASE_URL = "https://freshmart.frontendsourcecode.com/";


    public static Retrofit RETROFIT = null;
    public static Retrofit RETROFIT1 = null;
    public static RestService restService = null;

    public static Retrofit getClient() {
        if (RETROFIT == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new LoggingInterceptor())
                    .build();
            RETROFIT = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return RETROFIT;
    }

    public static Retrofit getStringClient() {
        if (RETROFIT1 == null) {
            RETROFIT1 = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(new ToStringConverterFactory())
                    .build();
        }
        return RETROFIT1;
    }

    public static RestService getRestService(final Context context) {
        if (restService == null) {
            Gson gson = new GsonBuilder().setLenient().create();
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            builder.readTimeout(30, TimeUnit.SECONDS);
            builder.connectTimeout(30, TimeUnit.SECONDS);
            OkHttpClient client = builder.build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            restService = retrofit.create(RestService.class);
        }
        return restService;
    }
}

