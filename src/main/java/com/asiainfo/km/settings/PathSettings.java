package com.asiainfo.km.settings;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by jiyuze on 2017/11/21.
 */
@Component
@ConfigurationProperties(prefix="path")
public class PathSettings {
    @Value("${path.localRoot}")
    private String localRoot;

    public String getLocalRoot() {
        return localRoot;
    }

    public void setLocalRoot(String localRoot) {
        this.localRoot = localRoot;
    }
}
