package com.asiainfo.km.controller;

import com.asiainfo.km.domain.DocInfo;
import com.asiainfo.km.pojo.Folder;
import com.asiainfo.km.pojo.KmErrorCode;
import com.asiainfo.km.pojo.KmException;
import com.asiainfo.km.pojo.KmResult;
import com.asiainfo.km.service.BugService;
import com.asiainfo.km.service.DocRepoService;
import com.asiainfo.km.settings.PathSettings;
import com.asiainfo.km.util.KmExceptionCreater;
import com.asiainfo.km.util.OsFileUtil;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private final PathSettings pathSettings;
    private final DocRepoService docRepoService;
    private final BugService bugService;

    public KmController(PathSettings pathSettings, DocRepoService docRepoService, BugService bugService) {
        this.pathSettings = pathSettings;
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
        File tempFile = OsFileUtil.newFileByOs(pathSettings.getLocalRoot(), fileName$Path);
        try {
            OsFileUtil.uploadFile(tempFile, uploadFile.getInputStream());
        } catch (IOException e) {
            throw KmExceptionCreater.create(KmErrorCode.IO_ERROR);
        }
        String md5 = getMd5(tempFile);
        DocInfo docInfo = docRepoService.getDocByMd5(md5);
        if(docInfo == null){
            docInfo = new DocInfo();
            docInfo.setCreateUser(getUsername());
            docInfo.setDocName(fileName);
            docInfo.setDocMime(uploadFile.getContentType());
            docInfo.setPath(tempFile.getPath());
            if(bugId != null) {
                docInfo.getBugList().add(bugService.getBugInfo(bugId));
            }
            docRepoService.saveDoc(docInfo);
        }else {
            boolean key =tempFile.delete();
            if(!key){
                throw KmExceptionCreater.create(KmErrorCode.IO_ERROR);
            }
            throw KmExceptionCreater.create("文件已存在,路径：" + docInfo.getPath() +  ", 文件名：" + docInfo.getDocName());
        }
        return new HashMap<>();
    }

    @PostMapping("deleteFile")
    public Map<String,Object> deleteFile(Long docId){
        KmResult<DocInfo> result = docRepoService.getOneDoc(docId);
        if(result.isSuccess()){
            DocInfo info = result.getData();
            try {
                Files.deleteIfExists(Paths.get(info.getPath()));
                docRepoService.deleteDoc(info.getDocId());
            } catch (IOException e) {
                logger.error("delete File error:", e);
            }
        }

        return new HashMap<>();
    }

    @GetMapping("getFolderList")
    public Folder getFolderList() throws Exception {
        Folder folder;
        folder = docRepoService.getFolderList();
        return folder;
    }

    @PostMapping("addFolder")
    public Map<String,Object> addFolder(String path, String folderName) {
        Map<String,Object> result = new HashMap<>();
        boolean key = false;
        String path$folderName = OsFileUtil.makeUp(path,folderName);
        Path newFolder = Paths.get(path$folderName);
        try {
            Files.createDirectory(newFolder);
            key = true;
        } catch (IOException e) {
            logger.error("add folder error:", e);
        }
        result.put("path",path);
        result.put("isSuccess",key);
        return result;
    }

    @PostMapping("deleteFolder")
    public Boolean deleteFolder(String path) {
        Path path_d = Paths.get(path);
        boolean key = false;
        try {
            Files.delete(path_d);
            key = true;
        } catch (IOException e) {
            logger.error("delete folder error", e);
        }
        return key;
    }

    @PostMapping("renameFolder")
    public Map<String, Object> renameFolder(String path, String newName) {
        Path oldFolder = Paths.get(path);
        Path newFolder = oldFolder.getParent().resolve(newName);
        boolean key = false;
        try {
            Files.move(oldFolder, newFolder);
            key = true;
        } catch (IOException e) {
            logger.error("rename folder", e);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("key", key);
        result.put("path", newFolder.toString());
        return result;
    }

    @PostMapping("syncSVN")
    public Map<String, Object> syncSVN() throws KmException {
        Map<String, Object> map = new HashMap<>();
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
            File file = new File(info.getPath());
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
            File file = new File(info.getPath());
            FileInputStream ini = null; // 以byte流的方式打开文件
            OutputStream outi = null;
            try {
                ini = new FileInputStream(file);
                int i=ini.available(); //得到文件大小
                byte[] data = new byte[i];
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
