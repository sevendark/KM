package com.asiainfo.km.util;

import com.asiainfo.km.pojo.KmErrorCode;
import com.asiainfo.km.pojo.KmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.awt.OSInfo;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jiyuze on 2017/8/7.
 */
public class OsFileUtil {
    private static final Logger logger = LoggerFactory.getLogger(OsFileUtil.class);

    public static File newFileByOs(String path,String fileName) {
        return new File(makeUp(path,fileName));
    }

    public static String makeUp(String path,String fileName){
        return path + getOsSplite() + fileName;
    }

    public static String getOsSplite(){
        if(OSInfo.OSType.WINDOWS == OSInfo.getOSType()){
            return "\\";
        }else if(OSInfo.OSType.LINUX == OSInfo.getOSType()){
            return "/" ;
        }else{
            throw new RuntimeException("未知系统类型");
        }
    }

    public static void uploadFile(File wcPath, InputStream is) throws KmException {
        OutputStream os = null;
        try {
            os = new FileOutputStream(wcPath);
            byte[] bytes = new byte[1024];
            int length;
            while((length = is.read(bytes)) != -1){
                os.write(bytes, 0, length);
            }
        } catch (FileNotFoundException e) {
            throw KmExceptionCreater.create(KmErrorCode.OTHER);
        } catch (IOException e) {
            throw KmExceptionCreater.create(KmErrorCode.IO_ERROR);
        }finally {
            try {
                if(is!=null){
                    is.close();
                }
                if(os!=null){
                    os.close();
                }
            } catch (IOException e) {
                throw KmExceptionCreater.create(KmErrorCode.IO_ERROR);
            }
        }
    }

    public static String getMd5(File file) throws KmException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
            BigInteger bigInt = new BigInteger(1, md.digest());
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw KmExceptionCreater.create(KmErrorCode.OTHER);
        }  catch (IOException e) {
            throw KmExceptionCreater.create(KmErrorCode.IO_ERROR);
        }finally {
            if(fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    logger.error("流关闭失败",e);
                }
            }
        }
    }

    public static String getFileKB(long byteFile){
        if(byteFile==0)
            return "0KB";
        long kb=1024;
        return ""+byteFile/kb+"KB";
    }

    public static String getFileMB(long byteFile){
        if(byteFile==0)
            return "0MB";
        long mb=1024*1024;
        return ""+byteFile/mb+"MB";
    }

}
