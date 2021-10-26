package com.alyndroid.sayarty.pojo;

import java.util.List;

public class AllDriversResponse {
    private List<Driver> data;
    private String status;
    private String error;

    public List<Driver> getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }
}
