package com.asiainfo.km.pojo;

public enum KmErrorCode {
    SVN_URL_ERROR("资源库路径错误"),
    SVN_UPDATE_ERROR("SVN更新失败"),
    SVN_ADD_ERROR("SVN添加失败"),
    SVN_DEL_ERROR("SVN删除失败"),
    SVN_COMMIT_ERROR("SVN提交失败"),
    SVN_REVERT_ERROR("SVN还原失败"),
    SVN_NOT_WORKING_COPY("非SVN本地目录"),
    SVN_MOVE_ERROR("SVN移动文件错误"),

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
