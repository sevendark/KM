package com.asiainfo.km.service.impl;

import com.asiainfo.km.repository.UserRepo;
import com.asiainfo.km.domain.UserInfo;
import com.asiainfo.km.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserSvcRepoImp implements UserService{
    private final UserRepo userRepo;

    public UserSvcRepoImp(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo user;
        user = userRepo.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("没有找到用户");
        } else {
            return user;
        }
    }


    @Override
    public UserInfo saveUser(UserInfo userInfo) {
        return userRepo.save(userInfo);
    }
}
