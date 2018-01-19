package com.asiainfo.km.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Created by jiyuze on 2017/8/1.
 */
@Controller
public class IndexController {

    @GetMapping("/")
    public String index_get(){
        return "index";
    }

    @PostMapping("/")
    public String index_post(){
        return "redirect:index";
    }

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("login")
    public String login(){
        return "login";
    }

}
