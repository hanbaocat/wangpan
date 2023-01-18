package com.my.wangpan.generator.service;

import com.my.wangpan.generator.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author hanbaomao
* @description 针对表【user】的数据库操作Service
* @createDate 2023-01-14 15:37:09
*/
public interface UserService extends IService<User> {

    User getByEmail(String email);

    User getByEmailAndPassword(String email,String password);
}
