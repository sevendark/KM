package com.asiainfo.km.controller;

import com.asiainfo.km.domain.DocInfo;
import com.asiainfo.km.domain.solr.DocSolrInfo;
import com.asiainfo.km.pojo.KmResult;
import com.asiainfo.km.pojo.TableParam;
import com.asiainfo.km.pojo.TableResult;
import com.asiainfo.km.service.DocRepoService;
import com.asiainfo.km.service.DocService;
import com.asiainfo.km.settings.PathSettings;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by jiyuze on 2017/8/7.
 */
@Controller
public class DocController extends BaseController{
    private final PathSettings pathSettings;
    private final DocRepoService docRepoService;
    private final DocService<DocSolrInfo> docSolrService;

    public DocController(PathSettings pathSettings, DocRepoService docRepoService, DocService<DocSolrInfo> docSolrService) {
        this.pathSettings = pathSettings;
        this.docRepoService = docRepoService;
        this.docSolrService = docSolrService;
    }

    @GetMapping("getDoc")
    public String getDoc(Long docId, Model model) {
        KmResult<DocInfo> result = docRepoService.getOneDoc(docId);
        if(result.isSuccess()) {
            model.addAttribute("doc", result.getData());
            model.addAttribute("localRoot", pathSettings.getLocalRoot());
            return "docInfo";
        }else{
            return "redirect:index";
        }
    }

    @GetMapping("indexData")
    @ResponseBody
    public TableResult<DocSolrInfo> indexData(TableParam param){
        return docSolrService.getDocList4Table(param);
    }

    @PostMapping("changeDocTimes")
    @ResponseBody
    public Integer changeDocTimes(Long docId, Boolean flag,String context){
        Integer times;
        if(flag){
            times = docRepoService.upTimes(docId,getUsername(),context);
        }else{
            times = docRepoService.subTimes(docId,getUsername(),context);
        }
        return times;
    }
}
