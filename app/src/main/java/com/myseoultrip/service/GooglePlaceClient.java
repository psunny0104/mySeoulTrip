package com.myseoultrip.service;

import android.content.Context;
import android.util.Log;

import com.myseoultrip.model.place.detail.DetailItem;
import com.myseoultrip.model.place.nearby.NearbyPlaceItem;

import org.json.JSONException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GooglePlaceClient {

    private GooglePlaceService googlePlaceService;
    public static String baseUrl = GooglePlaceService.BASE_URL;
    private static Context mContext;
    private static Retrofit retrofit;

    private static class SingletonHolder {
        private static GooglePlaceClient INSTANCE = new GooglePlaceClient(mContext);
    }

    public static GooglePlaceClient getInstance(Context context) {
        if (context != null) {
            mContext = context;
        }
        return SingletonHolder.INSTANCE;
    }

    private GooglePlaceClient(Context context) {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    public GooglePlaceClient createBaseApi() {
        googlePlaceService = create(GooglePlaceService.class);
        return this;
    }

    public  <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }

    public void getNearbyPlace(String key, String type, int radius, String location, String language, final ApiCallback callback){
        googlePlaceService.getSearchNearbyPlace(key,type,radius,location,language).enqueue(new Callback<NearbyPlaceItem>() {
            @Override
            public void onResponse(Call<NearbyPlaceItem> call, Response<NearbyPlaceItem> response) {
                if (response.isSuccessful()) {
                    Log.i("myTag", "responseSuccess"+call.request().url());
                    try {
                        callback.onSuccess(response.code(), response.body());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Log.i("myTag", "responseFailure"+call.request().url());
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<NearbyPlaceItem> call, Throwable t) {
                Log.i("myTag", "responseFailure"+call.request().url());
                callback.onError(t);
            }
        });
    }

    public void getDetailPlace(String key, String placeId, final ApiCallback callback){
        googlePlaceService.getDetailPlace(key,placeId).enqueue(new Callback<DetailItem>() {
            @Override
            public void onResponse(Call<DetailItem> call, Response<DetailItem> response) {
                if (response.isSuccessful()) {
                    Log.i("myTag", "responseSuccess"+call.request().url());
                    try {
                        callback.onSuccess(response.code(), response.body());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Log.i("myTag", "responseFailure"+call.request().url());
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<DetailItem> call, Throwable t) {
                Log.i("myTag", "responseFailure"+call.request().url());
                callback.onError(t);
            }
        });
    }
}
