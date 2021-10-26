package com.alyndroid.sayarty.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.inject.Inject;


public class CoreUserData {

    @SerializedName("token")
    private String token;

    @SerializedName("id")
    private int userId;


    @SerializedName("password")
    private String password;

    @SerializedName("name")
    private String name;


    @SerializedName("phone")
    private String phone;

    @SerializedName("carInfo")
    private List<LoginResponse.CarInfo> carInfo;
    public CoreUserData() {

    }

    @Inject
    public CoreUserData(String token, int userId, String name, String phone) {
        this.token = token;
        this.userId = userId;
        this.name = name;
        this.phone = phone;
    }


    public List<LoginResponse.CarInfo> getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(List<LoginResponse.CarInfo> carInfo) {
        this.carInfo = carInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
