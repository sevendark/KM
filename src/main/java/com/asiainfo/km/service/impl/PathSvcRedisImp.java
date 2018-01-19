package com.asiainfo.km.service.impl;

import com.asiainfo.km.domain.redis.PathRedisInfo;
import com.asiainfo.km.repository.redis.PathRedisRepo;
import com.asiainfo.km.service.PathService;
import com.asiainfo.km.settings.SvnSettings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PathSvcRedisImp implements PathService {
    private final PathRedisRepo pathRedisRepo;

    public PathSvcRedisImp(PathRedisRepo pathRedisRepo) {
        this.pathRedisRepo = pathRedisRepo;
    }

    public void save(PathRedisInfo redisInfo){
        PathRedisInfo other = new PathRedisInfo();
        other.setKey(redisInfo.getValue());
        other.setValue(redisInfo.getKey());
        pathRedisRepo.save(redisInfo);
        pathRedisRepo.save(other);
    }

    @Override
    public String getPath(Long docId) {
        PathRedisInfo path = pathRedisRepo.findOne(String.valueOf(docId));
        if(path != null){
            return path.getValue();
        }
        return null;
    }

    @Override
    public Long getDocId(String path) {
        PathRedisInfo docId = pathRedisRepo.findOne(path);
        if(docId != null){
            return Long.valueOf(docId.getValue());
        }
        return null;
    }

    @Override
    public void delete(String key) {
        String id1 = folderRedis.getKey();
        String id2 = null;
        String path1 = folderRedis.getKey();
        String path2 = null;
        if(id1!=null){
            path2 = getPath(Long.valueOf(id1));
        }
        if(path1!=null){
            id2 = String.valueOf(getDocId(path1));
        }
        del(id1);
        del(id2);
        del(path1);
        del(path2);
    }

    private void del(String key){
        if(key!=null){
            pathRedisRepo.delete(key);
        }
    }
}
