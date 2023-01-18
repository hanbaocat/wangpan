package com.my.wangpan.generator.mapper;

import com.my.wangpan.generator.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author hanbaomao
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-01-14 15:37:09
* @Entity generator.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




