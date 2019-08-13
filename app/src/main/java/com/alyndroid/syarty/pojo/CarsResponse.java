package com.alyndroid.syarty.pojo;

import java.util.List;

public class CarsResponse {

    private List<LoginResponse.CarInfo> data;
    private String status;
    private String error;

    public List<LoginResponse.CarInfo> getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }
}

