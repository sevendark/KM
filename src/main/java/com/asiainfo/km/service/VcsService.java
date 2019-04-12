package com.asiainfo.km.service;

import com.asiainfo.km.pojo.KmException;

import java.io.File;

/**
 * Created by jiyuze on 2017/8/6.
 */
public interface VcsService {

    void deleteFile(File file) throws KmException;
    void move(File src, File dst) throws KmException;

}
