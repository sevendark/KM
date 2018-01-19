package com.asiainfo.km.repository;

import com.asiainfo.km.domain.DocInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by jiyuze on 2017/8/3.
 */
public interface DocRepo extends JpaRepository<DocInfo,Long>, JpaSpecificationExecutor<DocInfo> {
    List<DocInfo> findByMsgDigest(String md5);
}
