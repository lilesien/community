package com.lilesien.communicate.service;

import com.lilesien.communicate.mapper.UserMapper;
import com.lilesien.communicate.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        User dbUser = userMapper.selectByAccountId(user.getAccountId());
        if(dbUser == null){
            //设置创建时间
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else {
            dbUser.setToken(user.getToken());
            userMapper.updateById(dbUser);
        }
    }
}
