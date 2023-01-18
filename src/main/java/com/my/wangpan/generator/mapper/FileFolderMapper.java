package com.my.wangpan.generator.mapper;

import com.my.wangpan.generator.domain.FileFolder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author hanbaomao
* @description 针对表【file_folder】的数据库操作Mapper
* @createDate 2023-01-14 15:36:24
* @Entity generator.domain.FileFolder
*/
@Mapper
public interface FileFolderMapper extends BaseMapper<FileFolder> {

    @Select("select * from file_folder where file_store_id = #{fileStoreId} and parent_folder_id=0")
    List<FileFolder> getRootFoldersByFileStoreId(Integer fileStoreId);
    @Select("select * from file_folder where parent_folder_id=#{fId}")
    List<FileFolder> getFileFolderByParentFolderId(Integer fId);
}




