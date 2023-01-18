package com.my.wangpan.generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.wangpan.generator.domain.TempFile;
import com.my.wangpan.generator.mapper.*;
import com.my.wangpan.generator.service.TempFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author hanbaomao
* @description 针对表【temp_file】的数据库操作Service实现
* @createDate 2023-01-14 15:37:09
*/
@Service
public class TempFileServiceImpl extends ServiceImpl<TempFileMapper, TempFile>
    implements TempFileService{
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

}




