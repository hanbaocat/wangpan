package com.my.wangpan.generator.service;

import com.my.wangpan.generator.domain.FileStore;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author hanbaomao
* @description 针对表【file_store】的数据库操作Service
* @createDate 2023-01-14 15:37:09
*/
public interface FileStoreService extends IService<FileStore> {

    FileStore getFileStoreByUserId(Integer userId);
}
