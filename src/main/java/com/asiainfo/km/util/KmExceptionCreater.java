package com.asiainfo.km.util;

import com.asiainfo.km.pojo.KmErrorCode;
import com.asiainfo.km.pojo.KmException;

public class KmExceptionCreater {
    public static KmException create(KmErrorCode errorCode){
        return new KmException(errorCode);
    }

    public static KmException create(String msg) {
        return new KmException(msg);
    }
}
