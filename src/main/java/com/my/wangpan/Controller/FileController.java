package com.my.wangpan.Controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.my.wangpan.Utils.FTPUtil;
import com.my.wangpan.Utils.QRCodeUtil;
import com.my.wangpan.generator.domain.FileFolder;
import com.my.wangpan.generator.domain.FileStore;
import com.my.wangpan.generator.domain.MyFile;
import com.my.wangpan.generator.domain.TempFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Folder;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
@Controller
public class FileController extends BaseController{
    /**
     * 上传临时文件
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/uploadTempFile")
    public String updateFile(@RequestParam MultipartFile file,String url) throws IOException {
        session.setAttribute("imgPath","/");
        String name = file.getOriginalFilename().replaceAll(" ","");
        if(!checkTarget(name)){
            log.info("文件上传失败！");
            session.setAttribute("msg","文件名不符合规范");
            return "redirect:/temp-file";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(new Date());
        //加上uuid防止重名
        String path = "temp/"+dateStr+"/"+ UUID.randomUUID();
        try{
            if(FTPUtil.uploadFile(path,name,file.getInputStream())){
                //上传成功
                log.info("临时文件上传成功！");
                TempFile tempFile = TempFile.builder().fileName(name).filePath(path).size(String.valueOf(file.getSize())).uploadTime(new Date()).build();
                if(tempFileService.save(tempFile)){
                    try{
                        String id = UUID.randomUUID().toString();
                        String p = request.getSession().getServletContext().getRealPath("/user_img/");
                        long t = tempFile.getUploadTime().getTime();
                        url = url + "/file/share?t="+UUID.randomUUID().toString().substring(0,10)+"&f="+tempFile.getFileId()+"&p"+String.valueOf(file.getSize())+"&flag=2";
                        File targetFile = new File(p,"");
                        if(!targetFile.exists()){
                            targetFile.mkdirs();
                        }
                        File f = new File(p,id+".jpg");
                        if(!f.exists()){
                            //文件不存在，生成二维码并保存文件
                            OutputStream os = new FileOutputStream(f);
                            QRCodeUtil.encode(url,"/static/img/logo.png",os,true);
                            os.close();
                        }
                        //异步删除临时文件
                        tempFileService.removeById(tempFile.getFileId());
                        session.setAttribute("imgPath","user_img"+id+".jpg");
                        session.setAttribute("url",url);
                        session.setAttribute("msg","上传成功，扫码/访问链接下载!");
                        return "redirect:/temp-file";
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    log.info("临时文件数据库写入失败!"+name);
                    session.setAttribute("url","error");
                    session.setAttribute("msg", "服务器出错了，临时文件上传失败!");
                }
            }else {
                //上传失败
                //上传失败
                log.info("临时文件上传失败!"+name);
                session.setAttribute("url","error");
                session.setAttribute("msg", "服务器出错了，上传失败!");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/temp-file";
    }

    /**
     * 文件上传 ok
     * @param file
     * @return
     */
    @PostMapping("/uploadFile")
    @ResponseBody
    public Map<String,Object> uploadFile(MultipartFile file){
        Map<String,Object> map = new HashMap<>();
        if(fileStoreService.getFileStoreByUserId(loginUser.getUserId()).getPermission() != 0){
            log.info("没有权限！上传失败");
            map.put("code",499);
            return map;
        }
        FileStore store = fileStoreService.getFileStoreByUserId(loginUser.getUserId());
        //父文件夹的id
        Integer parentId = Integer.parseInt(request.getHeader("id"));
        String name = file.getOriginalFilename().replaceAll(" ","");
        //获取当前文件夹下的全部文件，检查是否重名
        List<MyFile> list = null;
        if(parentId==0){
            //根目录
            list = myFileService.getRootFilesByFileStoreId(loginUser.getFileStoreId());
        }else {
            list = myFileService.getFilesByParentFolderId(parentId);
        }
        //遍历list，查找重名文件
        for(int i = 0;i < list.size();i++){
            if((list.get(i).getMyFileName()+list.get(i).getPostfix()).equals(name)){
                log.info("存在重名文件，上传失败");
                map.put("code",501);
                return map;
            }
        }
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
        String dateStr = format.format(new Date());
        String path = loginUser.getUserId()+"/"+dateStr+"/"+parentId;
        if(!checkTarget(name)){
            log.error("上传失败!文件名不符合规范...");
            map.put("code", 502);
            return map;
        }
        int sizeInt = Math.toIntExact(file.getSize() / 1024);
        //仓库能否放得下文件
        if(store.getCurrentSize() > store.getMaxSize()){
            log.info("上传失败！文件仓库已满");
            map.put("code",503);
        }
        //处理文件大小
        String size = String.valueOf(file.getSize() / 1024);
        int indexDot = size.lastIndexOf(".");
        //截取整数部分
        if(indexDot != -1)size = size.substring(0,indexDot);
        int index = name.lastIndexOf(".");
        String tempName = name;
        String postfix = "";
        int type = 4;
        if(index != -1){ //确定文件名和后缀
            tempName = name.substring(index);
            name = name.substring(0,index);
            type = getType(tempName.toLowerCase());
            postfix = tempName.toLowerCase();
        }
        try{
            if(FTPUtil.uploadFile(path,name+postfix, file.getInputStream())){
               //上传成功
                log.info("文件上传成功！"+file.getOriginalFilename());
                //向数据库写入数据
                MyFile file1 = MyFile.builder().myFileName(name).myFilePath(path).fileStoreId(loginUser.getFileStoreId())
                        .uploadTime(new Date()).parentFolderId(parentId).size(Integer.parseInt(size)).type(type).postfix(postfix)
                        .downloadTime(0).build();
                boolean save = myFileService.save(file1);
                //更新仓库表大小
                FileStore s = fileStoreService.getFileStoreByUserId(loginUser.getFileStoreId());
                s.setCurrentSize(s.getCurrentSize()+Integer.parseInt(size));
                boolean b = fileStoreService.saveOrUpdate(s);
                //休眠两秒
                try{
                    Thread.sleep(2000);
                    map.put("code",200);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
                log.info("文件上传失败！");
                map.put("code",504);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 下载文件 ok
     * @param fId
     * @return
     */
    @ResponseBody
    @GetMapping("/downloadFile")
    public String downloadFile(Integer fId){
        if(fileStoreService.getFileStoreByUserId(loginUser.getFileStoreId()).getPermission() == 2){
            log.info("用户没有权限下载");
            return "redirect:/error401Page";
        }
        //获取文件信息
        MyFile file = myFileService.getById(fId);
        String rotePath = file.getMyFilePath();
        String filename = file.getMyFileName();
        String postfix = file.getPostfix();
        try{
            //FTP上拉取
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            response.setCharacterEncoding("utf-8");
            //设置返回类型
            response.setContentType("multipart/form-data");
            //文件名转码
            response.setHeader("Content-Disposition","attachment;fileName="+ URLEncoder.encode(filename+postfix,"utf-8"));
            boolean b = FTPUtil.downloadFile(rotePath, filename+postfix, os);
            if(b){
                //更新数据库（下载次数）
                file.setDownloadTime(file.getDownloadTime()+1);
                myFileService.saveOrUpdate(file);
                os.flush();
                os.close();
                log.info("文件下载成功!");
            }else{
                log.info("下载出问题了");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "success";
    }

    /**
     * 删除文件
     * @param fId
     * @param folder
     * @return
     */
    @GetMapping("/deleteFile")
    public String deleteFile(@RequestParam Integer fId,Integer folder){
        MyFile file = myFileService.getById(fId);
        String myFilePath = file.getMyFilePath();
        String fileName = file.getMyFileName()+file.getPostfix();
        if(FTPUtil.deleteFile(myFilePath,fileName)){
            //修改数据库
            boolean b = myFileService.removeById(fId);
            FileStore fileStoreByUserId = fileStoreService.getFileStoreByUserId(loginUser.getUserId());
            fileStoreByUserId.setCurrentSize(fileStoreByUserId.getCurrentSize()-file.getSize());
            b = fileStoreService.saveOrUpdate(fileStoreByUserId);
            if(b){
                log.info("文件删除成功！");
            }else{
                log.info("数据库修改失败！");
            }
        }else{
            log.info("FTP服务器修改失败！");
        }
        return "redirect:/files?fId="+folder;
    }

    /**
     * 删除文件夹
     * @param fId
     * @return
     */
    @GetMapping("/deleteFolder")
    public String deleteFolder(Integer fId){
        if (fId == 0){
            return "no";
        }
        FileFolder folder = fileFolderService.getById(fId);
        boolean b = deleteFolders(folder);
        if(b){
            return folder.getParentFolderId() == 0?"redirect:/files":"redirect:/files?fId="+folder.getParentFolderId();
        }
        return "失败";
    }

    /**
     * 递归删除文件夹
     * @param folder
     * @return
     */
    public boolean deleteFolders(FileFolder folder){
        boolean flag = true;
        //获取当前文件夹下的所有子文件夹
        List<FileFolder> folders = fileFolderService.getFileFolderByParentFolderId(folder.getFileFolderId());
        //获取全部文件
        List<MyFile> files = myFileService.getFilesByParentFolderId(folder.getFileFolderId());

        //删除全部子文件
        for(MyFile f : files){
            boolean b = FTPUtil.deleteFile(f.getMyFilePath(), f.getMyFileName() + f.getPostfix());
            if(b){
                log.info("文件删除成功！"+f);
            }else {
                log.info("文件删除失败！"+f);
                flag = false;
            }
        }
        //递归删除子文件夹
        for(FileFolder f : folders){
            flag = deleteFolders(f);
            if(!flag) return flag;
        }
        flag = fileFolderService.removeById(folder.getFileFolderId());
        return flag;
    }

    /**
     * 添加文件夹
     * @param folder
     * @param map
     * @return
     */
     @PostMapping("/addFolder")
     public String addFolder(FileFolder folder,Map<String,Object> map){
        //获取当前文件夹下的所有文件夹，检查重名
         List<FileFolder> list = null;
         if(folder.getParentFolderId() == 0){
             list = fileFolderService.getRootFoldersByFileStoreId(loginUser.getFileStoreId());
         }else list = fileFolderService.getFileFolderByParentFolderId(folder.getFileFolderId());
         for(FileFolder f : list){
             if(f.getFileFolderName().equals(folder.getFileFolderName())){
                 log.info("添加文件夹失败！存在重名文件夹");
                 map.put("msg","添加文件夹失败！存在重名文件夹");
                 return "redirect:/files?fId="+folder.getParentFolderId();
             }
         }
         folder.setFileStoreId(loginUser.getFileStoreId());
         folder.setTime(new Date());
         //存储到数据库
         boolean save = fileFolderService.save(folder);
         log.info("添加文件夹成功");
         return "redirect:/files?fId="+folder.getParentFolderId();
     }

    /**
     * 重命名文件夹
     * @param folder
     * @param map
     * @return
     */
    @PostMapping("/updateFolder")
    public String updateFolder(FileFolder folder,Map<String, Object> map) {
        //获得文件夹的数据库信息
        FileFolder fileFolder = fileFolderService.getFileFolderByFileFolderId(folder.getFileFolderId());
        fileFolder.setFileFolderName(folder.getFileFolderName());
        //获得当前目录下的所有文件夹,用于检查文件夹是否已经存在
        List<FileFolder> fileFolders = fileFolderService.getFileFolderByParentFolderId(fileFolder.getParentFolderId());
        for (int i = 0; i < fileFolders.size(); i++) {
            FileFolder folder1 = fileFolders.get(i);
            if (folder1.getFileFolderName().equals(folder.getFileFolderName()) && folder1.getFileFolderId() != folder.getFileFolderId()){
                log.info("重命名文件夹失败!文件夹已存在...");
                return "redirect:/files?error=2&fId="+fileFolder.getParentFolderId();
            }
        }
        //向数据库写入数据
        fileFolderService.saveOrUpdate(fileFolder);
        log.info("重命名文件夹成功!"+folder);
        return "redirect:/files?fId="+fileFolder.getParentFolderId();
    }

    /**
     * 重命名文件
     * @param file
     * @param map
     * @return
     */
    @PostMapping("/updateFileName")
    public String updateFileName(MyFile file,Map<String, Object> map) {
        MyFile myFile = myFileService.getById(file.getMyFileId());
        if (myFile != null){
            String oldName = myFile.getMyFileName();
            String neeName = file.getMyFileName();
            String postFix = myFile.getPostfix();
            boolean b = FTPUtil.renameFile(myFile.getMyFilePath(),oldName+postFix, neeName+postFix);
            if(b){
                //修改数据库
                myFile.setMyFileName(neeName);
                myFileService.saveOrUpdate(myFile);
                log.info("修改文件名成功！"+oldName+"-->"+neeName);
            }
        }
        return "redirect:/files?fId="+myFile.getParentFolderId();
    }

    /**
     * 获取二维码
     * @param id
     * @param url
     * @return
     */
    @GetMapping("getQrCode")
    @ResponseBody
    public Map<String,Object> getQrCode(@RequestParam Integer id,@RequestParam String url){
        Map<String,Object> map = new HashMap<>();
        map.put("imgPath","https://assets.leetcode.cn/aliyun-lc-upload/default_avatar.png?x-oss-process=image%2Fformat%2Cwebp");
        if(id != null){
            MyFile file = myFileService.getById(id);
            if(file != null){
                try{
                    String path = request.getSession().getServletContext().getRealPath("/user_img/");
                    url = url+"/file/share?t="+ UUID.randomUUID().toString().substring(0,10) +"&f="+file.getMyFileId()+"&p="+file.getUploadTime().getTime()+""+file.getSize()+"&flag=1";
                    File targetFile = new File(path, "");
                    if(!targetFile.exists()){
                        targetFile.mkdirs();
                    }
                    File f = new File(path,id+".jpg");
                    if(!f.exists()){
                        //文件不存在，生成二维码并保存文件
                        OutputStream os = new FileOutputStream(f);
                        QRCodeUtil.encode(url,"/static/img/logo.png",os,true);
                        os.close();
                    }
                    map.put("imgPath","user_img/"+id+".jpg");
                    map.put("url",url);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    /**
     * 下载分享文件
     * @param f
     * @param p
     * @param t
     * @param flag
     * @return
     */
    @ResponseBody
    @GetMapping("/file/share")
    public String shareFile(Integer f,String p,String t,Integer flag){
        String fileNameTemp;
        String remotePath;
        String fileName;
        Integer times = 0;
        if(f == null || p == null || t == null || flag == null){
            log.info("下载分享文件失败");
            return "redirect:/error400Page";
        }
        if(flag == 1){
            //获取文件信息
            MyFile myFile = myFileService.getById(f);
            if (myFile == null){
                return "redirect:/error404Page";
            }
            String pwd = myFile.getUploadTime().getTime()+""+myFile.getSize();
            if (!pwd.equals(p)){
                return "redirect:/error400Page";
            }
            remotePath = myFile.getMyFilePath();
            fileName = myFile.getMyFileName()+myFile.getPostfix();
        }else if(flag == 2){
            TempFile tempFile = tempFileService.getById(f);
            if (tempFile == null){
                return "redirect:/error404Page";
            }
            Long test = tempFile.getUploadTime().getTime();

            String pwd = tempFile.getSize();
            if (!pwd.equals(p)){
                return "redirect:/error400Page";
            }
            remotePath = tempFile.getFilePath();
            fileName = tempFile.getFileName();
        }else  return "redirect:/error400Page";
        fileNameTemp = fileName;
        try{
            //解决乱码
            boolean is = isMSBrowser(request);
            if(is){
                //ie乱码
                fileNameTemp = URLEncoder.encode(fileNameTemp,"UTF-8");
            }else {
                fileNameTemp = new String(fileNameTemp.getBytes("UTF-8"),"ISO-8859-1");
            }
            //FTP服务器拉取
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            response.setCharacterEncoding("utf-8");
            // 设置返回类型
            response.setContentType("multipart/form-data");
            // 文件名转码一下，不然会出现中文乱码
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileNameTemp);

            if(FTPUtil.downloadFile(remotePath,fileNameTemp,os)){
                //更新数据库
                if(flag == 1){
                    MyFile file = myFileService.getById(f);
                    file.setDownloadTime(file.getDownloadTime()+1);
                    myFileService.saveOrUpdate(file);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "success";
    }

    /**
     * 查看文件类型
     * @param type
     * @return
     */
    private int getType(String type) {
        if (".chm".equals(type)||".txt".equals(type)||".xmind".equals(type)||".xlsx".equals(type)||".md".equals(type)
                ||".doc".equals(type)||".docx".equals(type)||".pptx".equals(type)
                ||".wps".equals(type)||".word".equals(type)||".html".equals(type)||".pdf".equals(type)){
            return  1;
        }else if (".bmp".equals(type)||".gif".equals(type)||".jpg".equals(type)||".ico".equals(type)||".vsd".equals(type)
                ||".pic".equals(type)||".png".equals(type)||".jepg".equals(type)||".jpeg".equals(type)||".webp".equals(type)
                ||".svg".equals(type)){
            return 2;
        } else if (".avi".equals(type)||".mov".equals(type)||".qt".equals(type)
                ||".asf".equals(type)||".rm".equals(type)||".navi".equals(type)||".wav".equals(type)
                ||".mp4".equals(type)||".mkv".equals(type)||".webm".equals(type)){
            return 3;
        } else if (".mp3".equals(type)||".wma".equals(type)){
            return 4;
        } else {
            return 5;
        }
    }

    /**
     * 检验文件名是否合法 [汉字,字符,数字,下划线,英文句号,横线]
     * @param target
     * @return
     */
    private boolean checkTarget(String target) {
        final String format = "[^\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w-_.]";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(target);
        return !matcher.find();
    }

    /**
     * 检测当前浏览器是否为ie
     * @param request
     * @return
     */
    public static boolean isMSBrowser(HttpServletRequest request) {
        String[] IEBrowserSignals = {"MSIE", "Trident", "Edge"};
        String userAgent = request.getHeader("User-Agent");
        for (String signal : IEBrowserSignals) {
            if (userAgent.contains(signal)){
                return true;
            }
        }
        return false;
    }
}
