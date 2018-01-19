package com.asiainfo.km.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiyuze on 2017/8/8.
 */
@Entity
public class BugKindInfo implements Serializable {
    @Id
    @SequenceGenerator(name = "KIND_ID",sequenceName = "KIND_ID_SEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "KIND_ID")
    private Long kindId;
    private String kindName;
    private Long father;
    @OneToMany(mappedBy = "father",cascade = CascadeType.REFRESH , fetch = FetchType.EAGER)
    private List<BugKindInfo> children = new ArrayList<>();
    private Timestamp createTime;
    private Boolean delFlag;

    public Long getKindId() {
        return kindId;
    }

    public void setKindId(Long kindId) {
        this.kindId = kindId;
    }

    public String getKindName() {
        return kindName;
    }

    public void setKindName(String kindName) {
        this.kindName = kindName;
    }

    public Long getFather() {
        return father;
    }

    public void setFather(Long father) {
        this.father = father;
    }

    public List<BugKindInfo> getChildren() {
        return children;
    }

    public void setChildren(List<BugKindInfo> children) {
        this.children = children;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Boolean getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }
}
