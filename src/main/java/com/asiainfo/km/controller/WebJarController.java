package com.asiainfo.km.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;
import org.webjars.WebJarAssetLocator;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WebJarController {
    private final WebJarAssetLocator assetLocator = new WebJarAssetLocator();

    @ResponseBody
    @GetMapping("/webJarsLocator/{webJar}/**")
    public ResponseEntity<ClassPathResource> locatorWebJar(@PathVariable String webJar, HttpServletRequest request){
        try {
            String mvcPrefix = "/webJarsLocator/" + webJar + "/";
            String mvcPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
            String fullPath = assetLocator.getFullPath(webJar, mvcPath.substring(mvcPrefix.length()));
            return new ResponseEntity<>(new ClassPathResource(fullPath), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
