package com.my.wangpan.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.wangpan.generator.domain.FileStore;
import com.my.wangpan.generator.mapper.*;
import com.my.wangpan.generator.service.FileStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author hanbaomao
* @description 针对表【file_store】的数据库操作Service实现
* @createDate 2023-01-14 15:37:09
*/
@Service
public class FileStoreServiceImpl extends ServiceImpl<FileStoreMapper, FileStore>
    implements FileStoreService {
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
     * 根据用户id获取文件仓库
     * @param userId
     * @return
     */
    @Override
    public FileStore getFileStoreByUserId(Integer userId) {
        QueryWrapper wrapper = new QueryWrapper<>().eq("user_id",userId);
        return fileStoreMapper.selectOne(wrapper);
    }
}




