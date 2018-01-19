package com.asiainfo.km.repository;

import com.asiainfo.km.domain.BugInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by jiyuze on 2017/8/3.
 */
public interface BugRepo extends JpaRepository<BugInfo,Long>, JpaSpecificationExecutor<BugInfo> {
}
