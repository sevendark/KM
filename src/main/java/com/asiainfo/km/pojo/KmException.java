package com.asiainfo.km.pojo;

public class KmException extends Exception {
    private final KmErrorCode errorCode;

    public KmException(KmErrorCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
    }
    public KmException(String msg) {
        super(msg);
        this.errorCode = KmErrorCode.OTHER;
    }


    public KmErrorCode getErrorCode() {
        return errorCode;
    }

}
