package com.asiainfo.km.service;

import com.asiainfo.km.pojo.KmException;

import java.io.*;

/**
 * Created by jiyuze on 2017/8/6.
 */
public interface VcsService {

    void login(String username, String password) throws KmException;
    void update() throws KmException;
    void addFile(File[] files) throws KmException;
    void addFolder(File[] folders) throws KmException;
    void deleteFile(File file) throws KmException;
    void commit(String commitMessage) throws KmException;
    void move(File src, File dst) throws KmException;
    void revert(File file) throws KmException;

}
