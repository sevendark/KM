package com.asiainfo.km.service;

import com.asiainfo.km.domain.DocInfo;
import com.asiainfo.km.pojo.Folder;
import com.asiainfo.km.pojo.KmException;

import java.io.File;
import java.util.List;

public interface DocRepoService extends DocService<DocInfo> {

    void addSearchTimes(Long docId);

    void addDocBug(Long docId, Long bugId);

    void removeDocBug(Long docId, Long bugId);

    Integer upTimes(Long docId, String user, String context);

    Integer subTimes(Long docId, String user, String context);

    List<String> readPath() throws KmException;

    void readPath(File path, List<String> files) throws KmException;

    Folder getFolderList() throws KmException;

    void getFolderList(File path,Folder me) throws KmException;

    DocInfo getDocByMd5(String md5);
}
