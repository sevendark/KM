package com.asiainfo.km.pojo;

public class KmResult<T> {
    private final T data;
    private final boolean isSuccess;

    public KmResult(T data, boolean isSuccess) {
        this.data = data;
        this.isSuccess = isSuccess;
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

}
