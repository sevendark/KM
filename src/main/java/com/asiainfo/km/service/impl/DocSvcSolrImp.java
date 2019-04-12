package com.asiainfo.km.service.impl;

import com.asiainfo.km.domain.solr.DocSolrInfo;
import com.asiainfo.km.pojo.*;
import com.asiainfo.km.repository.solr.DocSolrRepo;
import com.asiainfo.km.service.DocService;
import com.asiainfo.km.service.PathService;
import com.asiainfo.km.util.KmExceptionCreater;
import com.asiainfo.km.util.KmResultCreater;
import com.asiainfo.km.util.ParamUtil;
import com.asiainfo.km.util.TableResultUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class DocSvcSolrImp implements DocService<DocSolrInfo> {
    private final SolrClient solrClient;
    private final DocSolrRepo docSolrRepo;
    private final PathService pathService;

    public DocSvcSolrImp(SolrClient solrClient, DocSolrRepo docSolrRepo, PathService pathService) {
        this.solrClient = solrClient;
        this.docSolrRepo = docSolrRepo;
        this.pathService = pathService;
    }

    @Override
    public TableResult<DocSolrInfo> getDocList4Table(TableParam param) {
        String searchValue = param.getSearch().get(TableParam.Search.value);
        Page<DocSolrInfo> result;
        Long num = docSolrRepo.count();
        if(StringUtils.isBlank(searchValue)){
            result = docSolrRepo.findAll(ParamUtil.param2Page(param));
        }else{
            result = docSolrRepo.findByDocNameOrDocIntro(searchValue,searchValue, ParamUtil.param2Page(param));
        }
        return TableResultUtil.page2Result(result,param.getDraw(),num);
    }

    @Override
    public DocSolrInfo saveDoc(DocSolrInfo info) throws KmException {
        ContentStreamUpdateRequest up = new ContentStreamUpdateRequest("/update/extract");
        try {
            up.addFile(new File(pathService.getPath(info.getDocId())),info.getDocMime());
        } catch (IOException e) {
            throw KmExceptionCreater.create(KmErrorCode.IO_ERROR);
        }
        up.setAction(AbstractUpdateRequest.ACTION.COMMIT,true,true);
        up.setParam("literal.docId", info.getDocId().toString());
        up.setParam("literal.docName", info.getDocName());
        up.setParam("literal.createUser", info.getCreateUser());
        String timestamp = String.valueOf(info.getCreateTime());
        timestamp = timestamp.substring(0,timestamp.length()-3);
        up.setParam("literal.createTime", timestamp);
        try {
            solrClient.request(up,"km_doc");
        } catch (SolrServerException e) {
            throw KmExceptionCreater.create(KmErrorCode.SOLR_SERVER_ERROR);
        } catch (IOException e) {
            throw KmExceptionCreater.create(KmErrorCode.IO_ERROR);
        }
        return info;
    }

    @Override
    public KmResult getOneDoc(Long docId) {
        return KmResultCreater.createSuccess(docSolrRepo.findByDocId(docId));
    }

    @Override
    public void deleteDoc(Long docId){
        docSolrRepo.delete(docId);
    }

}
