package com.asiainfo.km.controller;

import com.asiainfo.km.domain.BugInfo;
import com.asiainfo.km.domain.BugKindInfo;
import com.asiainfo.km.pojo.TableParam;
import com.asiainfo.km.pojo.TableResult;
import com.asiainfo.km.service.BugKindService;
import com.asiainfo.km.service.BugService;
import com.asiainfo.km.service.DocRepoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiyuze on 2017/8/7.
 */
@Controller
@RequestMapping("bug")
public class BugController extends BaseController{
    private final BugService bugService;
    private final BugKindService bugKindService;
    private final DocRepoService docRepoService;

    public BugController(BugService bugService, BugKindService bugKindService, DocRepoService docRepoService) {
        this.bugService = bugService;
        this.bugKindService = bugKindService;
        this.docRepoService = docRepoService;
    }

    @PostMapping("saveBug")
    public String saveBug(BugInfo bug){
        bugService.save(bug, getUsername());
        return "redirect:bugManager";
    }

    @GetMapping("getBug")
    public String getBug(Long bugId, Model model){
        BugInfo bug = bugService.getBugInfo(bugId);
        model.addAttribute("bug", bug);
        return "bug/bugInfo";
    }

    @GetMapping("removeBugDoc")
    public String removeBugDoc(Long docId,Long bugId){
        docRepoService.removeDocBug(docId,bugId);
        return "redirect:getBug?bugId=" + bugId;
    }

    @GetMapping("removeBug")
    @ResponseBody
    public Map removeBug(Long bugId){
        Map<String,Object> map = new HashMap<>();
        Boolean flag = bugService.remove(bugId);
        if(flag){
            map.put("msg", true);
        }else {
            map.put("msg", false);
        }
        return map;
    }

    @GetMapping("bugData")
    @ResponseBody
    public TableResult<BugInfo> bugData(TableParam param){
        return bugService.getListForIndex(param);
    }

    @PostMapping("changeTimes")
    @ResponseBody
    public Integer changeTimes(Long bugId, Boolean flag,String context){
        Integer times;
        if(flag){
            times = bugService.upTimes(bugId,getUsername(),context);
        }else{
            times = bugService.subTimes(bugId,getUsername(),context);
        }
        return times;
    }

    @GetMapping("bugManager")
    public String bugManager(Model model){
        BugKindInfo root = bugKindService.getRootKind();
        model.addAttribute("rootKind",root);
        return "bug/bugManager";
    }

}
