package com.asiainfo.km.controller;

import com.asiainfo.km.domain.BugKindInfo;
import com.asiainfo.km.service.BugKindService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiyuze on 2017/8/8.
 */
@RestController
@RequestMapping("bug")
public class BugKindController extends BaseController{
    private final BugKindService bugKindService;

    public BugKindController(BugKindService bugKindService) {
        this.bugKindService = bugKindService;
    }

    @PostMapping("addKind")
    public Long addKind(BugKindInfo newKind){
        BugKindInfo old = bugKindService.findById(newKind.getKindId());
        if(old!=null){
            old.setKindName(newKind.getKindName());
            return bugKindService.save(old).getKindId();
        }
        newKind.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return bugKindService.save(newKind).getKindId();
    }

    @PostMapping("deleteKind")
    public Boolean deleteKind(Long kindId){
        if(kindId!=null){
            try {
                bugKindService.delete(kindId);
                return true;
            }catch (Exception e){
                return false;
            }
        }else{
            return false;
        }
    }

    @GetMapping("getRootKind")
    public BugKindInfo getRootKind(){
        return bugKindService.getRootKind();
    }

}
