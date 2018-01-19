package com.asiainfo.km.service.impl;

import com.asiainfo.km.repository.BugKindRepo;
import com.asiainfo.km.domain.BugKindInfo;
import com.asiainfo.km.service.BugKindService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
@Transactional
public class BugKindSvcRepoImpl implements BugKindService{
    private final BugKindRepo bugKindRepo;

    public BugKindSvcRepoImpl(BugKindRepo bugKindRepo) {
        this.bugKindRepo = bugKindRepo;
    }

    @Override
    public BugKindInfo getRootKind(){
        Long num = bugKindRepo.count();
        if(num == 0){
            BugKindInfo root = new BugKindInfo();
            root.setKindName("根目录");
            root.setCreateTime(new Timestamp(System.currentTimeMillis()));
            bugKindRepo.save(root);
            return root;
        }else{
            return bugKindRepo.findByFatherIsNull();
        }
    }

    @Override
    public void delete(Long kinId) {
        bugKindRepo.delete(kinId);
    }

    @Override
    public BugKindInfo save(BugKindInfo kindInfo) {
        return bugKindRepo.saveAndFlush(kindInfo);
    }

    @Override
    public BugKindInfo findById(Long kindId) {
        return bugKindRepo.findByKindId(kindId);
    }

}
