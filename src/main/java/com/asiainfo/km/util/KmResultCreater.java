package com.asiainfo.km.util;

import com.asiainfo.km.pojo.KmResult;

public class KmResultCreater {
    public static <T> KmResult<T> createSuccess(T data){
        return new KmResult<>(data, true);
    }

    public static <T> KmResult<T> createError(){
        return new KmResult<>(null, false);
    }
}
