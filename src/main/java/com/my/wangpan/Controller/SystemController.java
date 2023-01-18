package com.my.wangpan.Controller;

import com.my.wangpan.generator.domain.FileFolder;
import com.my.wangpan.generator.domain.FileStoreStatistics;
import com.my.wangpan.generator.domain.MyFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 系统页面跳转控制器
 */
@Controller
@Slf4j
public class SystemController extends BaseController{

    /**
     * 前往主页面
     * @param map
     * @return
     */
    @GetMapping("/index")
    public String index(Map<String,Object> map){
        //获取统计信息，前端展示
        FileStoreStatistics statistics = myFileService.getCountStatistics(loginUser.getFileStoreId());
        map.put("statistics",statistics);
        return "u-admin/index";
    }

    /**
     * 查看我的文件
     * @param fId
     * @param fName
     * @param error
     * @param map
     * @return
     */
    @GetMapping("/files")
    public String toFileStorepPage(Integer fId,String fName,Integer error,Map<String,Object> map){
        //判断是否包含错误信息
        if(error != null) {
            if (error == 1) {
                map.put("error", "添加失败！已存在同名文件夹");
            } else if (error == 2) {
                map.put("error", "重命名失败！文件夹已存在");
            }
        }
        //包含的子文件夹
        List<FileFolder> folders = null;
        //包含的文件
        List<MyFile> files = null;
        //当前文件夹信息
        FileFolder nowFolder = null;
        //当前文件夹相对路径
        List<FileFolder> location = new ArrayList<>();
        if(fId == null || fId <= 0){
            //代表当前目录为根目录
            fId = 0;
            //获取根目录下的所有文件夹
            folders = fileFolderService.getRootFoldersByFileStoreId(loginUser.getFileStoreId());
            //获取根目录下的所有文件
            files = myFileService.getRootFilesByFileStoreId(loginUser.getFileStoreId());
            //获取当前文件夹
            nowFolder = FileFolder.builder().fileFolderId(fId).build();
            location.add(nowFolder);
        }else{
            //当前目录为具体目录，访问的文件夹不是当前登录用户所创建的文件夹
            FileFolder folder = fileFolderService.getById(fId);
            if(folder.getFileStoreId() != loginUser.getFileStoreId()){
                return "redirect:/error401Page";
            }
            //当前目录为具体目录，访问的文件夹是当前登录用户所创建的
            folders = fileFolderService.getFileFolderByParentFolderId(fId);
            files = myFileService.getFilesByParentFolderId(fId);
            nowFolder = fileFolderService.getFileFolderByFileFolderId(fId);
            //遍历查询当前目录
            FileFolder temp = nowFolder;
            while(temp.getParentFolderId() !=0){
                temp = fileFolderService.getFileFolderByFileFolderId(temp.getParentFolderId());
                location.add(temp);
            }
        }
        //反转路径
        Collections.reverse(location);
        //获取统计信息
        FileStoreStatistics statistics = myFileService.getCountStatistics(loginUser.getFileStoreId());
        map.put("statistics",statistics);
        map.put("permission",fileStoreService.getFileStoreByUserId(loginUser.getUserId()).getPermission());
        map.put("folders",folders);
        map.put("files",files);
        map.put("nowFolder",nowFolder);
        map.put("location",location);
        log.info("网盘中的数据："+map);
        return "u-admin/files";
    }

    @GetMapping("/upload")
    public String toUploadPage(Integer fId, String fName, Map<String, Object> map){
        //展示所有
        //包含的文件夹
        List<FileFolder> folders;
        //当前文件夹
        FileFolder nowFolder;
        //路径
        List<FileFolder> location = new ArrayList<>();
        if(fId == null || fId <= 0){
            //当前为根目录
            fId = 0;
            folders = fileFolderService.getRootFoldersByFileStoreId(loginUser.getFileStoreId());
            nowFolder = FileFolder.builder().fileFolderId(fId).build();
            location.add(nowFolder);
        }else {
            folders = fileFolderService.getFileFolderByParentFolderId(fId);
            nowFolder = fileFolderService.getFileFolderByFileFolderId(fId);
            FileFolder temp = nowFolder;
            while(temp.getParentFolderId() != 0){
                temp = fileFolderService.getFileFolderByFileFolderId(temp.getParentFolderId());
                location.add(temp);
            }
        }
        Collections.reverse(location);
        //获得统计信息
        FileStoreStatistics statistics = myFileService.getCountStatistics(loginUser.getFileStoreId());
        map.put("statistics", statistics);
        map.put("folders", folders);
        map.put("nowFolder", nowFolder);
        map.put("location", location);
        log.info("网盘页面域中的数据:" + map);
        return "u-admin/upload";
    }

    /**
     * 前往所有文档页面
     * @param map
     * @return
     */
    @GetMapping("/doc-files")
    public String toDocFilePage( Map<String, Object> map){
        List<MyFile> files = myFileService.getFilesByType(loginUser.getFileStoreId(),1);
        //获得统计信息
        FileStoreStatistics statistics = myFileService.getCountStatistics(loginUser.getFileStoreId());
        map.put("statistics", statistics);
        map.put("files", files);
        map.put("permission", fileStoreService.getFileStoreByUserId(loginUser.getUserId()).getPermission());
        return "u-admin/doc-files";
    }

    /**
     * 前往所有视频页面
     * @param map
     * @return
     */
    @GetMapping("/video-files")
    public String toVideoFilePage( Map<String, Object> map){
        List<MyFile> files = myFileService.getFilesByType(loginUser.getFileStoreId(),3);
        //获得统计信息
        FileStoreStatistics statistics = myFileService.getCountStatistics(loginUser.getFileStoreId());
        map.put("statistics", statistics);
        map.put("files", files);
        map.put("permission", fileStoreService.getFileStoreByUserId(loginUser.getUserId()).getPermission());
        return "u-admin/video-files";
    }

    /**
     * 前往所有音频页面
     * @param map
     * @return
     */
    @GetMapping("/music-files")
    public String toMusicFilePage( Map<String, Object> map){
        List<MyFile> files = myFileService.getFilesByType(loginUser.getFileStoreId(),4);
        //获得统计信息
        FileStoreStatistics statistics = myFileService.getCountStatistics(loginUser.getFileStoreId());
        map.put("statistics", statistics);
        map.put("files", files);
        map.put("permission", fileStoreService.getFileStoreByUserId(loginUser.getUserId()).getPermission());
        return "u-admin/music-files";
    }

    /**
     * 前往所有图片页面
     * @param map
     * @return
     */
    @GetMapping("/image-files")
    public String toImageFilePage( Map<String, Object> map){
        List<MyFile> files = myFileService.getFilesByType(loginUser.getFileStoreId(),2);
        //获得统计信息
        FileStoreStatistics statistics = myFileService.getCountStatistics(loginUser.getFileStoreId());
        map.put("statistics", statistics);
        map.put("files", files);
        map.put("permission", fileStoreService.getFileStoreByUserId(loginUser.getUserId()).getPermission());
        return "u-admin/image-files";
    }

    /**
     * 前往其他页面
     * @param map
     * @return
     */
    @GetMapping("/other-files")
    public String toOtherFilePage( Map<String, Object> map){
        List<MyFile> files = myFileService.getFilesByType(loginUser.getFileStoreId(),5);
        //获得统计信息
        FileStoreStatistics statistics = myFileService.getCountStatistics(loginUser.getFileStoreId());
        map.put("statistics", statistics);
        map.put("files", files);
        map.put("permission", fileStoreService.getFileStoreByUserId(loginUser.getUserId()).getPermission());
        return "u-admin/other-files";
    }
    /**
     * 前往帮助页面
     */
    @GetMapping("/help")
    public String helpPage(Map<String, Object> map) {
        //获得统计信息
        FileStoreStatistics statistics = myFileService.getCountStatistics(loginUser.getFileStoreId());
        map.put("statistics", statistics);
        return "u-admin/help";
    }
}
