package com.github.idobashari.rating_sdk.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://sdk-rating-system-qakj1p6ow-idos-projects-3be30449.vercel.app/";
    private static ApiService apiService;
    private static String authToken;

    private ApiClient() {
        // Private constructor to prevent instantiation
    }

    public static ApiService getApiService() {
        if (apiService == null) {
            createApiService();
        }
        return apiService;
    }

    public static void setAuthToken(String token) {
        authToken = token;
        createApiService(); //
    }

    private static void createApiService() {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder builder = original.newBuilder();


                    if (authToken != null) {
                        builder.header("Authorization", "Bearer " + authToken);
                    }

                    builder.method(original.method(), original.body());
                    return chain.proceed(builder.build());
                })
                .build();

        // Gson configuration
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();

        // Retrofit builder
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = retrofit.create(ApiService.class);
    }
}