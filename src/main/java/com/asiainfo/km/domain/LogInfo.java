package com.asiainfo.km.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by jiyuze on 2017/8/9.
 */
@Entity
public class LogInfo implements Serializable {
    @Id
    @SequenceGenerator(name = "LOG_ID",sequenceName = "LOG_ID_SEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "LOG_ID")
    private Long logId;
    private String logContext;
    private String createUser;
    private Integer value;

    private Timestamp createTime;
    private Boolean delFlag;

    public Boolean getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getLogContext() {
        return logContext;
    }

    public void setLogContext(String logContext) {
        this.logContext = logContext;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

}
