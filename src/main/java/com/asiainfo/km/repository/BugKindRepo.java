package com.asiainfo.km.repository;

import com.asiainfo.km.domain.BugKindInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jiyuze on 2017/8/3.
 */
public interface BugKindRepo extends JpaRepository<BugKindInfo,Long>{
    BugKindInfo findByFatherIsNull();
    BugKindInfo findByKindId(Long kindId);
}
