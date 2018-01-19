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
public class BugInfo implements Serializable {
    @Id
    @SequenceGenerator(name = "BUG_ID",sequenceName = "BUG_ID_SEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "BUG_ID")
    private Long bugId;
    private String bugCode;
    private String bugName;
    @Lob
    private String bugTntro;
    private Integer searchTimes;
    private Integer bugTimes;
    private String createUser;
    @ManyToOne
    private BugKindInfo bugKind;
    @JsonIgnoreProperties("bugList")
    @ManyToMany(cascade = CascadeType.REFRESH, mappedBy = "bugList")
    private List<DocInfo> docList = new ArrayList<>();
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

    public List<LogInfo> getLogList() {
        return logList;
    }

    public void setLogList(List<LogInfo> logList) {
        this.logList = logList;
    }

    public List<DocInfo> getDocList() {
        return docList;
    }

    public void setDocList(List<DocInfo> docList) {
        this.docList = docList;
    }

    public BugInfo() {
    }

    public BugInfo(Long bugId) {
        this.bugId = bugId;
    }


    public BugKindInfo getBugKind() {
        return bugKind;
    }

    public void setBugKind(BugKindInfo bugKind) {
        this.bugKind = bugKind;
    }

    public Long getBugId() {
        return bugId;
    }

    public void setBugId(Long bugId) {
        this.bugId = bugId;
    }

    public String getBugCode() {
        return bugCode;
    }

    public void setBugCode(String bugCode) {
        this.bugCode = bugCode;
    }

    public String getBugName() {
        return bugName;
    }

    public void setBugName(String bugName) {
        this.bugName = bugName;
    }

    public String getBugTntro() {
        return bugTntro;
    }

    public void setBugTntro(String bugTntro) {
        this.bugTntro = bugTntro;
    }

    public Integer getSearchTimes() {
        return searchTimes;
    }

    public void setSearchTimes(Integer searchTimes) {
        this.searchTimes = searchTimes;
    }

    public Integer getBugTimes() {
        return bugTimes;
    }

    public void setBugTimes(Integer bugTimes) {
        this.bugTimes = bugTimes;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
}
