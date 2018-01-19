package com.asiainfo.km.settings;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

import static com.asiainfo.km.service.impl.VcsSvcSvnImp.isWorkingCopy;

/**
 * Created by jiyuze on 2017/11/21.
 */
@Component
@ConfigurationProperties(prefix="svn")
public class SvnSettings {
    @Value("${svn.svnRoot}")
    private String svnRoot;
    @Value("${svn.localRoot}")
    private String localRoot;

    public String getSvnRoot() {
        return svnRoot;
    }

    public void setSvnRoot(String svnRoot) {
        this.svnRoot = svnRoot;
    }

    public String getLocalRoot() {
        return localRoot;
    }

    public void setLocalRoot(String localRoot) throws Exception {
        isWorkingCopy(new File(localRoot));
        this.localRoot = localRoot;
    }
}
