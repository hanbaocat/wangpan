package com.my.wangpan.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.wangpan.generator.domain.User;
import com.my.wangpan.generator.mapper.*;
import com.my.wangpan.generator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author hanbaomao
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-01-14 15:37:09
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {
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

    @Override
    public User getByEmail(String email) {
        QueryWrapper queryWrapper = (QueryWrapper) new QueryWrapper().eq("email",email);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User getByEmailAndPassword(String email,String password) {
        QueryWrapper queryWrapper = (QueryWrapper) new QueryWrapper().eq("email",email);
        queryWrapper.eq("password",password);
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }
}




