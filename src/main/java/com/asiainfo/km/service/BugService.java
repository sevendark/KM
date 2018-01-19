package com.asiainfo.km.service;

import com.asiainfo.km.domain.BugInfo;
import com.asiainfo.km.pojo.TableParam;
import com.asiainfo.km.pojo.TableResult;

/**
 * Created by jiyuze on 2017/8/7.
 */
public interface BugService {

    TableResult<BugInfo> getListForIndex(TableParam param);

    BugInfo save(BugInfo bug, String userName);

    BugInfo getBugInfo(Long bugId);

    Integer upTimes(Long bugId, String user, String context);

    Integer subTimes(Long bugId, String user, String context);

    boolean remove(Long bugId);

}
