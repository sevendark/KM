package com.asiainfo.km.service.impl;

import com.asiainfo.km.pojo.KmErrorCode;
import com.asiainfo.km.pojo.KmException;
import com.asiainfo.km.service.VcsService;
import com.asiainfo.km.settings.SvnSettings;
import com.asiainfo.km.util.KmExceptionCreater;
import com.asiainfo.km.util.OsFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.*;

import java.io.*;

/**
 * Created by jiyuze on 2017/8/6.
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VcsSvcSvnImp implements VcsService {
    private static final Logger logger = LoggerFactory.getLogger(VcsSvcSvnImp.class);

    private final SvnSettings svnSettings;
    private SVNClientManager clientManager;

    public VcsSvcSvnImp(SvnSettings svnSettings) {
        this.svnSettings = svnSettings;
    }

    private void setupLibrary() {
        DAVRepositoryFactory.setup();
        SVNRepositoryFactoryImpl.setup();
        FSRepositoryFactory.setup();
    }

    public void login(String username, String password) throws KmException {
        setupLibrary();
        SVNRepository repository = null;
        try {
            repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(svnSettings.getSvnRoot()));
        } catch (SVNException e) {
            throw KmExceptionCreater.create(KmErrorCode.SVN_URL_ERROR);
        }
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password.toCharArray());
        repository.setAuthenticationManager(authManager);
        DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);
        clientManager = SVNClientManager.newInstance(options,authManager);
    }

    public void update() throws KmException {
        SVNUpdateClient updateClient = clientManager.getUpdateClient();
        File path = new File(svnSettings.getLocalRoot());
        try {
            updateClient.doUpdate(path,SVNRevision.HEAD,SVNDepth.INFINITY,false,true);
            updateClient.getPathListHandler();
        } catch (SVNException e) {
            throw KmExceptionCreater.create(KmErrorCode.SVN_UPDATE_ERROR);
        }
    }

    public void addFile(File[] files) throws KmException {
        add2SVN(files,false);
    }

    public void addFolder(File[] folders) throws KmException {
        add2SVN(folders, true);
    }

    private void add2SVN(File[] files,boolean mkdir) throws KmException {
        try {
            clientManager.getWCClient().doAdd(files, true,
                    mkdir, false, SVNDepth.INFINITY, false, false, true);
        } catch (SVNException e) {
            throw KmExceptionCreater.create(KmErrorCode.SVN_ADD_ERROR);
        }
    }

    public void deleteFile(File file) throws KmException {
        SVNWCClient wcClient = clientManager.getWCClient();
        try {
            wcClient.doDelete(file,false,false);
        } catch (SVNException e) {
            throw KmExceptionCreater.create(KmErrorCode.SVN_DEL_ERROR);
        }
    }

    public void commit(String commitMessage) throws KmException {
        File wcPath = new File(svnSettings.getLocalRoot());
        try {
            clientManager.getCommitClient().doCommit(
                    new File[]{wcPath}, false, commitMessage, null,
                    null, false, false, SVNDepth.INFINITY);
        } catch (SVNException e) {
            throw KmExceptionCreater.create(KmErrorCode.SVN_COMMIT_ERROR);
        }
    }

    @Override
    public void move(File src, File dst) throws KmException {
        SVNMoveClient moveClient = clientManager.getMoveClient();
        try {
            moveClient.doMove(src,dst);
        } catch (SVNException e) {
            throw KmExceptionCreater.create(KmErrorCode.SVN_MOVE_ERROR);
        }
    }

    public void revert(File file) throws KmException {
        SVNWCClient wcClient = clientManager.getWCClient();
        try {
            wcClient.doRevert(new File[]{file}, SVNDepth.FILES, null);
        } catch (SVNException e) {
            throw KmExceptionCreater.create(KmErrorCode.SVN_REVERT_ERROR);
        }
    }

    public static void isWorkingCopy(File path) throws KmException {
        try {
            if(null == SVNWCUtil.getWorkingCopyRoot(path, false)){
                throw KmExceptionCreater.create(KmErrorCode.SVN_NOT_WORKING_COPY);
            }
        } catch (SVNException e) {
            throw KmExceptionCreater.create(KmErrorCode.SVN_NOT_WORKING_COPY);
        }
    }

}
