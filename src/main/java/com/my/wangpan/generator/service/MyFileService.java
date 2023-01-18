package com.my.wangpan.generator.service;

import com.my.wangpan.generator.domain.FileStoreStatistics;
import com.my.wangpan.generator.domain.MyFile;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author hanbaomao
* @description 针对表【my_file】的数据库操作Service
* @createDate 2023-01-14 15:37:09
*/
public interface MyFileService extends IService<MyFile> {

    /**
     * 获取仓库的统计信息
     * @param fileStoreId
     * @return
     */
    FileStoreStatistics getCountStatistics(Integer fileStoreId);

    List<MyFile> getRootFilesByFileStoreId(Integer fileStoreId);

    List<MyFile> getFilesByParentFolderId(Integer fId);

    List<MyFile> getFilesByType(Integer fileStoreId, int i);
}
