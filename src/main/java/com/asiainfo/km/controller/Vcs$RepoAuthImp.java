package com.asiainfo.km.controller;

import com.asiainfo.km.domain.UserInfo;
import com.asiainfo.km.service.impl.UserSvcRepoImp;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class Vcs$RepoAuthImp implements AuthenticationProvider {
    private final UserSvcRepoImp userService;

    public Vcs$RepoAuthImp(UserSvcRepoImp userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        UserDetails user;
        try{
            user =  userService.loadUserByUsername(username);
            if (!password.equals(user.getPassword())) {
                throw new BadCredentialsException("密码错误");
            }
        }catch (UsernameNotFoundException ignored){
            UserInfo newUser =new UserInfo();
            newUser.setUsername(username);
            newUser.setPassword(password);
            user = userService.saveUser(newUser);
        }

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        return new UsernamePasswordAuthenticationToken(user, password, authorities);
    }

    @Override
    public boolean supports(Class<?> arg0) {
        return true;
    }

}
