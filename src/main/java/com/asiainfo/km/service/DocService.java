package com.asiainfo.km.service;

import com.asiainfo.km.pojo.KmException;
import com.asiainfo.km.pojo.KmResult;
import com.asiainfo.km.pojo.TableParam;
import com.asiainfo.km.pojo.TableResult;

/**
 * Created by jiyuze on 2017/8/3.
 */
public  interface DocService<T> {

    TableResult<T> getDocList4Table(TableParam param);
    KmResult<T> getOneDoc(Long docId);
    T saveDoc(T info) throws KmException;
    void deleteDoc(Long docId);

}
