package com.asiainfo.km.domain.solr;


import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

/**
 * Created by jiyuze on 2017/8/2.
 */
@SolrDocument(solrCoreName = "km_doc")
public class DocSolrInfo {
    @Id
    @Indexed
    private Long docId;
    @Indexed
    private String docName;
    @Indexed
    private String docIntro;
    private String docMime;
    private String createUser;
    private String docSize;
    @Indexed
    private Long createTime;

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

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getDocSize() {
        return docSize;
    }

    public void setDocSize(String docSize) {
        this.docSize = docSize;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
