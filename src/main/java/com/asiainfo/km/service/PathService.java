package com.asiainfo.km.service;

import com.asiainfo.km.domain.redis.PathRedisInfo;

public interface PathService {
    void save(PathRedisInfo folderRedis);
    String getPath(Long docId);
    Long getDocId(String path);
    void delete(PathRedisInfo folderRedis);
}
