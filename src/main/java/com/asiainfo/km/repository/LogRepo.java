package com.asiainfo.km.repository;

import com.asiainfo.km.domain.LogInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jiyuze on 2017/8/3.
 */
public interface LogRepo extends JpaRepository<LogInfo,Long>{
}
