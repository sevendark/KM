package com.asiainfo.km.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class BaseController {

    void putMsg(String msg ){
        getSession().setAttribute("msg",msg);
    }

    String getUsername(){
        return getUserDetails().getUsername();
    }

    String getPassword(){
        return getUserDetails().getPassword();
    }

    HttpSession getSession(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getSession();
    }

    UserDetails getUserDetails(){
        return (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
