package com.my.wangpan.generator.service;

import com.my.wangpan.generator.domain.FileFolder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author hanbaomao
* @description 针对表【file_folder】的数据库操作Service
* @createDate 2023-01-14 15:36:24
*/
public interface FileFolderService extends IService<FileFolder> {

    List<FileFolder> getRootFoldersByFileStoreId(Integer fileStoreId);

    List<FileFolder> getFileFolderByParentFolderId(Integer fId);

    FileFolder getFileFolderByFileFolderId(Integer fId);
}
