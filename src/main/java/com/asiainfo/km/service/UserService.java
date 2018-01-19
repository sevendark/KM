package com.asiainfo.km.service;

import com.asiainfo.km.domain.UserInfo;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService{
    UserInfo saveUser(UserInfo userInfo);
}
