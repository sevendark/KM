package com.asiainfo.km.test;

import com.asiainfo.km.util.OsFileUtil;
import org.junit.Test;
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

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * Created by jiyuze on 2017/8/4.
 */

public class TestSome {
    private String svnRoot = "svn://localhost/asiainfokm";
    private String localRoot = "C:\\\\Users\\\\jiyuze\\\\Desktop\\\\km_repo";
    private SVNRepository repository;
    private SVNClientManager clientManager;
    @Test
    public void testSome() throws SVNException, IOException {
        DAVRepositoryFactory.setup();
        SVNRepositoryFactoryImpl.setup();
        FSRepositoryFactory.setup();
        repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(svnRoot));
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager("jyz", "jyz");
        repository.setAuthenticationManager(authManager);
        DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);
        clientManager = SVNClientManager.newInstance(options,authManager);

        SVNWCClient svnwcClient = clientManager.getWCClient();
        File a = OsFileUtil.newFileByOs(localRoot,"DOC_2017120505000303.java");
        System.out.println(a.getPath());
        System.out.println(a.getName());
        System.out.println(a.getAbsolutePath());
        System.out.println(a.getCanonicalPath());
 /*     File b = OsFileUtil.newFileByOs(localRoot,"2222");
        Boolean key = b.renameTo(a);
        if(key){
            clientManager.getWCClient().doAdd(new File[] { a }, true,
                    true, false, SVNDepth.INFINITY, false, false, true);
        }*/
        svnwcClient.doDelete(a,true,true,false);
        clientManager.getCommitClient().doCommit(new File[]{a},false,"111",false,false);

        SVNUpdateClient updateClient = clientManager.getUpdateClient();
        File path = new File(localRoot);
        updateClient.doUpdate(path, SVNRevision.HEAD,SVNDepth.INFINITY,false,true);

    }

    @Test
    public void testSome2() throws Exception {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        System.out.println(System.currentTimeMillis());
        System.out.println(String.valueOf(ts.getTime()));
        System.out.println(ts.toString());
    }

}
