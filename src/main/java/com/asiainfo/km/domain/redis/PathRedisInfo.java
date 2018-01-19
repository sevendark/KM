package com.asiainfo.km.domain.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.ArrayList;
import java.util.List;

@RedisHash
public class PathRedisInfo {
    @Id
    private String key;
    private String value;

    public PathRedisInfo(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public PathRedisInfo() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
