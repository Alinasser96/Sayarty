package com.alyndroid.sayarty.data.remote;

import com.alyndroid.sayarty.pojo.AllDriversResponse;
import com.alyndroid.sayarty.pojo.CarsResponse;
import com.alyndroid.sayarty.pojo.SetFireBaseTokenResponse;
import com.alyndroid.sayarty.pojo.StoreResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("all-driver")
    Call<AllDriversResponse> getDrivers();

    @PUT("user-token/{id}")
    Call<SetFireBaseTokenResponse> setFireBaseToken(@Path("id") int user_id,
                                                    @Body HashMap map);

    @POST("delivery-car")
    Call<StoreResponse> sendToReceiver(@Body HashMap map);

    @POST("driver_problem")
    Call<SetFireBaseTokenResponse> sendIssue(@Body HashMap map);

    @PUT("delivery-status/{id}")
    Call<SetFireBaseTokenResponse> setOperationStatus(@Path("id") int operation_id,
                                                      @Body HashMap map);

    @PUT("request_received-status/{id}")
    Call<SetFireBaseTokenResponse> setIsVisible(@Path("id") int operation_id,
                                                @Body HashMap map);

    @GET("car-info/{id}")
    Call<CarsResponse> getCars(@Path("id") int id);
}
