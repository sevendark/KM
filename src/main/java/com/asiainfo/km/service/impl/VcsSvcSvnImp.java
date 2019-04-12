package com.asiainfo.km.service.impl;

import com.asiainfo.km.pojo.KmErrorCode;
import com.asiainfo.km.pojo.KmException;
import com.asiainfo.km.service.VcsService;
import com.asiainfo.km.util.KmExceptionCreater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by jiyuze on 2017/8/6.
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VcsSvcSvnImp implements VcsService {
    private static final Logger logger = LoggerFactory.getLogger(VcsSvcSvnImp.class);

    @Override
    public void deleteFile(File file) throws KmException {
        try {
            Files.delete(file.toPath());
        } catch (IOException e) {
            logger.error("delete file", e);
            throw KmExceptionCreater.create(KmErrorCode.IO_ERROR);
        }
    }

    @Override
    public void move(File src, File dst) throws KmException {
        try {
            Files.move(src.toPath(), dst.toPath());
        } catch (IOException e) {
            logger.error("mv file", e);
            throw KmExceptionCreater.create(KmErrorCode.IO_ERROR);
        }
    }

}
