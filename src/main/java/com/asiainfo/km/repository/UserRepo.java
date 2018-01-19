package com.asiainfo.km.repository;

import com.asiainfo.km.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jiyuze on 2017/8/3.
 */
public interface UserRepo extends JpaRepository<UserInfo,Long>{
    UserInfo findByUsername(String username);
}
