package com.asiainfo.km.controller;

import com.asiainfo.km.domain.UserInfo;
import com.asiainfo.km.pojo.KmErrorCode;
import com.asiainfo.km.pojo.KmException;
import com.asiainfo.km.service.VcsService;
import com.asiainfo.km.service.impl.UserSvcRepoImp;
import com.asiainfo.km.settings.SvnSettings;
import com.asiainfo.km.util.KmExceptionCreater;
import com.asiainfo.km.util.OsFileUtil;
import com.asiainfo.km.util.SVNLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

@Component
public class Vcs$RepoAuthImp implements AuthenticationProvider {
    private static final Logger logger = LoggerFactory.getLogger(Vcs$RepoAuthImp.class);
    private final UserSvcRepoImp userService;
    private final VcsService vcService;
    private final SvnSettings svnSettings;

    public Vcs$RepoAuthImp(UserSvcRepoImp userService, VcsService vcService, SvnSettings svnSettings) {
        this.userService = userService;
        this.vcService = vcService;
        this.svnSettings = svnSettings;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        UserDetails user;
        try{
            user =  userService.loadUserByUsername(username);
            if (!password.equals(user.getPassword())) {
                throw new BadCredentialsException("密码错误");
            }
        }catch (UsernameNotFoundException ignored){
            String loginFileName = username + ".login";
            File tempFile = OsFileUtil.newFileByOs(svnSettings.getLocalRoot(), loginFileName);
            try {
                OsFileUtil.uploadFile(tempFile,new ByteArrayInputStream(new byte[1]));
            } catch (KmException e) {
                throw new BadCredentialsException("登陆异常: 登陆文件上传失败！");
            }
            synchronized (SVNLock.LOCK){
                try {
                    vcService.login(username, password);
                    vcService.addFile(new File[]{tempFile});
                    vcService.commit(username + "登陆文件+");
                } catch (KmException e) {
                    //TODO 需要处理SVN异常
                    boolean key = tempFile.delete();
                    if(!key){
                        logger.error("请手动删除异常的登陆文件：" + loginFileName);
                    }
                    throw new BadCredentialsException("登陆异常:" + e.getErrorCode().getMsg());
                }

                try {
                    vcService.deleteFile(tempFile);
                    vcService.commit(username + "登陆文件-");
                }catch (KmException e){
                    //TODO 需要处理SVN异常
                    throw new BadCredentialsException("登陆异常:" + e.getErrorCode().getMsg());
                }
            }

            UserInfo newUser =new UserInfo();
            newUser.setUsername(username);
            newUser.setPassword(password);
            user = userService.saveUser(newUser);

        }

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        return new UsernamePasswordAuthenticationToken(user, password, authorities);
    }

    @Override
    public boolean supports(Class<?> arg0) {
        return true;
    }

}
