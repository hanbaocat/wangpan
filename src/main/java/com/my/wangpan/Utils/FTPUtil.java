package com.my.wangpan.Utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import javax.sound.sampled.Port;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * FTP工具类
 */
@Slf4j
public class FTPUtil {
    /**
     * FTP服务器
     */
    private static String HOST = "";
    /**
     * 端口
     */
    private static int PORT = 21;
    /**
     * 登录账号
     */
    private static String USERNAME = "";
    /**
     * 登录密码
     */
    private static String PASSWORD = "";
    /**
     * FTP基础目录，这个是搭建的时候自己设置的
     */
    private static String BASEPATH = "/";
    /**
     * 客户端
     */
    private static FTPClient ftp;

    /**
     * 初始化ftp客户端
     * @return 注册成功返回true，失败返回false
     */
    public static boolean initFtpClient(){
        ftp = new FTPClient();
        int reply;
        try{
            //连接ftp服务器，默认端口21，如果采用默认端口，可以不设置port
            ftp.connect(HOST, PORT);
            //登录
            ftp.login(USERNAME,PASSWORD);
            //设置传输大小
            ftp.setBufferSize(10240);
            //设置传输超时时间
            ftp.setDataTimeout(60*1000);
            //FTP以二进制形式传输
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            reply = ftp.getReplyCode();
            if(!FTPReply.isPositiveCompletion(reply)){
                ftp.disconnect();
                log.info("FTP服务器登录失败！");
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        log.info("FTP服务器登陆成功！");
        return true;
    }

    /**
     * 文件上传指定目录
     * @param filePath 文件路径，按时间存储 比如/1/16
     * @param filename 文件名
     * @param input 文件的二进制流
     * @return 成功返回true，失败返回false
     */
    public static boolean uploadFile(String filePath, String filename, InputStream input){
        boolean result = false;
        try{
            //解决乱码问题
            filename = new String(filename.getBytes("GBK"), FTP.DEFAULT_CONTROL_ENCODING);
            filePath = new String(filePath.getBytes("GBK"),FTP.DEFAULT_CONTROL_ENCODING);
            //初始化ftp
            if(!initFtpClient()){
                log.info("ftp客户端初始化失败！");
                return result;
            }
            //传输数据之前的工作，告诉服务器打开一个端口接收数据
            ftp.enterLocalPassiveMode();
            //切换到工作目录
            if(!ftp.changeWorkingDirectory(BASEPATH+filePath)){
                //如果目录不存在，就要一层一层创建
                String[] dirs = filePath.split("/");
                String tempPath = BASEPATH;
                //创建文件夹
                for(String dir : dirs){
                    //不创建空文件夹
                    if(dir == null || "".equals(dir)){
                        continue;
                    }
                    tempPath += "/" + dir;
                    if(!ftp.changeWorkingDirectory(tempPath)){
                        //如果当前目录不存在，就创建
                        //创建失败返回false
                        if(!ftp.makeDirectory(tempPath)){
                            return result;
                        }else{
                            //创建成功，切换工作目录到当前目录
                            ftp.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }
            //创建完毕或者已经存在目录
            //上传文件
            ftp.enterLocalPassiveMode();
            if(!ftp.storeFile(filename,input)){
                return result;
            }
            input.close();
            ftp.logout();
            result = true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return true;
    }

    /**
     * 下载文件
     * @param remotePath  FTP服务器上的相对路径
     * @param fileName 要下载的文件名
     * @param localPath
     * @return
     */
    public static boolean downloadFile(String remotePath,String fileName,String localPath){
        boolean result = false;
        try{
            remotePath = new String(remotePath.getBytes("GBK"),"iso-8859-1");
            fileName = new String(fileName.getBytes("GBK"),"iso-8859-1");
            if (!initFtpClient()){
                return result;
            }
            //转移到服务器目录
            ftp.changeWorkingDirectory(remotePath);
            ftp.enterLocalPassiveMode();
            FTPFile[] fs = ftp.listFiles();
            for(FTPFile ff : fs){
                //匹配文件名
                if(ff.getName().equals(fileName)){
                    //把文件下载下来
                    ftp.enterLocalPassiveMode();
                    FileOutputStream outputStream = new FileOutputStream(localPath);
                    ftp.retrieveFile(remotePath+"/"+localPath,outputStream);

                    result = true;
                    outputStream.close();
                }
            }
            ftp.logout();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }

    /**
     * 下载文件到指定流
     * @param remotePath
     * @param fileName
     * @param outputStream
     * @return
     */
    public static boolean downloadFile(String remotePath, String fileName, OutputStream outputStream){
        boolean result = false;
        try{
            remotePath = new String(remotePath.getBytes("GBK"),FTP.DEFAULT_CONTROL_ENCODING);
            fileName = new String(fileName.getBytes("GBK"),FTP.DEFAULT_CONTROL_ENCODING);
            if (!initFtpClient()){
                return result;
            }
            //切换到FTP服务器
            ftp.enterLocalPassiveMode();
            ftp.changeWorkingDirectory(remotePath);

            FTPFile[] fs = ftp.listFiles();
            for(FTPFile ff : fs){
                if(ff.getName().equals(fileName)){
                    //下载文件
                    ftp.enterLocalPassiveMode();
                    ftp.retrieveFile(remotePath+"/"+fileName,outputStream);
                    result = true;
                }
            }
            ftp.logout();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }

    /**
     * 删除文件
     * @param remotePath
     * @param fileName
     * @return
     */
    public static boolean deleteFile(String remotePath, String fileName){
        boolean result = false;
        try{
            if("".equals(fileName))return result;

            remotePath = new String(remotePath.getBytes("GBK"),FTP.DEFAULT_CONTROL_ENCODING);
            fileName = new String(fileName.getBytes("GBK"),FTP.DEFAULT_CONTROL_ENCODING);
            if (!initFtpClient()){
                return result;
            }
            //切换到FTP服务器
            ftp.enterLocalPassiveMode();
            ftp.changeWorkingDirectory(remotePath);

            FTPFile[] fs = ftp.listFiles();
            for(FTPFile ff : fs){
                if(ff.getName().equals(fileName)){
                    //删除文件
                    ftp.enterLocalPassiveMode();
                    ftp.deleteFile(fileName); //
                    result = true;
                }
            }
            ftp.logout();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }

    /**
     * 删除文件夹
     * @param remotePath
     * @return
     */
    public static boolean deleteFolder( String remotePath){
        boolean result = false;
        try{
            remotePath = new String(remotePath.getBytes("GBK"),FTP.DEFAULT_CONTROL_ENCODING);
            if (!initFtpClient()){
                return result;
            }
            //切换到FTP服务器
            ftp.enterLocalPassiveMode();
            ftp.changeWorkingDirectory(remotePath);

            FTPFile[] fFs = ftp.listDirectories();
            //直接根据路径删除，不需要检查名字
            if(fFs.length!=0){
                //删除
                ftp.removeDirectory(remotePath);
                result = true;
            }
            ftp.logout();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }

    /**
     * 重命名文件夹或者文件
     * @param path
     * @param oldAllName
     * @param newAllName
     * @return
     */
    public static boolean renameFile(String path,String oldAllName, String newAllName){
        boolean flag = false;
        try {
            oldAllName = new String(oldAllName.getBytes("GBK"),"iso-8859-1");
            newAllName = new String(newAllName.getBytes("GBK"),"iso-8859-1");
            if (!initFtpClient()){
                return flag;
            };
            ftp.enterLocalPassiveMode();
            ftp.changeWorkingDirectory(path);
            boolean rename = ftp.rename(oldAllName,newAllName);
            flag = true;
            ftp.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return flag;
    }
}
