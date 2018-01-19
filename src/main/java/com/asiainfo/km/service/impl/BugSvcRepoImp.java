package com.asiainfo.km.service.impl;

import com.asiainfo.km.repository.BugRepo;
import com.asiainfo.km.repository.LogRepo;
import com.asiainfo.km.domain.BugInfo;
import com.asiainfo.km.domain.LogInfo;
import com.asiainfo.km.pojo.TableParam;
import com.asiainfo.km.pojo.TableResult;
import com.asiainfo.km.service.BugService;
import com.asiainfo.km.util.ParamUtil;
import com.asiainfo.km.util.TableResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.util.Map;

/**
 * Created by jiyuze on 2017/8/7.
 */
@Service
@Transactional
public class BugSvcRepoImp implements BugService {
    private final BugRepo bugRepo;
    private final LogRepo logRepo;

    @Autowired
    public BugSvcRepoImp(BugRepo bugRepo, LogRepo logRepo) {
        this.bugRepo = bugRepo;
        this.logRepo = logRepo;
    }

    public TableResult<BugInfo> getListForIndex(TableParam param){
        Specification<BugInfo> specification = (Root<BugInfo> root, CriteriaQuery<?> q, CriteriaBuilder cb)->{
            Path<Long> bugId = root.get("bugId");
            Path<String> bugTntro = root.get("bugTntro");
            Path<String> bugName = root.get("bugName");
            Path<String> createUser = root.get("createUser");
            Path<Timestamp> createTime = root.get("createTime");
            Path<Long> kindId = root.get("bugKind").get("kindId");
            Predicate predicate = cb.disjunction();
            Map<TableParam.Search, String> search = param.getSearch();
            Map<String, Object> other = param.getOther();
            predicate = cb.or(predicate,cb.like(bugTntro, "%" + search.get(TableParam.Search.value)+ "%"));
            try {
                predicate = cb.or(predicate,cb.equal(bugId,Long.valueOf(search.get(TableParam.Search.value))));
            }catch (Exception ignored){}
            predicate = cb.or(predicate,cb.like(bugName, "%" + search.get(TableParam.Search.value)+ "%"));
            predicate = cb.or(predicate,cb.like(createUser, "%" + search.get(TableParam.Search.value)+ "%"));
            predicate = cb.and(predicate,cb.greaterThanOrEqualTo(createTime,param.getStartTime()));
            predicate = cb.and(predicate,cb.lessThanOrEqualTo(createTime,param.getEndTime()));
            try{
                predicate = cb.and(predicate,cb.equal(kindId,Long.valueOf((String) other.get("kindId"))));
            }catch (Exception ignored){}
            return predicate;
        };
        Page<BugInfo> result = bugRepo.findAll(specification, ParamUtil.param2Page(param));
        return TableResultUtil.page2Result(result,param.getDraw(), bugRepo.count());
    }

    public BugInfo save(BugInfo bug, String userName){
        bug.setCreateTime(new Timestamp(System.currentTimeMillis()));
        bug.setBugTimes(0);
        bug.setSearchTimes(0);
        bug.setCreateUser(userName);
        return bugRepo.save(bug);
    }

    public BugInfo getBugInfo(Long bugId){
        BugInfo bug = bugRepo.findOne(bugId);
        bug.setSearchTimes(bug.getSearchTimes() + 1);
        bugRepo.save(bug);
        return bug;
    }

    public Integer upTimes(Long bugId, String user, String context){
        LogInfo log = new LogInfo();
        log.setCreateTime(new Timestamp(System.currentTimeMillis()));
        log.setCreateUser(user);
        log.setLogContext(context);
        log.setValue(1);

        BugInfo bug = bugRepo.findOne(bugId);
        bug.setBugTimes(bug.getBugTimes() + 1);
        bug.getLogList().add(logRepo.save(log));
        bugRepo.save(bug);
        return bug.getBugTimes();
    }

    public Integer subTimes(Long bugId, String user, String context){
        LogInfo log = new LogInfo();
        log.setCreateTime(new Timestamp(System.currentTimeMillis()));
        log.setCreateUser(user);
        log.setLogContext(context);
        log.setValue(-1);

        BugInfo bug = bugRepo.findOne(bugId);
        if( (bug.getBugTimes() - 1) >= 0){
            bug.setBugTimes(bug.getBugTimes() - 1);
            bug.getLogList().add(logRepo.save(log));
            bugRepo.save(bug);
        }
        return bug.getBugTimes();
    }

    public boolean remove(Long bugId){
        try {
            bugRepo.delete(bugId);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
