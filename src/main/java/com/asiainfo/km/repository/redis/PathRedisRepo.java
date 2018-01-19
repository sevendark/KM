package com.asiainfo.km.repository.redis;


import com.asiainfo.km.domain.redis.PathRedisInfo;
import org.springframework.data.repository.CrudRepository;

public interface PathRedisRepo extends CrudRepository<PathRedisInfo, String> {
}
