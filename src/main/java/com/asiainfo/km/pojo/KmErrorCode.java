package com.asiainfo.km.pojo;

public enum KmErrorCode {
    IO_ERROR("读写错误"),
    OTHER("其它错误"),

    SOLR_SERVER_ERROR("SOLR服务端错误");

    KmErrorCode(String msg) {
        this.msg = msg;
    }

    private final String msg;

    public String getMsg() {
        return msg;
    }
}
