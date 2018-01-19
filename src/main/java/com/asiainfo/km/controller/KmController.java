package com.asiainfo.km.controller;

import com.asiainfo.km.domain.DocInfo;
import com.asiainfo.km.domain.redis.PathRedisInfo;
import com.asiainfo.km.pojo.Folder;
import com.asiainfo.km.pojo.KmErrorCode;
import com.asiainfo.km.pojo.KmException;
import com.asiainfo.km.pojo.KmResult;
import com.asiainfo.km.service.BugService;
import com.asiainfo.km.service.DocRepoService;
import com.asiainfo.km.service.PathService;
import com.asiainfo.km.service.VcsService;
import com.asiainfo.km.settings.SvnSettings;
import com.asiainfo.km.util.KmExceptionCreater;
import com.asiainfo.km.util.OsFileUtil;
import com.asiainfo.km.util.SVNLock;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.asiainfo.km.util.OsFileUtil.*;

/**
 * Created by jiyuze on 2017/8/1.
 */
@RestController
public class KmController extends BaseController{
    private static final Logger logger = LoggerFactory.getLogger(KmController.class);

    private final VcsService vcsService;
    private final SvnSettings svnSettings;
    private final PathService pathService;
    private final DocRepoService docRepoService;
    private final BugService bugService;

    public KmController(SvnSettings svnSettings, VcsService vcsService, PathService pathService, DocRepoService docRepoService, BugService bugService) {
        this.svnSettings = svnSettings;
        this.vcsService = vcsService;
        this.pathService = pathService;
        this.docRepoService = docRepoService;
        this.bugService = bugService;
    }

    @PostMapping("uploadFile")
    public Map<String,Object> uploadFile(String path, Long bugId, MultipartFile uploadFile) throws KmException {//TODO bugId 需要处理
        String fileName = uploadFile.getOriginalFilename();
        String fileName$Path;
        if(StringUtils.isNotBlank(path)){
            fileName$Path = makeUp(path, fileName);
        }else{
            fileName$Path = fileName;
        }
        File tempFile = OsFileUtil.newFileByOs(svnSettings.getLocalRoot(), fileName$Path);
        try {
            OsFileUtil.uploadFile(tempFile, uploadFile.getInputStream());
        } catch (IOException e) {
            throw KmExceptionCreater.create(KmErrorCode.IO_ERROR);
        }
        String md5 = getMd5(tempFile);
        DocInfo docInfo = docRepoService.getDocByMd5(md5);
        if(docInfo == null){
            synchronized (SVNLock.LOCK) {
                try {
                    vcsService.login(getUsername(), getPassword());
                    vcsService.addFile(new File[]{tempFile});
                    vcsService.commit("文件上传");
                }catch (KmException e) {
                    //TODO 需要处理SVN异常
                }
            }
            docInfo = new DocInfo();
            docInfo.setCreateUser(getUsername());
            docInfo.setDocName(fileName);
            docInfo.setDocMime(uploadFile.getContentType());
            if(bugId != null) {
                docInfo.getBugList().add(bugService.getBugInfo(bugId));
            }
            docInfo = docRepoService.saveDoc(docInfo);

            PathRedisInfo pathInfo = new PathRedisInfo();
            pathInfo.setValue(docInfo.getDocId().toString());
            pathInfo.setKey(tempFile.getPath());
            pathService.save(pathInfo);

        }else {
            boolean key =tempFile.delete();
            if(!key){
                throw KmExceptionCreater.create(KmErrorCode.IO_ERROR);
            }
            throw KmExceptionCreater.create("文件已存在,路径：" + pathService.getPath(docInfo.getDocId()) +  ", 文件名：" + docInfo.getDocName());
        }
        return new HashMap<>();
    }

    @PostMapping("deleteFile")
    public Map<String,Object> deleteFile(Long docId){
        KmResult<DocInfo> result = docRepoService.getOneDoc(docId);
        if(result.isSuccess()){
            DocInfo info = result.getData();
            try {
                synchronized (SVNLock.LOCK) {
                    vcsService.login(getUsername(), getPassword());
                    File temp = new File(pathService.getPath(info.getDocId()));
                    vcsService.deleteFile(temp);
                    vcsService.commit("删除文件");
                }
                docRepoService.deleteDoc(info.getDocId());
                pathService.delete(new PathRedisInfo(String.valueOf(docId),null));
            } catch (KmException e) {
                //TODO 需要处理SVN异常
            }
        }

        return new HashMap<>();
    }

    @GetMapping("getFolderList")
    public Folder getFolderList() throws Exception {
        Folder folder;
        synchronized (SVNLock.LOCK) {
            vcsService.login(getUsername(),getPassword());
            vcsService.update();
        }
        folder = docRepoService.getFolderList();
        return folder;
    }

    @PostMapping("addFolder")
    public Map<String,Object> addFolder(String path, String folderName) throws KmException {
        Map<String,Object> result = new HashMap<>();
        Boolean key = false;
        String path$folderName = OsFileUtil.makeUp(path,folderName);
        File newFolder = OsFileUtil.newFileByOs(svnSettings.getLocalRoot(),path$folderName);
        if(newFolder.mkdir()){
            key = true;
            synchronized (SVNLock.LOCK) {
                try {
                    vcsService.login(getUsername(), getPassword());
                    vcsService.addFolder(new File[]{newFolder});
                    vcsService.commit("创建文件夹");
                } catch (KmException e) {
                    key = false;
                    //TODO 需要处理SVN异常
                }
                docRepoService.readPath();
            }
        }
        result.put("path",path);
        result.put("isSuccess",key);
        return result;
    }

    @PostMapping("deleteFolder")
    public Boolean deleteFolder(String path) throws KmException {
        File folder = OsFileUtil.newFileByOs(svnSettings.getLocalRoot(),path);
        synchronized (SVNLock.LOCK) {
            try {
                vcsService.login(getUsername(), getPassword());
                vcsService.deleteFile(folder);
                vcsService.commit("删除文件夹");
            } catch (KmException e) {
                //TODO 需要处理SVN异常
            }
            docRepoService.readPath();
        }
        return true;
    }

    @PostMapping("renameFolder")
    public Boolean renameFolder(String oldName,String newName) throws KmException {
        Integer lastIndex = oldName.lastIndexOf(OsFileUtil.getOsSplite());
        if(lastIndex == -1){
            lastIndex = 0;
        }
        newName = oldName.substring(0,lastIndex) + newName;

        File oldFolder = OsFileUtil.newFileByOs(svnSettings.getLocalRoot(),oldName);
        File newFolder = OsFileUtil.newFileByOs(svnSettings.getLocalRoot(),newName);
        synchronized (SVNLock.LOCK) {
            try {
                vcsService.login(getUsername(), getPassword());
                vcsService.move(oldFolder,newFolder);
                vcsService.commit("重命名文件夹，旧名字:" + oldName);
            } catch (KmException e) {
                //TODO 需要处理SVN异常
            }
            docRepoService.readPath();
        }
        return true;
    }

    @PostMapping("syncSVN")
    public Map<String, Object> syncSVN() throws KmException {
        Map<String, Object> map = new HashMap<>();
        synchronized (SVNLock.LOCK) {
            try {
                vcsService.login(getUsername(), getPassword());
                vcsService.update();
            } catch (KmException e) {
                //TODO 需要处理SVN异常
            }
        }
        List<String> files = docRepoService.readPath();
        map.put("files", files);
        return map;
    }

    @ExceptionHandler(Exception.class)
    public Map<String,Object> handleException(Exception ex) {
        Map<String,Object> map = new HashMap<>();
        if (ex instanceof MaxUploadSizeExceededException){
            map.put("error","文件应不大于 "+
                    getFileKB(((MaxUploadSizeExceededException)ex).getMaxUploadSize()));
        } else{
            logger.error("文件上传错误", ex);
            map.put("error","操作失败: " + ex.getMessage());
        }
        return map;

    }

    @GetMapping("download")
    public ResponseEntity<byte[]> download(Long docId) throws KmException {
        KmResult<DocInfo> result = docRepoService.getOneDoc(docId);
        if(result.isSuccess()){
            DocInfo info = result.getData();
            File file = new File(pathService.getPath(info.getDocId()));
            HttpHeaders headers = new HttpHeaders();
            String fileName;//为了解决中文名称乱码问题
            try {
                fileName = new String(info.getDocName().getBytes("UTF-8"),"iso-8859-1");
            } catch (UnsupportedEncodingException e) {
                throw KmExceptionCreater.create(KmErrorCode.OTHER);
            }
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +fileName + "\"");
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            try {
                return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
            } catch (IOException e) {
                throw KmExceptionCreater.create(KmErrorCode.IO_ERROR);
            }
        }
        KmExceptionCreater.create("文件已经不存在");
        return null;
    }

    @GetMapping("getImage")
    public void getImage(Long docId, HttpServletResponse response) throws KmException {
        KmResult<DocInfo> result = docRepoService.getOneDoc(docId);
        if(result.isSuccess()){
            DocInfo info = result.getData();
            File file = new File(pathService.getPath(info.getDocId()));
            FileInputStream ini = null; // 以byte流的方式打开文件
            OutputStream outi = null;
            try {
                ini = new FileInputStream(file);
                int i=ini.available(); //得到文件大小
                byte data[]=new byte[i];
                ini.read(data);  //读数据
                response.setContentType("image/*"); //设置返回的文件类型
                outi = response.getOutputStream(); //得到向客户端输出二进制数据的对象
                outi.write(data);  //输出数据
            } catch (FileNotFoundException e) {
                throw KmExceptionCreater.create(KmErrorCode.OTHER);
            } catch (IOException e) {
                throw KmExceptionCreater.create(KmErrorCode.IO_ERROR);
            }finally {
                try {
                    if(outi!=null){
                        outi.flush();
                        outi.close();
                    }
                    if(ini!=null){
                        ini.close();
                    }
                } catch (IOException e) {
                    throw KmExceptionCreater.create(KmErrorCode.IO_ERROR);
                }
            }
        }
    }
}
