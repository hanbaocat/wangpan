package com.my.wangpan.generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.wangpan.generator.domain.FileStore;
import com.my.wangpan.generator.domain.FileStoreStatistics;
import com.my.wangpan.generator.domain.MyFile;
import com.my.wangpan.generator.mapper.*;
import com.my.wangpan.generator.service.MyFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author hanbaomao
* @description 针对表【my_file】的数据库操作Service实现
* @createDate 2023-01-14 15:37:09
*/
@Service
public class MyFileServiceImpl extends ServiceImpl<MyFileMapper, MyFile>
    implements MyFileService {
    @Autowired
    protected UserMapper userMapper;
    @Autowired
    protected MyFileMapper myFileMapper;
    @Autowired
    protected FileFolderMapper fileFolderMapper;
    @Autowired
    protected FileStoreMapper fileStoreMapper;
    @Autowired
    protected TempFileMapper tempFileMapper;

    /**
     * 获取全部文档
     * @param fileStoreId
     * @param i
     * @return
     */
    @Override
    public List<MyFile> getFilesByType(Integer fileStoreId, int i) {
        return myFileMapper.getFilesByType(fileStoreId,i);
    }

    /**
     * 获取文件仓库的统计信息
     * @param id
     * @return
     */
    public FileStoreStatistics getCountStatistics(Integer id){
        FileStoreStatistics countStatistics = myFileMapper.getCountStatistics(id);
        if(countStatistics==null)countStatistics = FileStoreStatistics.builder().fileCount(0).folderCount(0).doc(0).image(0)
                .other(0).music(0).video(0).build();
        FileStore store = fileStoreMapper.selectById(id);
        countStatistics.setFileStore(store);
        return countStatistics;
    }

    /**
     * 根据父文件夹编号获取文件夹下的全部文件
     * @param fId
     * @return
     */
    @Override
    public List<MyFile> getFilesByParentFolderId(Integer fId) {
        return myFileMapper.getFilesByParentFolderId(fId);
    }

    /**
     * 获取根目录下的所有文件
     * @param fileStoreId
     * @return
     */
    @Override
    public List<MyFile> getRootFilesByFileStoreId(Integer fileStoreId) {
        return myFileMapper.getRootFilesByFileStoreId(fileStoreId);
    }
}




