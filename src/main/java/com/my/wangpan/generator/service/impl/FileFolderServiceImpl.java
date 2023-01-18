package com.my.wangpan.generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.wangpan.generator.domain.FileFolder;
import com.my.wangpan.generator.mapper.*;
import com.my.wangpan.generator.service.FileFolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author hanbaomao
* @description 针对表【file_folder】的数据库操作Service实现
* @createDate 2023-01-14 15:36:24
*/
@Service
public class FileFolderServiceImpl extends ServiceImpl<FileFolderMapper, FileFolder>
    implements FileFolderService {
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
     * 根据仓库id获取当前仓库下的所有文件夹
     * @param fileStoreId
     * @return
     */
    @Override
    public List<FileFolder> getRootFoldersByFileStoreId(Integer fileStoreId) {
        return fileFolderMapper.getRootFoldersByFileStoreId(fileStoreId);
    }

    /**
     * 根据父文件夹id获取文件夹
     * @param fId
     * @return
     */
    @Override
    public List<FileFolder> getFileFolderByParentFolderId(Integer fId) {
        return fileFolderMapper.getFileFolderByParentFolderId(fId);
    }

    /**
     * 根据ID获取文件夹
     * @param fId
     * @return
     */
    @Override
    public FileFolder getFileFolderByFileFolderId(Integer fId) {
        return fileFolderMapper.selectById(fId);
    }
}




