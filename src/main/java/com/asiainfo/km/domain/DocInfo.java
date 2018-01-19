package com.asiainfo.km.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiyuze on 2017/8/2.
 */
@Entity
public class DocInfo implements Serializable {
    @Id
    @SequenceGenerator(name = "DOC_ID",sequenceName = "DOC_ID_SEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "DOC_ID")
    private Long docId;
    private String docName;
    @Lob
    private String docIntro;
    private String docMime;
    private Integer searchTimes;
    private Integer docTimes;
    private String createUser;
    private String docSize;
    private String msgDigest;
    @ManyToMany(cascade = CascadeType.REFRESH)
    @JsonIgnoreProperties("docList")
    private List<BugInfo> bugList = new ArrayList<>();
    @OneToMany
    private List<LogInfo> logList = new ArrayList<>();

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

    public String getMsgDigest() {
        return msgDigest;
    }

    public void setMsgDigest(String msgDigest) {
        this.msgDigest = msgDigest;
    }

    public List<LogInfo> getLogList() {
        return logList;
    }

    public void setLogList(List<LogInfo> logList) {
        this.logList = logList;
    }

    public List<BugInfo> getBugList() {
        return bugList;
    }

    public void setBugList(List<BugInfo> bugList) {
        this.bugList = bugList;
    }

    public String getCreateUser() {
        return createUser;
    }

    public String getDocSize() {
        return docSize;
    }

    public void setDocSize(String docSize) {
        this.docSize = docSize;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocIntro() {
        return docIntro;
    }

    public void setDocIntro(String docIntro) {
        this.docIntro = docIntro;
    }

    public String getDocMime() {
        return docMime;
    }

    public void setDocMime(String docMime) {
        this.docMime = docMime;
    }

    public Integer getSearchTimes() {
        return searchTimes;
    }

    public void setSearchTimes(Integer searchTimes) {
        this.searchTimes = searchTimes;
    }

    public Integer getDocTimes() {
        return docTimes;
    }

    public void setDocTimes(Integer docTimes) {
        this.docTimes = docTimes;
    }
}
