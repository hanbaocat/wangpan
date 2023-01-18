package com.my.wangpan.generator.mapper;

import com.my.wangpan.generator.domain.FileStoreStatistics;
import com.my.wangpan.generator.domain.MyFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author hanbaomao
* @description 针对表【my_file】的数据库操作Mapper
* @createDate 2023-01-14 15:37:09
* @Entity generator.domain.MyFile
*/
@Mapper
public interface MyFileMapper extends BaseMapper<MyFile> {
    @Select("select sum(type = 1) doc,sum(type = 2) image,sum(type = 3) video,sum(type = 4) music,sum(type = 5) other,count(*) from my_file where file_store_id = #{id}")
    FileStoreStatistics getCountStatistics(Integer id);
    @Select("select * from my_file where parent_folder_id = 0")
    List<MyFile> getRootFilesByFileStoreId(Integer fileStoreId);
    @Select("select * from my_file where parent_folder_id = #{fileStoreId}")
    List<MyFile> getFilesByParentFolderId(Integer fId);
    @Select("select * from my_file where file_store_id = #{fileStoreId} and type=#{i}")
    List<MyFile> getFilesByType(Integer fileStoreId, int i);
}




