package com.asiainfo.km.service;

import com.asiainfo.km.domain.BugKindInfo;

/**
 * Created by jiyuze on 2017/8/7.
 */
public interface BugKindService {
    BugKindInfo getRootKind();
    void delete(Long kinId);
    BugKindInfo save(BugKindInfo kindInfo);
    BugKindInfo findById(Long kindId);
}
